package com.leichao.retrofit.observer;

public abstract class JsonObserver<T> extends BaseObserver<T> {

    @Override
    protected final void handHttpSuccess(T result) {
        httpSuccess(result);
    }

    @Override
    protected final void handHttpFailure(Throwable throwable) {
        httpFailure(throwable);
    }

    private void httpSuccess(T result) {
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

    protected abstract void onHttpSuccess(T result);

    protected void onHttpFailure(Throwable throwable) {
    }

}
