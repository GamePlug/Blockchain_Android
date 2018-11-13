package com.leichao.retrofit.observer;

import com.leichao.retrofit.loading.BaseLoading;
import com.leichao.retrofit.util.LogUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public abstract class BaseObserver<T> implements Observer<T> {

    private Disposable mDisposable;
    private BaseLoading mLoading;

    @Override
    public final void onSubscribe(Disposable d) {
        mDisposable = d;
        showLoading();
    }

    @Override
    public final void onNext(T t) {
        if (t instanceof ResponseBody) {// 非ResponseBody的结果在转换器中已经打印了
            LogUtil.logE("result:" + "The http result is a ResponseBody");
        }
        handHttpSuccess(t);
    }

    @Override
    public final void onError(Throwable e) {
        LogUtil.logE("result:" + e.toString());
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

    /**
     * 设置具体请求的loading显示
     */
    public final void setLoading(BaseLoading loading) {
        mLoading = loading;
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

    protected abstract void handHttpSuccess(T result);

    protected abstract void handHttpFailure(Throwable throwable);

    protected abstract void handHttpCompleted();

}
