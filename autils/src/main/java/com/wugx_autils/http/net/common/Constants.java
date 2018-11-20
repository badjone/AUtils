package com.wugx_autils.http.net.common;

import com.wugx_autils.Autils;

/**
 * @author Wugx
 * @date 2018/11/9
 */
public interface Constants {

    int DEFAULT_TIMEOUT = 60 * 1000;

    String CODE_SUCCESS = "success";
    String CODE_ERROR = "error";
    String BASE_URL = Autils.getInstance().baseUrl;

//    String DOWNLOAD_URL = "http://www.oitsme.com/download/oitsme.apk";

}
