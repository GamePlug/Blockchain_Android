package com.leichao.retrofit.converter;

import com.google.gson.Gson;
import com.leichao.retrofit.util.Constant;

import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

// 转换成Json
public final class JsonRequestConverter<T> implements Converter<T, RequestBody> {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse(Constant.CONTENT_TYPE_JSON + Constant.CHARSET_UTF_8);

    private final Type type;

    JsonRequestConverter(Type type) {
        this.type = type;
    }

    @Override
    public RequestBody convert(T value) {
        String jsonStr = "";
        try {
            jsonStr = new Gson().toJson(value, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RequestBody.create(MEDIA_TYPE_JSON, jsonStr);
    }

}
