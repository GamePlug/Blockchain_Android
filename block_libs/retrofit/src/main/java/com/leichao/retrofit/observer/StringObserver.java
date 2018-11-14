package com.leichao.retrofit.observer;

public abstract class StringObserver extends BaseObserver<String> {

    @Override
    protected final void handHttpSuccess(String result) {
        httpSuccess(result);
    }

    @Override
    protected final void handHttpFailure(Throwable throwable) {
        httpFailure(throwable);
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

    protected abstract void onHttpSuccess(String result);

    protected void onHttpFailure(Throwable throwable) {
    }

}
