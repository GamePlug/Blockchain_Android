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

    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        // 当接口参数是@Body或者@Part时，才会执行此转换
        if (type == String.class
                || type == boolean.class || type == Boolean.class
                || type == byte.class || type == Byte.class
                || type == char.class || type == Character.class
                || type == double.class || type == Double.class
                || type == float.class || type == Float.class
                || type == int.class || type == Integer.class
                || type == long.class || type == Long.class
                || type == short.class || type == Short.class) {
            // 如果是String或者基本类型，则转换成文本
            return new StringRequestConverter<>();

        }
        // 如果是其他对象，则转换成json对象
        return new JsonRequestConverter<>(type);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            Type type, Annotation[] annotations, Retrofit retrofit) {
        // 当接口返回值不是ResponseBody时，才会执行此转换
        if (type == String.class
                || type == boolean.class || type == Boolean.class
                || type == byte.class || type == Byte.class
                || type == char.class || type == Character.class
                || type == double.class || type == Double.class
                || type == float.class || type == Float.class
                || type == int.class || type == Integer.class
                || type == long.class || type == Long.class
                || type == short.class || type == Short.class) {
            // 如果type是String或者基本类型，则转换成String
            return new StringResponseConverter();

        } else if (type == File.class) {
            // 如果type是File类型，则转换成File
            return new FileResponseConverter();

        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType == HttpResult.class) {
                // 如果type是HttpResult<T>类型，则转换成HttpResult<T>
                return new HttpResponseConverter<>(type);

            } else if (rawType == MulaResult.class) {
                // 如果type是MulaResult<T>类型，则转换成MulaResult<T>
                return new MulaResponseConverter<>(type);

            }
        }
        // 如果是其他对象，则转换成json对象
        return new JsonResponseConverter<>(type);
    }

}
