package com.leichao.retrofit.converter;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

// 转换成String
public final class StringRequestConverter<T> implements Converter<T, RequestBody> {

    private static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain; charset=UTF-8");

    @Override
    public RequestBody convert(T value) {
        return RequestBody.create(MEDIA_TYPE_TEXT, String.valueOf(value));
    }

}
