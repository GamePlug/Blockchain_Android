package com.leichao.retrofit.observer;

import android.content.Context;

import com.leichao.retrofit.HttpManager;

public abstract class StringObserver extends BaseObserver<String> {

    public StringObserver() {
        this(null, null, true);
    }

    public StringObserver(Context context) {
        this(context, null, true);
    }

    public StringObserver(Context context, String message) {
        this(context, message, true);
    }

    public StringObserver(Context context, String message, boolean cancelable) {
        if (context != null) {
            setLoading(HttpManager.config().getLoadingCallback().getLoading(context, message, cancelable));
        }
    }

    @Override
    protected final void handHttpSuccess(String result) {
        httpSuccess(result);
    }

    @Override
    protected final void handHttpFailure(Throwable throwable) {
        httpFailure(throwable);
    }

    @Override
    protected final void handHttpCompleted() {
        httpCompleted();
    }

    private void httpSuccess(String result) {
        try {
            onHttpSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void httpFailure(Throwable throwable) {
        try {
            onHttpFailure(throwable);
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

    protected abstract void onHttpSuccess(String result);

    protected void onHttpFailure(Throwable throwable) {
    }

    protected void onHttpCompleted() {
    }
}
