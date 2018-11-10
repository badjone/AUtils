package com.wugx_autils.http.net.common;


public class RetrofitHelper {
    private static Api mApi;

    public static Api getApiService() {
        if (mApi == null)
            mApi = IdeaApi.getApiService(Api.class, Constants.BASE_URL);
        return mApi;
    }
}
