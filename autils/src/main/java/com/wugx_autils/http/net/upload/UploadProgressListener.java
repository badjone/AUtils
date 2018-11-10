package com.wugx_autils.http.net.upload;

/**
 * @author wugx
 * @data 2018/6/8.
 */

public interface UploadProgressListener {
    /**
     * 上传进度
     * @param currentBytesCount
     * @param totalBytesCount
     */
    void onProgress(long currentBytesCount, long totalBytesCount);
}
