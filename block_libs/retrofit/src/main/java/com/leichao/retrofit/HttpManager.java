package com.leichao.retrofit;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.leichao.retrofit.core.HttpApi;
import com.leichao.retrofit.core.HttpClient;
import com.leichao.retrofit.core.HttpConfig;
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

    public static HttpConfig config() {
        return mConfig;
    }

    public static HttpApi create() {
        return create(HttpApi.class);
    }

    public static <T> T create(Class<T> service) {
        return HttpClient.getInstance().getRetrofit().create(service);
    }

    public static <T> ObservableTransformer<T, T> transformer() {
        return transformer(null, null);
    }

    public static <T> ObservableTransformer<T, T> transformer(LifecycleOwner owner) {
        return transformer(owner, null);
    }

    public static <T> ObservableTransformer<T, T> transformer(final LifecycleOwner owner, final Lifecycle.Event event) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                Observable<T> observable = upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                if (owner != null) {
                    LifecycleProvider<Lifecycle.Event> lifecycleProvider = AndroidLifecycle.createLifecycleProvider(owner);
                    if (event != null) {
                        return observable.compose(lifecycleProvider.<T>bindUntilEvent(event));
                    } else {
                        return observable.compose(lifecycleProvider.<T>bindUntilEvent(Lifecycle.Event.ON_DESTROY));
                    }
                }
                return observable;
            }
        };
    }

}
