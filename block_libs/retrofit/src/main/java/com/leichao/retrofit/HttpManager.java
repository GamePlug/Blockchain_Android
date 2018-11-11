package com.leichao.retrofit;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.leichao.retrofit.core.HttpClient;
import com.leichao.retrofit.core.HttpConfig;
import com.leichao.retrofit.core.HttpSimple;
import com.leichao.retrofit.progress.ProgressListener;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class HttpManager {

    private static HttpConfig mConfig = new HttpConfig();

    private HttpManager() {
    }

    /**
     * Retrofit配置
     */
    public static HttpConfig config() {
        return mConfig;
    }

    /**
     * 创建Retrofit的极度简化模式
     *
     * @param url 访问链接
     */
    public static HttpSimple create(String url) {
        return HttpSimple.create(url);
    }

    /**
     * 创建Retrofit的Api接口
     *
     * @param service Api接口
     */
    public static <T> T create(Class<T> service) {
        return HttpClient.getRetrofit().create(service);
    }

    /**
     * 创建Retrofit的Api接口
     *
     * @param service  Api接口
     * @param upListener 上传进度监听
     * @param downListener 下载进度监听
     */
    public static <T> T create(Class<T> service, ProgressListener upListener, ProgressListener downListener) {
        return HttpClient.getRetrofit(upListener, downListener).create(service);
    }

    /**
     * 线程调度
     */
    public static <T> ObservableTransformer<T, T> composeThread() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 生命周期绑定
     *
     * @param owner SupportActivity或者Fragment都实现了LifecycleOwner接口
     */
    public static <T> ObservableTransformer<T, T> composeLifecycle(LifecycleOwner owner) {
        return composeLifecycle(owner, null);
    }

    /**
     * 生命周期绑定
     *
     * @param owner SupportActivity或者Fragment都实现了LifecycleOwner接口
     * @param event {@link Lifecycle.Event}
     */
    public static <T> ObservableTransformer<T, T> composeLifecycle(final LifecycleOwner owner, final Lifecycle.Event event) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                if (owner != null) {
                    LifecycleProvider<Lifecycle.Event> lifecycleProvider = AndroidLifecycle.createLifecycleProvider(owner);
                    if (event != null) {
                        return upstream.compose(lifecycleProvider.<T>bindUntilEvent(event));
                    } else {
                        return upstream.compose(lifecycleProvider.<T>bindUntilEvent(Lifecycle.Event.ON_DESTROY));
                    }
                }
                return upstream;
            }
        };
    }

}
