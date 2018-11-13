//package com.wugx_autils.http.net.common;
//
//
//import retrofit2.Converter;
//
//public class RetrofitHelper {
//    private static Api mApi;
//
//    public static Api getApiService() {
//        if (mApi == null)
//            mApi = AUtilsApi.getApiService(Api.class, Constants.BASE_URL);
//        return mApi;
//    }
//
//    public static Api getApiService(Converter.Factory factory) {
//        if (mApi == null)
//            mApi = AUtilsApi.getApiService(Api.class, Constants.BASE_URL, factory);
//        return mApi;
//    }
//}
