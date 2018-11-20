package com.wugx_autils.http.net.common;


import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.wugx_autils.http.net.download.DownloadListener;
import com.wugx_autils.http.net.download.DownloadProgressHandler;
import com.wugx_autils.http.net.download.ProgressHelper;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

/**
 * Created by zhpan on 2018/3/21.
 */
public class DownloadUtils {
    private DownloadListener mDownloadListener;
    private CompositeDisposable mDisposables;
    /**
     * 下载文件名
     */
    private String down_file_name = "zzz.apk";

    public DownloadUtils() {
        mDisposables = new CompositeDisposable();
    }

    /**
     * 开始下载
     *
     * @param url
     */
    public void download(@NonNull String baseUrl, @NonNull String url, DownloadListener downloadListener) {
        mDownloadListener = downloadListener;

        getApiService(baseUrl).download(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(getConsumer())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    /**
     * 取消下载
     */
    public void cancelDownload() {
        mDisposables.clear();
    }

    private AUtilsApi getApiService(String baseUrl) {
        OkHttpClient.Builder httpClientBuilder = RetrofitUtils.getOkHttpClientBuilder();
        ProgressHelper.addProgress(httpClientBuilder);

        AUtilsApi api = RetrofitUtils.getRetrofitBuilder(baseUrl, null)
                .client(httpClientBuilder.build())
                .build()
                .create(AUtilsApi.class);

        ProgressHelper.setProgressHandler(new DownloadProgressHandler() {
            @Override
            protected void onProgress(long bytesRead, long contentLength, boolean done) {
                mDownloadListener.onProgress((int) ((100 * bytesRead) / contentLength));
            }
        });
        return api;
    }

    private Consumer<ResponseBody> getConsumer() {
        return new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {

                String sdPath = SDCardUtils.getSDCardPathByEnvironment();
                File file = new File(sdPath, down_file_name);
                FileUtils.createFileByDeleteOldFile(file);
                boolean isComplete = FileIOUtils.writeFileFromIS(file, responseBody.byteStream());
                if (isComplete) mDownloadListener.onSuccess(file);
            }
        };
    }

    private Observer<ResponseBody> getObserver() {
        return new Observer<ResponseBody>() {

            @Override
            public void onSubscribe(Disposable d) {
                mDisposables.add(d);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                // mDownloadListener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                mDownloadListener.onFail(e.getMessage());
                mDisposables.clear();
            }

            @Override
            public void onComplete() {
                mDownloadListener.onComplete();
                mDisposables.clear();
            }
        };
    }
}
