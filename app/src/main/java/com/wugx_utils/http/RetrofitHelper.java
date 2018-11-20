package com.wugx_utils.http;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wugx_autils.http.net.common.AUtilsHelper;
import com.wugx_autils.http.net.common.Constants;
import com.wugx_utils.http.converter.GsonConverterFactory;

import retrofit2.Converter;

public class RetrofitHelper {
    private static Api mApi;

    public static Api getApiService() {
        if (mApi == null) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
            //add gson parse factory
            mApi = AUtilsHelper.getApiService(Api.class, Constants.BASE_URL, GsonConverterFactory.create(gson));
        }
        return mApi;
    }

    /**
     * custom parse factory
     *
     * @param factory gson factory
     * @return
     */
    public static Api getApiService(Converter.Factory factory) {
        if (mApi == null)
            mApi = AUtilsHelper.getApiService(Api.class, Constants.BASE_URL, factory);
        return mApi;
    }
}
