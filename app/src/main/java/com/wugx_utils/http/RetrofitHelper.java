package com.wugx_utils.http;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wugx_autils.http.net.common.AUtilsHelper;
import com.wugx_autils.http.net.common.Constants;
import com.wugx_utils.http.converter.GsonConverterFactory;

public class RetrofitHelper {
    private static Api mApi;

    public static Api getApiService() {
        if (mApi == null) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
            mApi = AUtilsHelper.getApiService(Api.class, Constants.BASE_URL, GsonConverterFactory.create(gson));
        }
        return mApi;
    }
}
