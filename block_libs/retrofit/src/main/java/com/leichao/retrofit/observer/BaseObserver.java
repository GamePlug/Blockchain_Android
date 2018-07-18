package com.leichao.retrofit.observer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.leichao.retrofit.config.Config;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {

    private Context mContext;
    private Dialog mLoading;
    private Disposable mDisposable;

    public BaseObserver() {
        this(null, null, true);
    }

    public BaseObserver(Context context) {
        this(context, null, true);
    }

    public BaseObserver(Context context, String message) {
        this(context, message, true);
    }

    /**
     * 网络请求回调构造方法
     *
     * @param context    Context上下文，不为null则会显示loading
     * @param message    loading显示的信息
     * @param cancelable loading是否可以点击返回键取消，true可以，false不可以，默认为true
     */
    public BaseObserver(Context context, String message, boolean cancelable) {
        mContext = context;
        mLoading = showLoading(message, cancelable);
        if (mLoading != null) {
            mLoading.show();
        }
    }

    @Override
    public final void onSubscribe(Disposable d) {
        mDisposable = d;
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

    private void dismissLoading() {
        if (mLoading == null) {
            return;
        }
        if (mLoading.getContext() instanceof Activity && ((Activity) mLoading.getContext()).isFinishing()) {
            return;
        }
        mLoading.dismiss();
    }

    /**
     * 重写该方法可以控制具体请求的loading显示
     */
    protected Dialog showLoading(String message, boolean cancelable) {
        if (mContext != null) {
            return Config.getInstance().getLoading(mContext, message, cancelable);
        }
        return null;
    }

    protected abstract void handHttpSuccess(T result);

    protected abstract void handHttpFailure(Throwable throwable);

    protected abstract void handHttpCompleted();

}
