package com.leichao.retrofit.converter;

import com.leichao.retrofit.util.Constant;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

// 转换成File
public final class FileRequestConverter implements Converter<File, RequestBody> {

    private static final MediaType MEDIA_TYPE_FILE = MediaType.parse(Constant.CONTENT_TYPE_PART);

    @Override
    public RequestBody convert(File value) {
        return RequestBody.create(MEDIA_TYPE_FILE, value);
    }

}
