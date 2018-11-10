package com.wugx_utils.http;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.LogUtils;
import com.wugx_autils.base.BaseActivity;
import com.wugx_autils.http.dialog.DialogUtils;
import com.wugx_autils.http.net.common.DefaultObserver;
import com.wugx_autils.http.net.common.ProgressUtils;
import com.wugx_autils.http.net.upload.UploadProgressListener;
import com.wugx_autils.http.net.upload.UploadProgressRequestBody;
import com.wugx_utils.entity.BasicBean;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author Wugx
 * @date 2018/11/9
 */
public class UploadFileUtils {

    /**
     * 上传文件
     *
     * @param ba
     * @param url
     * @param fileKey  文件key
     * @param file
     * @param parms    一般参数map
     * @param listener
     */
    public static void uploadFile(BaseActivity ba, String url, String fileKey, File file,
                                  Map<String, String> parms, final UpFileProgressListener listener) {
        final MaterialDialog dialog = new DialogUtils().showFileProgressBar(ba);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        //一般参数
        for (String parmKey : parms.keySet()) {
            builder.addFormDataPart(parmKey, parms.get(parmKey));
        }
        LogUtils.d("上传文件参数>>>" + parms.toString());
        //文件
//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //显示上传进度
        UploadProgressRequestBody uploadProgressRequestBody = new UploadProgressRequestBody(requestBody, new UploadProgressListener() {
            long time = System.currentTimeMillis();

            @Override
            public void onProgress(long currentBytesCount, long totalBytesCount) {
                //100毫秒更新一次
                if ((System.currentTimeMillis() - time) > 100) {
                    int progress = (int) (((float) currentBytesCount / (float) totalBytesCount) * 100);
                    dialog.setProgress(progress);
                    listener.progress(progress);
                    time = System.currentTimeMillis();
                }
                if (currentBytesCount == totalBytesCount) {
                    listener.progress(100);
                    dialog.setProgress(100);
                }
            }
        });

        builder.addFormDataPart(fileKey, file.getName(), uploadProgressRequestBody);
        List<MultipartBody.Part> partList = builder.build().parts();

        RetrofitHelper.getApiService()
                .uploadFiles(/*url,*/ partList)
                .subscribeOn(Schedulers.io())
                .compose(ba.<BasicBean>bindToLifecycle())
//                .compose(ProgressUtils.<BasicBean>applyProgressBar(ba, "上传文件中..."))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicBean>() {
                    @Override
                    public void onSuccess(BasicBean response) {
                        dialog.dismiss();
                        listener.success(response);
//                        ToastUtils.show("文件上传成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dialog.dismiss();
                        listener.failure();
                    }
                });
    }


    /**
     * 单文件上传 方法二
     */
    public static void uploadFile2(BaseActivity ba, String url, String fileKey, File file, Map<String, String> parms, final UpFileProgressListener listener) {
        //  图片参数
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileBody = MultipartBody.Part.createFormData(fileKey, file.getName(), requestFile);

        Map<String, RequestBody> upLoadMap = new HashMap<>();
        for (String parmKey : parms.keySet()) {
            upLoadMap.put(parmKey, RequestBody.create(MediaType.parse("multipart/form-data"), parms.get(parmKey)));
        }
        RetrofitHelper.getApiService()
                .uploadFiles(url, upLoadMap, fileBody)
                .subscribeOn(Schedulers.io())
                .compose(ba.<BasicBean>bindToLifecycle())
                .compose(ProgressUtils.<BasicBean>applyProgressBar(ba, "上传文件..."))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<BasicBean>() {
                    @Override
                    public void onSuccess(BasicBean response) {
                        listener.success(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        listener.failure();
                    }
                });
    }

    public interface UpFileProgressListener<T> {
        void progress(int progress);

        void failure();

        void success(T t);
    }

}
