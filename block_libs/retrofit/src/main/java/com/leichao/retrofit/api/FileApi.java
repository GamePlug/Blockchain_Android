package com.leichao.retrofit.api;

import java.io.File;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 返回File的接口，注解多了一个 @{@link Streaming}，参数规则同 {@link StringApi}
 * Created by leichao on 2017/3/7.
 */
public interface FileApi {

    @Streaming
    @GET
    Observable<File> getNormal(
            @Url String url,
            @HeaderMap Map<String, Object> headerParams,
            @QueryMap Map<String, Object> queryParams
    );

    @Streaming
    @FormUrlEncoded
    @POST
    Observable<File> postNormal(
            @Url String url,
            @HeaderMap Map<String, Object> headerParams,
            @QueryMap Map<String, Object> queryParams,
            @FieldMap Map<String, Object> fieldParams
    );

    @Streaming
    @Multipart
    @POST
    Observable<File> postFile(
            @Url String url,
            @HeaderMap Map<String, Object> headerParams,
            @QueryMap Map<String, Object> queryParams,
            @PartMap Map<String, Object> partParams
    );

    @Streaming
    @POST
    Observable<File> postJson(
            @Url String url,
            @HeaderMap Map<String, Object> headerParams,
            @QueryMap Map<String, Object> queryParams,
            @Body Object object
    );

}
