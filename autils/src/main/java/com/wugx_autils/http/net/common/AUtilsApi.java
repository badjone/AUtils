package com.wugx_autils.http.net.common;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author Wugx
 * @date 2018/11/9
 */
public interface AUtilsApi {


    /**
     * 下载文件
     *
     * @param url
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);//直接使用网址下载


    /**
     * 单文件上传 方法一
     *
     * @param partList
     * @return
     */
    @Multipart
    @POST()
    Observable<String> uploadFiles(@Url String url, @Part List<MultipartBody.Part> partList);

    /**
     * 单文件上传 方法二
     *
     * @return
     */
    @Multipart
    @POST(/*"upload/uploadFile.do"*/)
    Observable<String> uploadFiles(@Url String url, @PartMap Map<String, RequestBody> map, @Part MultipartBody.Part file);


//
//    /**
//     * 多文件上传 方法一
//     *
//     * @param description
//     * @param imgs1
//     * @param imgs2
//     * @return
//     */
//    @POST(/*"upload/uploadFile.do"*/)
//    Observable<BasicBean> uploadFiles(@Url String url, @Part("filename") String description,
//                                      @Part("pic\"; filename=\"image1.png") RequestBody imgs1,
//                                      @Part("pic\"; filename=\"image2.png") RequestBody imgs2);
//
//    /**
//     * 多文件上传 方法二
//     *
//     * @param description
//     * @param maps
//     * @return
//     */
//    @POST(/*"upload/uploadFile.do"*/)
//    Observable<BasicBean> uploadFiles(@Url String url,
//                                      @Part("filename") String description,
//                                      @PartMap() Map<String, RequestBody> maps);
//


}
