package com.leichao.retrofit;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.leichao.retrofit.api.FileApi;
import com.leichao.retrofit.api.StringApi;
import com.leichao.retrofit.progress.ProgressListener;
import com.leichao.retrofit.util.DataUtil;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class HttpSimple {

    private static final Map<String, Object> EMPTY_MAP = Collections.emptyMap();

    private Method mMethod = Method.GET;
    private String mUrl;
    private Map<String, Object> mHeaders = new LinkedHashMap<>();// 要上传的header
    private Map<String, Object> mParams = new LinkedHashMap<>();// 要上传的参数
    private Map<String, Object> mFileParams = new LinkedHashMap<>();// 要以文件数据格式上传的参数
    private Object mJsonData;// 要以json数据格式上传的对象
    private LifecycleOwner mLifeOwner;
    private Lifecycle.Event mLifeEvent;
    private ProgressListener mListener;

    public enum Method {GET, POST}

    private HttpSimple(String url) {
        this.mUrl = url;
    }

    /**
     * 创建请求工具
     *
     * @param url 请求的url
     */
    public static HttpSimple create(String url) {
        return new HttpSimple(url);
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
     * 请求的header
     */
    public HttpSimple header(String key, Object value) {
        this.mHeaders.put(key, value);
        return this;
    }

    /**
     * 请求的header集合
     */
    public HttpSimple headers(Map<String, Object> params) {
        this.mHeaders.putAll(params);
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
     * 绑定生命周期
     *
     * @param owner 与Activity或者Fragment的生命周期绑定，在destroy时结束请求
     */
    public HttpSimple bindLifecycle(LifecycleOwner owner) {
        this.mLifeOwner = owner;
        return this;
    }

    /**
     * 绑定生命周期
     *
     * @param owner 与Activity或者Fragment的生命周期绑定
     * @param event 在Activity或者Fragment的生命周期状态event时结束请求
     */
    public HttpSimple bindLifecycle(LifecycleOwner owner, Lifecycle.Event event) {
        this.mLifeOwner = owner;
        this.mLifeEvent = event;
        return this;
    }

    /**
     * 下载进度监听
     *
     * @param listener 进度监听器
     */
    public HttpSimple progress(ProgressListener listener) {
        this.mListener = listener;
        return this;
    }

    /**
     * 执行获取String的请求
     */
    public void getString(Observer<String> observer) {
        getString().compose(HttpManager.<String>composeThread())
                .subscribe(observer);
    }

    /**
     * 执行获取String的请求
     */
    public Observable<String> getString() {
        StringApi api = HttpManager.create(StringApi.class, mListener);
        Observable<String> observable;
        if (mJsonData != null) {
            observable = api.postJson(mUrl, mHeaders, mParams, mJsonData);

        } else if (!mFileParams.isEmpty()) {
            observable = api.postFile(mUrl, mHeaders, mParams, mFileParams);

        } else {
            switch (mMethod) {
                case POST:
                    observable = api.postNormal(mUrl, mHeaders, EMPTY_MAP, mParams);
                    break;

                case GET:
                default:
                    observable = api.getNormal(mUrl, mHeaders, mParams);
                    break;
            }
        }
        return observable.compose(HttpManager.<String>composeLifecycle(mLifeOwner, mLifeEvent));
    }

    /**
     * 执行获取File的请求
     */
    public void getFile(Observer<File> observer) {
        getFile().compose(HttpManager.<File>composeThread())
                .subscribe(observer);
    }

    /**
     * 执行获取File的请求
     */
    public Observable<File> getFile() {
        FileApi api = HttpManager.create(FileApi.class, mListener);
        Observable<File> observable;
        if (mJsonData != null) {
            observable = api.postJson(mUrl, mHeaders, mParams, mJsonData);

        } else if (!mFileParams.isEmpty()) {
            observable = api.postFile(mUrl, mHeaders, mParams, mFileParams);

        } else {
            switch (mMethod) {
                case POST:
                    observable = api.postNormal(mUrl, mHeaders, EMPTY_MAP, mParams);
                    break;

                case GET:
                default:
                    observable = api.getNormal(mUrl, mHeaders, mParams);
                    break;
            }
        }
        return observable.compose(HttpManager.<File>composeLifecycle(mLifeOwner, mLifeEvent));
    }

}
