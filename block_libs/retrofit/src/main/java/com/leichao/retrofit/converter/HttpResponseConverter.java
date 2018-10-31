package com.leichao.retrofit.converter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.leichao.retrofit.result.HttpResult;
import com.leichao.retrofit.util.LogUtil;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

// 转换成HttpResult对象
public final class HttpResponseConverter<T> implements Converter<ResponseBody, HttpResult<T>> {

    private final Type type;

    HttpResponseConverter(Type type) {
        this.type = type;
    }

    @Override
    public HttpResult<T> convert(ResponseBody value) throws IOException {
        String responseStr = value.string();
        LogUtil.logE("result:" + responseStr);
        HttpResult<T> httpResult;
        try {
            JsonObject jsonObject = new JsonParser().parse(responseStr).getAsJsonObject();
            String code = jsonObject.get("code").getAsString();
            if ("success".equals(code) || "00".equals(code)) {
                httpResult = new Gson().fromJson(responseStr, type);
            } else {
                httpResult = new HttpResult<>();
                httpResult.setType(false);
                httpResult.setCode(code);
                httpResult.setMessage(jsonObject.get("message").getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpResult = new HttpResult<>();
            httpResult.setStatus(HttpResult.Status.ERROR_JSON);
            httpResult.setMessage("json_error");
        }
        httpResult.setJsonStr(responseStr);
        return httpResult;
    }

}
