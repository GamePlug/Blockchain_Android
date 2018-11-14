package com.leichao.retrofit.observer;

import java.io.File;

public abstract class FileObserver extends BaseObserver<File> {

    @Override
    protected final void handHttpSuccess(File result) {
        httpSuccess(result);
    }

    @Override
    protected final void handHttpFailure(Throwable throwable) {
        httpFailure(throwable);
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

    protected abstract void onHttpSuccess(File file);

    protected void onHttpFailure(Throwable throwable) {
    }

}
