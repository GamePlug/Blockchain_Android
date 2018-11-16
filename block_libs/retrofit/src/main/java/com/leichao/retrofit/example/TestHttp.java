package com.leichao.retrofit.example;

import android.content.Context;
import android.support.v4.app.SupportActivity;

import com.leichao.retrofit.Http;
import com.leichao.retrofit.core.HttpConfig;
import com.leichao.retrofit.core.Util;
import com.leichao.retrofit.interceptor.MulaParamsInterceptor;
import com.leichao.retrofit.loading.BaseLoading;
import com.leichao.retrofit.loading.CarLoading;
import com.leichao.retrofit.observer.HttpObserver;
import com.leichao.retrofit.progress.ProgressListener;
import com.leichao.retrofit.result.HttpResult;

import java.io.File;

public class TestHttp {

    public static void test(SupportActivity activity) {
        Http.config()
                .setTimeout(40)
                .setParamsInterceptor(new MulaParamsInterceptor())
                .setLoadingCallback(new HttpConfig.LoadingCallback() {
                    @Override
                    public BaseLoading newLoading(Context context, String message, boolean cancelable) {
                        return new CarLoading(context, message, cancelable);
                    }
                });
        /*MulaObserver<String> mObserver = new MulaObserver<String>() {
            @Override
            protected void onHttpSuccess(MulaResult<String> result) {

            }
        };
        Http.create(TestApi.class)
                .test()
                .compose(Http.<MulaResult<String>>composeThread())
                .compose(Http.<MulaResult<String>>composeLifecycle(activity, Lifecycle.Event.ON_PAUSE))
                .subscribe(mObserver);*/
        //mObserver.cancel();
        /*Http.create(StringApi.class, HttpClient.builder().addInterceptor(new GoogleParamsInterceptor()))
                .getNormal("api/tms/googleKey/getGoogleKey?isVerify=0",
                        Collections.<String, Object>emptyMap(), Collections.<String, Object>emptyMap())
                .compose(Http.<String>composeLifecycle(activity))
                .compose(Http.<String>composeThread())
                .subscribe(new StringObserver() {
                    @Override
                    protected void onHttpSuccess(String result) {

                    }

                    @Override
                    protected void onHttpFailure(Throwable throwable) {

                    }

                    @Override
                    protected void onHttpStart() {

                    }

                    @Override
                    protected void onHttpCompleted() {

                    }
                }.loading(activity, "加载啦", true));*/
        Http.create("http://47.74.159.3:8083/api/tms/tmsMessages/messageList?page=1&userType=2&userId=307ad3da76f24a4aac903c317653f71a&isVerify=0")
                .param("aaaaa", new File(activity.getPackageManager().getInstalledApplications(0).get(0).sourceDir))
                .param("bbbbb", "55555")
                //.body("123456789")
                .post()
                .bindLifecycle(activity)
                .upListener(new ProgressListener() {
                    @Override
                    public void onProgress(long progress, long total, boolean done) {
                        Util.log((done ? "上传完成:" : "上传中:") + "--progress:" + progress + "--total:" + total);
                    }
                })
                .downListener(new ProgressListener() {
                    @Override
                    public void onProgress(long progress, long total, boolean done) {
                        Util.log((done ? "下载完成:" : "下载中:") + "--progress:" + progress + "--total:" + total);
                    }
                })
                /*.subscribe(new StringObserver() {
                    @Override
                    protected void onHttpSuccess(String result) {
                        LogUtil.e(result);
                    }
                });*/
                /*.subscribe(new FileObserver() {
                    @Override
                    protected void onHttpSuccess(File file) {
                        LogUtil.e(file.toString());
                    }

                    @Override
                    protected void onHttpFailure(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });*/
                .subscribe(new HttpObserver<TestBean>() {
                    @Override
                    protected void onHttpSuccess(HttpResult<TestBean> result) {
                        Util.log(result.toString());
                    }
                });

        /*HttpManager.create(HomeApi.class).test2().compose(HttpManager.<HttpResult<TestBean>>composeThread())
                .subscribe(new HttpObserver<TestBean>() {
                    @Override
                    protected void onHttpSuccess(HttpResult<TestBean> result) {
                        LogUtil.e(result.toString());
                    }
                });*/
    }

}
