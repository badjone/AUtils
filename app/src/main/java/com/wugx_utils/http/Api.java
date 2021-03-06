package com.wugx_utils.http;

import com.wugx_utils.entity.BasicBean;
import com.wugx_utils.entity.LoginBean;
import com.wugx_utils.entity.UserInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
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
public interface Api {


    /**
     * 登录
     *
     * @param cellPhoneNum 手机号
     * @param password     手机验证码
     * @return
     */
    @POST()
    @FormUrlEncoded
    Observable<LoginBean> toLogin(@Url String url, @FieldMap Map<String, String> map);


    /**
     * 获取用户信息
     *
     * @param token
     * @param memberId
     * @param deviceId
     * @return
     */
    @POST("userMember/memberList.do")
    @FormUrlEncoded
    Observable<UserInfo> getUserMember(@Field("token") String token,
                                       @Field("memberId") String memberId,
                                       @Field("deviceId") String deviceId);


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
    Observable<BasicBean> uploadFiles(@Url String url, @Part List<MultipartBody.Part> partList);

    /**
     * 单文件上传 方法二
     *
     * @return
     */
    @Multipart
    @POST(/*"upload/uploadFile.do"*/)
    Observable<BasicBean> uploadFiles(@Url String url, @PartMap Map<String, RequestBody> map, @Part MultipartBody.Part image);


    /**
     * 多文件上传 方法一
     *
     * @param description
     * @param imgs1
     * @param imgs2
     * @return
     */
    @POST(/*"upload/uploadFile.do"*/)
    Observable<BasicBean> uploadFiles(@Url String url, @Part("filename") String description,
                                      @Part("pic\"; filename=\"image1.png") RequestBody imgs1,
                                      @Part("pic\"; filename=\"image2.png") RequestBody imgs2);

    /**
     * 多文件上传 方法二
     *
     * @param description
     * @param maps
     * @return
     */
    @POST(/*"upload/uploadFile.do"*/)
    Observable<BasicBean> uploadFiles(@Url String url,
                                      @Part("filename") String description,
                                      @PartMap() Map<String, RequestBody> maps);


    @Multipart
    @POST()
    Observable<BasicBean> uploadImg(@Url String url, @PartMap Map<String, RequestBody> map, @Part() MultipartBody.Part file);

}
