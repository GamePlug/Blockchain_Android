package com.leichao.retrofit.observer;

import android.content.Context;

import com.leichao.retrofit.Http;
import com.leichao.retrofit.core.Util;
import com.leichao.retrofit.loading.BaseLoading;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public abstract class BaseObserver<T> implements Observer<T> {

    private Disposable mDisposable;
    private BaseLoading mLoading;

    @Override
    public final void onSubscribe(Disposable d) {
        mDisposable = d;
        httpStart();
        showLoading();
    }

    @Override
    public final void onNext(T t) {
        if (t instanceof ResponseBody) {// 非ResponseBody的结果在转换器中已经打印了
            Util.log("result:" + "The http result is a ResponseBody");
        }
        handHttpSuccess(t);
    }

    @Override
    public final void onError(Throwable e) {
        Util.log("result:" + e.toString());
        handHttpFailure(e);
        onComplete();
    }

    @Override
    public final void onComplete() {
        httpCompleted();
        dismissLoading();
    }

    /**
     * 取消请求
     */
    public final void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            dismissLoading();
        }
    }

    /**
     * 设置具体请求的loading显示
     */
    public final <N extends BaseObserver<T>> N loading(Context context) {
        return loading(context, null);
    }
    public final <N extends BaseObserver<T>> N loading(Context context, String message) {
        return loading(context, message, true);
    }
    public final <N extends BaseObserver<T>> N loading(Context context, String message, boolean cancelable) {
        return loading(Http.config().getLoadingCallback().newLoading(context, message, cancelable));
    }
    public final <N extends BaseObserver<T>> N loading(BaseLoading loading) {
        mLoading = loading;
        return (N)this;
    }

    private void showLoading() {
        if (mLoading != null) {
            mLoading.show();
        }
    }

    private void dismissLoading() {
        if (mLoading != null) {
            mLoading.dismiss();
        }
    }

    private void httpStart() {
        try {
            onHttpStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void httpCompleted() {
        try {
            onHttpCompleted();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onHttpStart() {
    }

    protected void onHttpCompleted() {
    }

    protected abstract void handHttpSuccess(T result);

    protected abstract void handHttpFailure(Throwable throwable);

}
