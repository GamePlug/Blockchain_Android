package com.leichao.retrofit.converter;

import com.leichao.retrofit.core.Util;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

// 转换成String对象
public final class StringResponseConverter implements Converter<ResponseBody, String> {

    @Override
    public String convert(ResponseBody value) throws IOException {
        String responseStr = value.string();
        Util.log("result:" + responseStr);
        return responseStr;
    }

}
