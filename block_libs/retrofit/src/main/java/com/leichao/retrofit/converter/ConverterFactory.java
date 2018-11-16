package com.leichao.retrofit.converter;

import com.leichao.retrofit.core.Util;
import com.leichao.retrofit.result.HttpResult;
import com.leichao.retrofit.result.MulaResult;

import java.io.File;
import java.lang.annotation.Annotation;
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

    // 当接口参数不是RequestBody，且参数注解是@Body或者@Part或者@PartMap时，才会执行此转换
    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new Converter<Object, RequestBody>() {
            @Override
            public RequestBody convert(Object value) {
                return Util.requestBody(value);
            }
        };
    }

    // 当接口返回值不是ResponseBody时，才会执行此转换
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            Type type, Annotation[] annotations, Retrofit retrofit) {
        Class clazz = Util.getClassFromType(type);
        if (Util.isStringOrBase(clazz)) {
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

}
