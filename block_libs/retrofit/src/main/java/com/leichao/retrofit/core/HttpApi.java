package com.leichao.retrofit.core;

import com.leichao.retrofit.result.MulaResult;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 通用api接口
 * Created by leichao on 2017/3/7.
 */

public interface HttpApi {

    /**
     * 普通get请求，此时Field参数会以键值对的形式写在body中
     * 注:
     * 1.参数只能使用@Path，@Query，@QueryMap注解
     * 2.此时必须为get请求，即使用@GET注解
     */
    @GET
    Observable<String> getNormal(
            @Url String url,
            @QueryMap Map<String, Object> queryParams
    );

    /**
     * 下载文件get请求
     * 参数规则同getNormal
     */
    @Streaming
    @GET
    Observable<ResponseBody> getNormalDownload(
            @Url String url,
            @QueryMap Map<String, Object> queryParams
    );

    /**
     * 普通post请求，此时Field类型的参数会以键值对的形式写在body中
     * 注:
     * 1.参数注解只能使用@Path，@Query，@QueryMap，@Field，@FieldMap
     * 2.此时必须为post请求，即使用@POST注解，且必须使用@FormUrlEncoded注解
     */
    @FormUrlEncoded
    @POST
    Observable<String> postNormal(
            @Url String url,
            @QueryMap Map<String, Object> queryParams,
            @FieldMap Map<String, Object> fieldParams
    );

    /**
     * 下载文件post请求
     * 参数规则同postNormal
     */
    @Streaming
    @FormUrlEncoded
    @POST
    Observable<ResponseBody> postNormalDownload(
            @Url String url,
            @QueryMap Map<String, Object> queryParams,
            @FieldMap Map<String, Object> fieldParams
    );

    /**
     * 上传表单数据方式，比如上传多文件,图片,参数等
     * 注:
     * 1.参数只能使用@Path，@Query，@QueryMap，@Part，@PartMap注解
     * 2.此时必须为post请求，即使用@POST注解，且必须使用@Multipart注解
     */
    @Multipart
    @POST("api/travel/base/ocrCard")
    Observable<String> postFile(
            @Url String url,
            @QueryMap Map<String, Object> queryParams,
            @PartMap Map<String, Object> partParams// 此处的Map中的Object可以为RequestBody
    );

    /**
     * 下载文件post请求
     * 参数规则同postFile
     */
    @Streaming
    @Multipart
    @POST("api/travel/base/ocrCard")
    Observable<ResponseBody> postFileDownload(
            @Url String url,
            @QueryMap Map<String, Object> queryParams,
            @PartMap Map<String, Object> partParams// 此处的Map中的Object可以为RequestBody
    );

    /**
     * 上传纯Json数据，是以json的格式写入body中，而不是键值对，使用@Body注解标记要上传的json实体类即可
     * 注：
     * 1.参数只能使用@Path，@Query，@QueryMap，@Body注解，其中最多只能有一个@Body
     * 2.此时必须为post请求，即使用@POST注解，且不能使用@FormUrlEncoded和@Multipart注解
     */
    @POST
    Observable<String> postJson(
            @Url String url,
            @QueryMap Map<String, Object> queryParams,
            @Body Object object
    );

    /**
     * 下载文件post请求
     * 参数规则同postJson
     */
    @Streaming
    @POST
    Observable<ResponseBody> postJsonDownload(
            @Url String url,
            @QueryMap Map<String, Object> queryParams,
            @Body Object object
    );

    // 测试用接口
    @GET("api/tms/googleKey/getGoogleKey?isVerify=0")
    Observable<MulaResult<String>> test();

}
