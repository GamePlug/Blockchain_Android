package com.leichao.retrofit.observer;

import android.content.Context;

import com.leichao.retrofit.HttpManager;

import java.io.File;

public abstract class FileObserver extends BaseObserver<File> {

    public FileObserver() {
        this(null, null, true);
    }

    public FileObserver(Context context) {
        this(context, null, true);
    }

    public FileObserver(Context context, String message) {
        this(context, message, true);
    }

    public FileObserver(Context context, String message, boolean cancelable) {
        if (context != null) {
            setLoading(HttpManager.config().getCallback().getLoading(context, message, cancelable));
        }
    }

    @Override
    protected final void handHttpSuccess(File result) {
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

    private void httpSuccess(File result) {
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

    protected abstract void onHttpSuccess(File file);

    protected void onHttpFailure(Throwable throwable) {
    }

    protected void onHttpCompleted() {
    }
}
