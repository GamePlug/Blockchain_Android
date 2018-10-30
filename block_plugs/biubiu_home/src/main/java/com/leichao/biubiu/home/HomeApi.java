package com.leichao.biubiu.home;

import com.leichao.retrofit.result.MulaResult;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * api接口
 * Created by leichao on 2017/3/7.
 */

public interface HomeApi {

    // 测试用接口
    @GET("api/tms/googleKey/getGoogleKey?isVerify=0")
    Observable<MulaResult<String>> test();

}
