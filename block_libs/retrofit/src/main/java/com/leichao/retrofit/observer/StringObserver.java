package com.leichao.retrofit.observer;

import android.content.Context;

import com.leichao.retrofit.config.Config;
import com.leichao.retrofit.loading.BaseLoading;
import com.leichao.retrofit.util.LogUtil;

public abstract class StringObserver extends BaseObserver<String> {

    private BaseLoading mLoading;

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
            mLoading = Config.getInstance().getLoading(context, message, cancelable);
        }
    }

    @Override
    protected BaseLoading onHttpLoading() {
        return mLoading;
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
