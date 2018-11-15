package com.leichao.retrofit.converter;

import com.google.gson.Gson;
import com.leichao.retrofit.core.Util;
import com.leichao.retrofit.result.MulaResult;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

// 转换成MulaResult对象
public final class MulaResponseConverter<T> implements Converter<ResponseBody, MulaResult<T>> {

    private final Type type;

    MulaResponseConverter(Type type) {
        this.type = type;
    }

    @Override
    public MulaResult<T> convert(ResponseBody value) throws IOException {
        String responseStr = value.string();
        Util.log("result:" + responseStr);
        MulaResult<T> mulaResult;
        Gson gson = new Gson();
        MulaResult temp = gson.fromJson(responseStr, MulaResult.class);
        if ("success".equals(temp.getCode())) {
            mulaResult = gson.fromJson(responseStr, type);
        } else {
            mulaResult = new MulaResult<>();
            mulaResult.setCode(temp.getCode());
            mulaResult.setMessage(temp.getMessage());
        }
        mulaResult.setJsonStr(responseStr);
        return mulaResult;
    }

}
