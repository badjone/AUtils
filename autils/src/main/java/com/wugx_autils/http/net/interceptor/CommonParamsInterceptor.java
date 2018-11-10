package com.wugx_autils.http.net.interceptor;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 公共拦截器
 *
 * @author wugx
 * @data 2018/6/11.
 */

public class CommonParamsInterceptor implements Interceptor {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private Map<String, String> bodyParms = new HashMap<>();
    private Map<String, String> headerParamsMap = new HashMap<>();

    public CommonParamsInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取到请求
        Request oldRequest = chain.request();
        //无网络时读取缓存
        if (!NetworkUtils.isConnected()) {
            oldRequest = oldRequest.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            return chain.proceed(oldRequest);
        }
        long time1 = System.nanoTime();
        //获取请求的方式
        String method = oldRequest.method();
        //获取请求的路径
        String oldUrl = oldRequest.url().toString();

        Request.Builder newBuilder = oldRequest.newBuilder();

        newBuilder.method(oldRequest.method(), oldRequest.body());
        //添加公共参数,添加到header中
        if (headerParamsMap.size() > 0) {
            for (Map.Entry<String, String> params : headerParamsMap.entrySet()) {
                newBuilder.header(params.getKey(), params.getValue());
            }
        }

        //要添加的公共参数...map
//        map.put("source", "android");
//        bodyParms.put("token", "dd09de42-96c5-4c1e-826d-050e2df0b54e"); //添加到url后面
//        bodyParms.put("deviceid", "5e65ba9171367cf1");
//        bodyParms.put("userid", "ff808081636896f2016369095cc70000");

        String parms = "";
        if ("GET".equals(method)) {
            StringBuilder stringBuilder = showGetParms(oldUrl, bodyParms);
            parms = stringBuilder.toString().replace(oldUrl, "");
            String newUrl = stringBuilder.toString();//新的路径
            //拿着新的路径重新构建请求
            newBuilder = newBuilder
                    .url(newUrl);
        } else if ("POST".equals(method)) {
            //先获取到老的请求的实体内容
            RequestBody oldRequestBody = oldRequest.body();//....之前的请求参数,,,需要放到新的请求实体内容中去
            //如果请求调用的是上面doPost方法
            if (oldRequestBody instanceof FormBody) {
                FormBody oldBody = (FormBody) oldRequestBody;
                //构建一个新的请求实体内容
                FormBody.Builder builder = new FormBody.Builder();
                StringBuilder sb = showPostParms(bodyParms, oldBody, builder);
                FormBody newBody = builder.build();//新的请求实体内容

                parms = sb.toString();
                //构建一个新的请求
                newBuilder = newBuilder
                        .url(oldUrl)
                        .post(newBody);
            } else if (oldRequestBody instanceof MultipartBody) {
                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                List<MultipartBody.Part> parts = ((MultipartBody) oldRequestBody).parts();
                parms = showMultiParms(bodyParms, builder, parts);

                newBuilder = newBuilder.url(oldUrl);
            }
        }
        Request newRequest = newBuilder.build();

        Response response = chain.proceed(newRequest);
        long time2 = System.nanoTime();
        //返回数据...
        BufferedSource source = response.body().source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        String logStr = "\n--------------------"
                .concat("  begin--------------------\n")
                .concat("\nmethod ->").concat(newRequest.method() + "")
                .concat("\nnetwork code->").concat(response.code() + "")
                .concat("\nparamsStr-> ").concat(String.format("[%s]", parms) + "")
                .concat("\ntime-> ").concat(String.format(Locale.getDefault(), "%.1fms", (time2 - time1) / 1e6d))
                .concat("\nurl->").concat(URLDecoder.decode(newRequest.url().toString(), "UTF-8") + "")
                .concat("\nrequest headers->").concat(newRequest.headers() + "")
                .concat("\ncall-data->").concat(buffer.clone().readString(Charset.forName("UTF-8")));
//        Log.w("网络请求:", logStr);

        LogUtils.w(logStr);
        return response;
    }

    /********************************获取几种请求方式的参数*******************************************/
    @NonNull
    private String showMultiParms(Map<String, String> bodyParms, MultipartBody.Builder builder, List<MultipartBody.Part> parts) {
        String parms;
        StringBuilder sb = new StringBuilder();
        if (bodyParms != null) {
            for (Map.Entry<String, String> entry : bodyParms.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        //文件part
        for (int i = 0; i < parts.size(); i++) {
            builder.addPart(parts.get(i)).build();
        }
        //删掉最后一个&符号
        if (sb.indexOf("&") != -1) {
            sb.deleteCharAt(sb.lastIndexOf("&"));
        }
        parms = sb.toString();
        return parms;
    }

    @NonNull
    private StringBuilder showPostParms(Map<String, String> bodyParms, FormBody oldBody, FormBody.Builder builder) {
        StringBuilder sb = new StringBuilder();
        //1.添加老的参数
        for (int i = 0; i < oldBody.size(); i++) {
            builder.add(oldBody.name(i), oldBody.value(i));
            sb.append(oldBody.name(i)).append("=").append(oldBody.value(i)).append("&");
        }

        //2.添加公共参数
        for (Map.Entry<String, String> entry : bodyParms.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return sb;
    }

    @NonNull
    private StringBuilder showGetParms(String oldUrl, Map<String, String> bodyParms) {
        StringBuilder stringBuilder = new StringBuilder();//创建一个stringBuilder
        stringBuilder.append(oldUrl);
        if (oldUrl.contains("?")) {
            //?在最后面....2类型
            if (oldUrl.indexOf("?") == oldUrl.length() - 1) {

            } else {
                //3类型...拼接上&
                stringBuilder.append("&");
            }
        } else {
            //不包含? 属于1类型,,,先拼接上?号
            stringBuilder.append("?");
        }
        //添加公共参数....
        for (Map.Entry<String, String> entry : bodyParms.entrySet()) {
            //拼接
            stringBuilder.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }

        //删掉最后一个&符号
        if (stringBuilder.indexOf("&") != -1) {
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("&"));
        }
        return stringBuilder;
    }


    public static class Builder {
        CommonParamsInterceptor commonParamsInterceptor;

        public Builder() {
            commonParamsInterceptor = new CommonParamsInterceptor();
        }

        public CommonParamsInterceptor.Builder addHeaderParams(String key, String value) {
            commonParamsInterceptor.headerParamsMap.put(key, value);
            return this;
        }

        public CommonParamsInterceptor.Builder addBodyParams(String key, String value) {
            commonParamsInterceptor.bodyParms.put(key, value);
            return this;
        }

        public CommonParamsInterceptor build() {
            return commonParamsInterceptor;
        }
    }
}

