package com.wugx_autils.http.net.common;

import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by zhpan on 2017/4/1.
 */

public class AUtilsHelper {

    public static <T> T getApiService(Class<T> cls, String baseUrl, Converter.Factory factory) {
        Retrofit retrofit = RetrofitUtils.getRetrofitBuilder(baseUrl, factory).build();
        return retrofit.create(cls);
    }

}
