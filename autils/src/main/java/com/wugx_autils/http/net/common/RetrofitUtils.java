package com.wugx_autils.http.net.common;

import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wugx_autils.AUtils;
import com.wugx_autils.http.net.interceptor.CommonParamsInterceptor;
import com.wugx_autils.http.net.interceptor.HttpCacheInterceptor;
import com.wugx_autils.http.net.interceptor.HttpHeaderInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by zhpan on 2018/3/21.
 */
public class RetrofitUtils {
    public static OkHttpClient.Builder getOkHttpClientBuilder() {
        File cacheFile = new File(Utils.getApp().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

        CommonParamsInterceptor.Builder builder = new CommonParamsInterceptor.Builder();
        if (AUtils.getInstance().reqParams != null) {
            for (String paramKey : AUtils.getInstance().reqParams.keySet()) {
                builder.addBodyParams(paramKey, AUtils.getInstance().reqParams.get(paramKey));
            }
        }
        CommonParamsInterceptor commonParamsInterceptor = builder.build();

        //配置一些公共的请求参数
//        CommonParamsInterceptor commonParamsInterceptor = new CommonParamsInterceptor.Builder()
////                .addHeaderParams(key,vaule)  //添加header
////                .addBodyParams(key,vaule) //添加body
//                .build();

        return new OkHttpClient.Builder()
                .readTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(commonParamsInterceptor)
//                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new HttpHeaderInterceptor())
                .addNetworkInterceptor(new HttpCacheInterceptor())
                // .sslSocketFactory(SslContextFactory.getSSLSocketFactoryForTwoWay())  // https认证 如果要使用https且为自定义证书 可以去掉这两行注释，并自行配制证书。
                // .hostnameVerifier(new SafeHostnameVerifier())
                .cache(cache);
    }

//    public static Retrofit.Builder getRetrofitBuilder(String baseUrl) {
//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
//        OkHttpClient okHttpClient = RetrofitUtils.getOkHttpClientBuilder().build();
//        return new Retrofit.Builder()
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .baseUrl(baseUrl);
//    }

    /**
     * 增加 factory
     *
     * @param baseUrl
     * @param factory
     * @return
     */
    public static Retrofit.Builder getRetrofitBuilder(String baseUrl, Converter.Factory factory) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
        OkHttpClient okHttpClient = RetrofitUtils.getOkHttpClientBuilder().build();
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(factory == null ? GsonConverterFactory.create(gson) : factory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl);
    }

    public static <T> Observable<T> applySchedulers(Observable<T> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
