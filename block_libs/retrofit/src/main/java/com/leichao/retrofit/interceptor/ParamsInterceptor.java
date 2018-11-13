package com.leichao.retrofit.interceptor;

import android.text.TextUtils;

import com.leichao.retrofit.util.LogUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 基础参数拦截器
 * Created by leichao on 2017/3/3.
 */
public abstract class ParamsInterceptor implements Interceptor {

    private static final String APPLICATION_FORM_URL = "application/x-www-form-urlencoded;charset=UTF-8";
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static final String APPLICATION_JSON = "application/json";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        switch (request.method()) {
            case "GET":
                request = getNormal(request);
                break;
            case "POST":
                RequestBody body = request.body();
                MediaType contentType = body != null ? body.contentType() : null;
                if (body != null && contentType != null && contentType.toString().contains(MULTIPART_FORM_DATA)) {
                    request = postFile(request);
                } else if (body != null && contentType != null && contentType.toString().contains(APPLICATION_JSON)) {
                    request = postJson(request);
                } else {
                    request = postNormal(request);
                }
                break;
        }
        return chain.proceed(request);
    }

    /**
     * 普通get请求统一处理的操作
     */
    private Request getNormal(Request request) {
        // 获取公共参数
        String originUrl = request.url().toString();
        Map<String, String> params = getCommonParams(originUrl);
        // 设置公共参数
        HttpUrl.Builder builder = request.url().newBuilder();
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (!TextUtils.isEmpty(value)) {
                builder.addQueryParameter(key, value);
            }
        }
        HttpUrl url = builder.build();
        // 生成新的Request
        request = request.newBuilder()
                .url(url)
                .build();
        LogUtil.logE("url:" + request.url());
        return request;
    }

    /**
     * 普通post请求统一处理的操作
     */
    private Request postNormal(Request request) {
        String postBodyString = bodyToString(request.body());
        // 获取公共参数
        String originUrl = getAppendUrl(request, postBodyString);
        Map<String, String> params = getCommonParams(originUrl);
        // 设置公共参数
        StringBuilder sb = new StringBuilder(postBodyString);
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (!TextUtils.isEmpty(value)) {
                sb.append(sb.length() > 0 ? "&" : "").append(key).append("=").append(value);
            }
        }
        postBodyString = sb.toString();
        // 生成新的Request
        request = request.newBuilder()
                .post(RequestBody.create(
                        MediaType.parse(APPLICATION_FORM_URL),
                        postBodyString))
                .build();
        LogUtil.logE("url:" + getAppendUrl(request, postBodyString));
        return request;
    }

    /**
     * 上传文件数据(表单请求)统一处理的操作
     */
    private Request postFile(Request request) {
        RequestBody body = request.body();
        if (body == null || !(body instanceof MultipartBody)) {
            return request;
        }
        List<MultipartBody.Part> parts = ((MultipartBody) body).parts();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (MultipartBody.Part part : parts) {
            builder.addPart(part);
        }
        String uploadBodyString = partsToString(parts);
        // 获取公共参数
        String originUrl = getAppendUrl(request, uploadBodyString);// 不包含文件数据
        Map<String, String> params = getCommonParams(originUrl);
        // 设置公共参数
        StringBuilder sb = new StringBuilder(uploadBodyString);
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (!TextUtils.isEmpty(value)) {
                builder.addFormDataPart(key, value);
                sb.append(sb.length() > 0 ? "&" : "").append(key).append("=").append(value);
            }
        }
        MultipartBody multiBody = builder.build();
        uploadBodyString = sb.toString();
        // 生成新的Request
        request = request.newBuilder()
                .post(multiBody)
                .build();
        LogUtil.logE("url:" + getAppendUrl(request, uploadBodyString));
        return request;
    }

    /**
     * 上传json数据请求统一处理的操作
     */
    private Request postJson(Request request) {
        // 获取公共参数
        String originUrl = request.url().toString();// 不包含json数据
        Map<String, String> params = getCommonParams(originUrl);
        // 设置公共参数
        HttpUrl.Builder builder = request.url().newBuilder();
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (!TextUtils.isEmpty(value)) {
                builder.addQueryParameter(key, value);
            }
        }
        HttpUrl url = builder.build();
        // 生成新的Request
        request = request.newBuilder()
                .url(url)
                .build();
        String postBodyString = bodyToString(request.body());
        LogUtil.logE("url:" + getAppendUrl(request, postBodyString));
        return request;
    }

    /**
     * 获取拼接好的Url
     */
    private String getAppendUrl(Request request, String append) {
        String url = request.url().toString();
        url += url.contains("?") ? "&" : "?";
        url += append;
        return url;
    }

    /**
     * 将RequestBody转换成对应的字符串
     */
    private String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            if (request != null) {
                request.writeTo(buffer);
                return buffer.readUtf8();
            } else {
                return "";
            }
        } catch (final IOException e) {
            return "";
        }
    }

    /**
     * 将List<MultipartBody.Part>转换成对应的字符串
     */
    private String partsToString(List<MultipartBody.Part> parts) {
        StringBuilder partBuilder = new StringBuilder();
        for (MultipartBody.Part part : parts) {
            Headers headers = part.headers();
            if (headers != null) {
                String cd = headers.get("Content-Disposition");
                if (!TextUtils.isEmpty(cd) && !cd.contains("filename")) {// 只获取非文件类型的字段
                    String key = cd.split("\"").length >= 2 ? cd.split("\"")[1] : "";
                    String value = bodyToString(part.body());
                    partBuilder.append(partBuilder.length() == 0 ? "" : "&").append(key).append("=").append(value);
                }
            }
        }
        return partBuilder.toString();
    }

    public abstract Map<String, String> getCommonParams(String url);

}
