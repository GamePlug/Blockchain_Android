package com.leichao.retrofit.converter;

import com.google.gson.Gson;
import com.leichao.retrofit.core.Util;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

// 转换成Json对象
public final class JsonResponseConverter<T> implements Converter<ResponseBody, T> {

    private final Type type;

    JsonResponseConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String responseStr = value.string();
        Util.log("result:" + responseStr);
        T jsonResult = null;
        try {
            jsonResult = new Gson().fromJson(responseStr, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

}
