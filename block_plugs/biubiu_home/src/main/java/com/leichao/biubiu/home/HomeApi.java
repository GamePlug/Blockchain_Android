package com.leichao.biubiu.home;

import com.leichao.retrofit.result.HttpResult;
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

    // 测试用接口
    @GET("http://47.74.159.3:8083/api/tms/tmsMessages/messageList?page=1&userType=2&userId=307ad3da76f24a4aac903c317653f71a&isVerify=0")
    Observable<HttpResult<TestBean>> test2();

}
