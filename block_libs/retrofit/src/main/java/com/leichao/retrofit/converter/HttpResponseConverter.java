package com.leichao.retrofit.converter;

import com.google.gson.Gson;
import com.leichao.retrofit.core.Util;
import com.leichao.retrofit.result.HttpResult;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

// 转换成HttpResult对象
public final class HttpResponseConverter<T> implements Converter<ResponseBody, HttpResult<T>> {

    private final Type type;

    HttpResponseConverter(Type type) {
        this.type = type;
    }

    @Override
    public HttpResult<T> convert(ResponseBody value) throws IOException {
        String responseStr = value.string();
        Util.log("result:" + responseStr);
        HttpResult<T> httpResult;
        Gson gson = new Gson();
        HttpResult temp = gson.fromJson(responseStr, HttpResult.class);
        if ("success".equals(temp.getCode())) {
            httpResult = gson.fromJson(responseStr, type);
        } else {
            httpResult = new HttpResult<>();
            httpResult.setCode(temp.getCode());
            httpResult.setMessage(temp.getMessage());
        }
        httpResult.setJsonStr(responseStr);
        return httpResult;
    }

}
