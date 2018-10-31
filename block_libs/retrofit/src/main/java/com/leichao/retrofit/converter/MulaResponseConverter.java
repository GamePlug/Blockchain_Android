package com.leichao.retrofit.converter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.leichao.retrofit.result.MulaResult;
import com.leichao.retrofit.util.LogUtil;

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
        LogUtil.logE("result:" + responseStr);
        MulaResult<T> result;
        try {
            JsonObject jsonObject = new JsonParser().parse(responseStr).getAsJsonObject();
            String code = jsonObject.get("code").getAsString();
            if ("success".equals(code) || "00".equals(code)) {
                result = new Gson().fromJson(responseStr, type);
            } else {
                result = new MulaResult<>();
                result.setType(false);
                result.setCode(code);
                result.setMessage(jsonObject.get("message").getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = new MulaResult<>();
            result.setStatus(MulaResult.Status.ERROR_JSON);
            result.setMessage("json_error");
        }
        result.setJsonStr(responseStr);
        return result;
    }

}
