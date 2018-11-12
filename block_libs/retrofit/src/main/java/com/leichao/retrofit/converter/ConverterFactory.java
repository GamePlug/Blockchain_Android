package com.leichao.retrofit.converter;

import com.leichao.retrofit.result.HttpResult;
import com.leichao.retrofit.result.MulaResult;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public final class ConverterFactory extends Converter.Factory {

    public static ConverterFactory create() {
        return new ConverterFactory();
    }

    private ConverterFactory() {

    }

    // 当接口参数是@Body或者@Part时，才会执行此转换
    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        Class clazz = getClassFromType(type);
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
            return new StringRequestConverter<>();

        } else {
            // 如果是其他类型，则转换成json
            return new JsonRequestConverter<>(type);
        }
    }

    // 当接口返回值不是ResponseBody时，才会执行此转换
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            Type type, Annotation[] annotations, Retrofit retrofit) {
        Class clazz = getClassFromType(type);
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
            return new StringResponseConverter();

        } else if (clazz == File.class) {
            // 如果是File类型，则转换成File
            return new FileResponseConverter();

        } else if (clazz == HttpResult.class) {
            // 如果是HttpResult类型，则转换成HttpResult
            return new HttpResponseConverter<>(type);

        } else if (clazz == MulaResult.class) {
            // 如果是MulaResult类型，则转换成MulaResult
            return new MulaResponseConverter<>(type);

        } else {
            // 如果是其他类型，则转换成json
            return new JsonResponseConverter<>(type);
        }
    }

    // 从type中获取class类型
    private Class getClassFromType(Type type) {
        if (type instanceof Class) {
            return (Class) type;

        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class) {
                return  (Class) rawType;
            }
        }
        return null;
    }

}
