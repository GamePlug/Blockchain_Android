package com.leichao.retrofit.converter;

import com.leichao.retrofit.util.Constant;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

// 转换成String
public final class StringRequestConverter implements Converter<String, RequestBody> {

    private static final MediaType MEDIA_TYPE_TEXT = MediaType.parse(Constant.CONTENT_TYPE_TEXT + Constant.CHARSET_UTF_8);

    @Override
    public RequestBody convert(String value) {
        return RequestBody.create(MEDIA_TYPE_TEXT, value);
    }

}
