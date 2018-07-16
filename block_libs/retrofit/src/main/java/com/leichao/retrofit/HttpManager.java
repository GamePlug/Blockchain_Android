package com.leichao.retrofit;

import com.leichao.retrofit.config.Config;
import com.leichao.retrofit.config.IConfig;
import com.leichao.retrofit.observer.BaseObserver;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HttpManager {

    public static void config(IConfig config) {
        Config.getInstance().setConfig(config);
    }

    public static <T> T create(Class<T> service) {
        return HttpClient.getInstance().getRetrofit().create(service);
    }

    public static <T> BaseObserver<T> subscribe(Observable<T> observable, BaseObserver<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        return observer;
    }

}
