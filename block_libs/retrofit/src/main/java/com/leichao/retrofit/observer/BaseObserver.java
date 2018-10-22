package com.leichao.retrofit.observer;

import com.leichao.retrofit.loading.BaseLoading;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {

    private Disposable mDisposable;
    private BaseLoading mLoading;

    @Override
    public final void onSubscribe(Disposable d) {
        mDisposable = d;
        mLoading = onHttpLoading();
        showLoading();
    }

    @Override
    public final void onNext(T t) {
        handHttpSuccess(t);
    }

    @Override
    public final void onError(Throwable e) {
        handHttpFailure(e);
        onComplete();
    }

    @Override
    public final void onComplete() {
        handHttpCompleted();
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

    /**
     * 重写该方法可以控制具体请求的loading显示
     */
    protected BaseLoading onHttpLoading() {
        return null;
    }

    protected abstract void handHttpSuccess(T result);

    protected abstract void handHttpFailure(Throwable throwable);

    protected abstract void handHttpCompleted();

}
