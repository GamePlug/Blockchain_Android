package com.leichao.retrofit.core;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leichao.retrofit.Http;
import com.leichao.retrofit.api.FileApi;
import com.leichao.retrofit.api.HttpApi;
import com.leichao.retrofit.api.StringApi;
import com.leichao.retrofit.interceptor.ParamsInterceptor;
import com.leichao.retrofit.interceptor.ProgressInterceptor;
import com.leichao.retrofit.loading.BaseLoading;
import com.leichao.retrofit.observer.BaseObserver;
import com.leichao.retrofit.observer.FileObserver;
import com.leichao.retrofit.observer.HttpObserver;
import com.leichao.retrofit.observer.StringObserver;
import com.leichao.retrofit.progress.ProgressListener;
import com.leichao.retrofit.result.HttpResult;
import com.leichao.retrofit.util.Constant;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class HttpSimple {

    private static final Map<String, Object> EMPTY_MAP = Collections.emptyMap();

    private String mMethod = Constant.GET;
    private String mUrl;
    private final Map<String, Object> mHeaders = new LinkedHashMap<>();// 要上传的header
    private final Map<String, Object> mParams = new LinkedHashMap<>();// 要上传的参数
    private boolean mContainFile = false;// 要上传的参数是否包含文件
    private Object mBody;// 要以Body数据格式上传的对象
    private LifecycleOwner mLifeOwner;// 用于生命周期绑定
    private Lifecycle.Event mLifeEvent;// 用于生命周期绑定
    private BaseLoading mLoading;// 加载loading
    private long mTimeout;// 超时时间
    private String mBaseUrl;// BaseUrl
    private ParamsInterceptor mParamsInterceptor;// 参数拦截器
    private ProgressListener mUpListener;// 上传进度监听
    private ProgressListener mDownListener;// 下载进度监听

    private HttpSimple(String url) {
        this.mUrl = url;
        HttpConfig config = Http.config();
        this.mTimeout = config.getTimeout();
        this.mBaseUrl = config.getBaseUrl();
        this.mParamsInterceptor = config.getParamsInterceptor();
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
        this.mMethod = Constant.GET;
        return this;
    }

    /**
     * 使用post请求方式
     */
    public HttpSimple post() {
        this.mMethod = Constant.POST;
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
     * 请求的参数，参数包含File时代表上传文件
     */
    public HttpSimple param(String key, Object value) {
        if (value instanceof File) {
            mContainFile = true;
            key += "\"; filename=\"" + ((File) value).getName();
        }
        this.mParams.put(key, value);
        return this;
    }

    /**
     * 请求的参数集合，参数包含File时代表上传文件
     */
    public HttpSimple params(Map<String, Object> params) {
        for (String key : params.keySet()) {
            Object value = params.get(key);
            param(key, value);
        }
        return this;
    }

    /**
     * 将数据直接写入Body请求体中，此时Body请求体中不再写入其他参数，其他参数会以Query方式上传
     */
    public HttpSimple body(Object body) {
        this.mBody = body;
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
     * 显示加载loading
     */
    public HttpSimple loading(Context context) {
        return loading(context, null);
    }

    /**
     * 显示加载loading
     */
    public HttpSimple loading(Context context, String message) {
        return loading(context, message, true);
    }

    /**
     * 显示加载loading
     */
    public HttpSimple loading(Context context, String message, boolean cancelable) {
        return loading(Http.config().getLoadingCallback().newLoading(context, message, cancelable));
    }

    /**
     * 显示加载loading
     */
    public HttpSimple loading(BaseLoading loading) {
        this.mLoading = loading;
        return this;
    }

    /**
     * 设置超时时间，单位秒
     */
    public HttpSimple timeout(long timeout) {
        this.mTimeout = timeout;
        return this;
    }

    /**
     * 设置baseUrl
     */
    public HttpSimple baseUrl(String baseUrl) {
        this.mBaseUrl = baseUrl;
        return this;
    }

    /**
     * 参数拦截器
     */
    public HttpSimple paramsInterceptor(ParamsInterceptor interceptor) {
        this.mParamsInterceptor = interceptor;
        return this;
    }

    /**
     * 上传进度监听,POST请求才生效
     *
     * @param listener 进度监听器
     */
    public HttpSimple upListener(ProgressListener listener) {
        this.mUpListener = listener;
        return this;
    }

    /**
     * 下载进度监听
     *
     * @param listener 进度监听器
     */
    public HttpSimple downListener(ProgressListener listener) {
        this.mDownListener = listener;
        return this;
    }

    /**
     * 执行获取String的请求
     */
    public void subscribe(StringObserver observer) {
        StringApi api = Http.create(StringApi.class, getHttpClient());
        Observable<String> observable;
        if (mBody != null) {
            observable = api.postBody(mUrl, mHeaders, mParams, mBody);

        } else if (mContainFile) {
            observable = api.postPart(mUrl, mHeaders, EMPTY_MAP, mParams);

        } else {
            switch (mMethod) {
                case Constant.POST:
                    observable = api.postNormal(mUrl, mHeaders, EMPTY_MAP, mParams);
                    break;

                case Constant.GET:
                default:
                    observable = api.getNormal(mUrl, mHeaders, mParams);
                    break;
            }
        }
        subscribe(observable, observer);
    }

    /**
     * 执行获取File的请求
     */
    public void subscribe(FileObserver observer) {
        FileApi api = Http.create(FileApi.class, getHttpClient());
        Observable<File> observable;
        if (mBody != null) {
            observable = api.postBody(mUrl, mHeaders, mParams, mBody);

        } else if (mContainFile) {
            observable = api.postPart(mUrl, mHeaders, EMPTY_MAP, mParams);

        } else {
            switch (mMethod) {
                case Constant.POST:
                    observable = api.postNormal(mUrl, mHeaders, EMPTY_MAP, mParams);
                    break;

                case Constant.GET:
                default:
                    observable = api.getNormal(mUrl, mHeaders, mParams);
                    break;
            }
        }
        subscribe(observable, observer);
    }

    /**
     * 执行获取HttpResult的请求
     */
    public <T> void subscribe(final HttpObserver<T> observer) {
        HttpApi api = Http.create(HttpApi.class, getHttpClient());
        Observable<HttpResult> observable;
        if (mBody != null) {
            observable = api.postBody(mUrl, mHeaders, mParams, mBody);

        } else if (mContainFile) {
            observable = api.postPart(mUrl, mHeaders, EMPTY_MAP, mParams);

        } else {
            switch (mMethod) {
                case Constant.POST:
                    observable = api.postNormal(mUrl, mHeaders, EMPTY_MAP, mParams);
                    break;

                case Constant.GET:
                default:
                    observable = api.getNormal(mUrl, mHeaders, mParams);
                    break;
            }
        }
        subscribe(observable.map(new Function<HttpResult, HttpResult<T>>() {
            @Override
            public HttpResult<T> apply(HttpResult httpResult) throws Exception {
                Type type = TypeToken.get(HttpResult.class).getType();
                Class clazz = observer.getClass();
                while (clazz != null) {
                    if (clazz.getSuperclass() == HttpObserver.class) {
                        Type paramType = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
                        type = TypeToken.getParameterized(HttpResult.class, paramType).getType();
                        break;
                    }
                    clazz = clazz.getSuperclass();
                }
                return new Gson().fromJson(httpResult.getJsonStr(), type);
            }
        }), observer);
    }

    // 获取HttpClient
    private HttpClient getHttpClient() {
        return HttpClient.builder()
                .timeout(mTimeout)
                .baseUrl(mBaseUrl)
                .addInterceptor(mParamsInterceptor)
                .addInterceptor(new ProgressInterceptor(mUpListener, mDownListener));
    }

    // 执行请求订阅
    private <T> void subscribe(Observable<T> observable, BaseObserver<T> observer) {
        if (mLoading != null) {
            observer.loading(mLoading);
        }
        observable.compose(Http.<T>composeThread())
                .compose(Http.<T>composeLifecycle(mLifeOwner, mLifeEvent))
                .subscribe(observer);
    }

}
