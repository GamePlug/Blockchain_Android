package com.leichao.retrofit;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.leichao.retrofit.core.HttpApi;
import com.leichao.retrofit.util.DataUtil;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class HttpSimple {

    private static final Map<String, Object> EMPTY_MAP = Collections.emptyMap();

    private HttpApi mHttpApi = HttpManager.create();
    private Method mMethod = Method.GET;
    private String mUrl;
    private Map<String, Object> mParams = new LinkedHashMap<>();// 要上传的参数
    private Map<String, Object> mFileParams = new LinkedHashMap<>();// 要以文件数据格式上传的参数
    private Object mJsonData;// 要以json数据格式上传的对象

    public enum Method {GET, POST}

    private HttpSimple() {
    }

    public static HttpSimple create() {
        return new HttpSimple();
    }

    /**
     * 使用get请求方式，默认请求方式，但上传文件或上传json时会强制使用post
     */
    public HttpSimple get() {
        this.mMethod = Method.GET;
        return this;
    }

    /**
     * 使用post请求方式
     */
    public HttpSimple post() {
        this.mMethod = Method.POST;
        return this;
    }

    /**
     * 请求的url
     */
    public HttpSimple url(String url) {
        this.mUrl = url;
        return this;
    }

    /**
     * 请求的参数
     */
    public HttpSimple param(String key, Object value) {
        this.mParams.put(key, value);
        return this;
    }

    /**
     * 请求的参数集合
     */
    public HttpSimple params(Map<String, Object> params) {
        this.mParams.putAll(params);
        return this;
    }

    /**
     * 请求的文件参数
     */
    public HttpSimple fileParam(String key, Object value) {
        DataUtil.addParams(this.mFileParams, key, value);
        return this;
    }

    /**
     * 请求的文件参数集合
     */
    public HttpSimple fileParams(Map<String, Object> params) {
        this.mFileParams.putAll(DataUtil.formatParams(params));
        return this;
    }

    /**
     * 请求的json数据参数
     */
    public HttpSimple jsonData(Object jsonData) {
        this.mJsonData = jsonData;
        return this;
    }


    /**
     * 执行请求
     */
    public Observable<String> request() {
        return request(null, null);
    }

    /**
     * 执行请求
     *
     * @param owner 与Activity或者Fragment的生命周期绑定，在destroy时结束请求
     */
    public Observable<String> request(LifecycleOwner owner) {
        return request(owner, null);
    }

    /**
     * 执行请求
     *
     * @param owner 与Activity或者Fragment的生命周期绑定
     * @param event 在Activity或者Fragment的生命周期状态event是结束请求
     */
    public Observable<String> request(LifecycleOwner owner, Lifecycle.Event event) {
        Observable<String> observable;
        if (mJsonData != null) {
            observable = mHttpApi.postJson(mUrl, mParams, mJsonData);

        } else if (!mFileParams.isEmpty()) {
            observable = mHttpApi.postFile(mUrl, mParams, mFileParams);

        } else {
            switch (mMethod) {
                case POST:
                    observable = mHttpApi.postNormal(mUrl, EMPTY_MAP, mParams);
                    break;

                case GET:
                default:
                    observable = mHttpApi.getNormal(mUrl, mParams);
                    break;
            }
        }
        return observable.compose(HttpManager.<String>transformer(owner, event));
    }

    /**
     * 执行下载类型请求
     */
    public Observable<ResponseBody> download() {
        return download(null, null);
    }

    /**
     * 执行下载类型请求
     *
     * @param owner 与Activity或者Fragment的生命周期绑定，在destroy时结束请求
     */
    public Observable<ResponseBody> download(LifecycleOwner owner) {
        return download(owner, null);
    }

    /**
     * 执行下载类型请求
     *
     * @param owner 与Activity或者Fragment的生命周期绑定
     * @param event 在Activity或者Fragment的生命周期状态event是结束请求
     */
    public Observable<ResponseBody> download(LifecycleOwner owner, Lifecycle.Event event) {
        Observable<ResponseBody> observable;
        if (mJsonData != null) {
            observable = mHttpApi.postJsonDownload(mUrl, mParams, mJsonData);

        } else if (!mFileParams.isEmpty()) {
            observable = mHttpApi.postFileDownload(mUrl, mParams, mFileParams);

        } else {
            switch (mMethod) {
                case POST:
                    observable = mHttpApi.postNormalDownload(mUrl, EMPTY_MAP, mParams);
                    break;

                case GET:
                default:
                    observable = mHttpApi.getNormalDownload(mUrl, mParams);
                    break;
            }
        }
        return observable.compose(HttpManager.<ResponseBody>transformer(owner, event));
    }

}
