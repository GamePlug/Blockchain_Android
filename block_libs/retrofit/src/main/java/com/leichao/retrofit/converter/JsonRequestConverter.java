package com.leichao.retrofit.converter;

import com.google.gson.Gson;
import com.leichao.retrofit.util.Constant;

import java.io.File;
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
        // ConverterFactory中type的class可能在接口中被定义为Object，此处还要判断value真正对应的类型
        Class clazz = value.getClass();
        if (clazz == String.class
                || clazz == boolean.class || clazz == Boolean.class
                || clazz == byte.class || clazz == Byte.class
                || clazz == char.class || clazz == Character.class
                || clazz == double.class || clazz == Double.class
                || clazz == float.class || clazz == Float.class
                || clazz == int.class || clazz == Integer.class
                || clazz == long.class || clazz == Long.class
                || clazz == short.class || clazz == Short.class) {
            // 如果是String或者基本类型，则转换成String
            return new StringRequestConverter().convert((String) value);

        } else if (clazz == File.class) {
            // 如果是File类型，则转换成File
            return new FileRequestConverter().convert((File) value);

        } else {
            // 如果是其他类型，则转换成json
            String jsonStr = "";
            try {
                jsonStr = new Gson().toJson(value, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return RequestBody.create(MEDIA_TYPE_JSON, jsonStr);
        }
    }

}
