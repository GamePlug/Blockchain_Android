package com.leichao.retrofit.observer;

import android.content.Context;

import com.leichao.retrofit.util.LogUtil;

public abstract class StringObserver extends BaseObserver<String> {

    public StringObserver() {
    }

    public StringObserver(Context context) {
        super(context);
    }

    public StringObserver(Context context, String message) {
        super(context, message);
    }

    public StringObserver(Context context, String message, boolean cancelable) {
        super(context, message, cancelable);
    }

    @Override
    protected final void handHttpSuccess(String result) {
        httpSuccess(result);
    }

    @Override
    protected final void handHttpFailure(Throwable throwable) {
        LogUtil.logE("result:" + throwable.toString());
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
