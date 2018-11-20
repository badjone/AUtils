package com.wugx_autils.http.net.download;

import java.io.File;

/**
 * Created by zhpan on 2018/3/21.
 */

public interface DownloadListener {
    void onProgress(int progress);

    void onSuccess(File file);

    void onFail(String message);

    void onComplete();
}
