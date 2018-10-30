package com.leichao.retrofit.observer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.leichao.retrofit.HttpManager;
import com.leichao.retrofit.loading.BaseLoading;
import com.leichao.retrofit.util.FileUtil;

import java.io.File;
import java.util.UUID;

import okhttp3.ResponseBody;

public abstract class FileObserver extends BaseObserver<ResponseBody> {

    private BaseLoading mLoading;

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
            mLoading = HttpManager.config().getCallback().getLoading(context, message, cancelable);
        }
    }

    @Override
    protected BaseLoading onHttpLoading() {
        return mLoading;
    }

    @Override
    protected final void handHttpSuccess(final ResponseBody result) {
        // 由于接口方法增加了注释@Streaming，所以流的读取和写入要在子线程中执行,否则会有NetworkOnMainThreadException
        new Thread(new Runnable() {
            @Override
            public void run() {
                final File file = new File(HttpManager.config().getDownloadDir(), UUID.randomUUID().toString());
                FileUtil.inputStreamToFile(result.byteStream(), file);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        httpSuccess(file);
                    }
                });
            }
        }).start();
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
