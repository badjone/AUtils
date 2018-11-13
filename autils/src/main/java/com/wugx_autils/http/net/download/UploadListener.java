package com.wugx_autils.http.net.download;

import okhttp3.ResponseBody;

/**
 * @author Wugx
 * @date 2018/11/10
 */
public interface UploadListener {

    void onProgress(int progress);

    void onSuccess(ResponseBody responseBody);

    void onFail(String message);

    void onComplete();
}
