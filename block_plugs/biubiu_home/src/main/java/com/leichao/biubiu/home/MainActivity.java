package com.leichao.biubiu.home;

import android.Manifest;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.leichao.retrofit.HttpManager;
import com.leichao.retrofit.api.StringApi;
import com.leichao.retrofit.core.HttpConfig;
import com.leichao.retrofit.interceptor.MulaParamsInterceptor;
import com.leichao.retrofit.loading.BaseLoading;
import com.leichao.retrofit.loading.CarLoading;
import com.leichao.retrofit.observer.HttpObserver;
import com.leichao.retrofit.observer.MulaObserver;
import com.leichao.retrofit.observer.StringObserver;
import com.leichao.retrofit.progress.ProgressListener;
import com.leichao.retrofit.result.HttpResult;
import com.leichao.retrofit.result.MulaResult;
import com.leichao.util.AppUtil;
import com.leichao.util.KeyboardUtil;
import com.leichao.util.LogUtil;
import com.leichao.util.NetworkUtil;
import com.leichao.util.PermissionUtil;
import com.leichao.util.StatusBarUtil;
import com.leichao.util.ToastUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by leichao on 2018/1/5.
 */
public class MainActivity extends AppCompatActivity implements AppUtil.OnAppStatusListener, KeyboardUtil.OnKeyboardStatusListener, NetworkUtil.OnNetworkStatusListener {

    TextView textView;
    EditText etInput;

    private MulaObserver<String> mObserver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //StatusBarUtil.setBackgroundColor(this, Color.RED);
        StatusBarUtil.setFullTranslucent(this);
        KeyboardUtil.fixAndroidBug5497(this);

        textView = findViewById(R.id.main_hello);
        etInput = findViewById(R.id.main_edit);

        textView.setText("aaaaaaa");

        etInput.postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyboardUtil.showSoftInput(etInput);
            }
        }, 200);

        test();

        AppUtil.addStatusListener(this);
        KeyboardUtil.addStatusListener(this, this);
        NetworkUtil.addStatusListener(this);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.show(R.string.app_name);
                KeyboardUtil.removeStatusListener(MainActivity.this, MainActivity.this);
                permission();
            }
        });

    }

    @Override
    public void onAppStatus(boolean isForeground) {
        LogUtil.e("AppStatus:" + isForeground);
    }

    @Override
    public void onKeyboardStatus(boolean isShow) {
        LogUtil.e("KeyboardStatus:" + isShow);
    }

    @Override
    public void onNetworkStatus(NetworkUtil.NetworkStatus status) {
        LogUtil.e("NetworkStatus:" + status.isAvailable + "---" + status.networkType + "---" + status.mobileType);
        textView.setText("NetworkStatus:" + status.isAvailable + "---" + status.networkType + "---" + status.mobileType);
    }

    @Override
    protected void onDestroy() {
        KeyboardUtil.fixSoftInputLeaks(this);
        super.onDestroy();
        mObserver.cancel();
        AppUtil.removeStatusListener(this);
        KeyboardUtil.removeStatusListener(this, this);
        NetworkUtil.removeStatusListener(this);
    }

    private void permission() {
        PermissionUtil.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtil.OnPermissionResultListener() {
            @Override
            public void onPermissionResult(List<String> grantedList, List<String> deniedList) {
                if (grantedList.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //plugin(MainActivity.this);
                }
            }
        });
    }

    /*public static void plugin(Context context) {
        AssetManager asset = context.getAssets();
        //循环的读取asset下的文件，并且写入到SD卡
        FileOutputStream out = null;
        InputStream in=null;
        try {
            File sdDir = new File(Environment.getExternalStorageDirectory().toString()+"/"+"Plugins");
            if (!sdDir.exists() && !sdDir.mkdirs()) {
                return;// 文件夹不存在且创建失败则返回
            }
            String assetPath = "plugins/mula_travel.apk";
            File sdApk = new File(sdDir.getAbsolutePath(), assetPath.substring(
                    assetPath.lastIndexOf("/"), assetPath.length()));
            if (!sdApk.exists() && !sdApk.createNewFile()) {
                return;// 文件不存在且创建失败则返回
            }
            //将内容写入到文件中
            in=asset.open(assetPath);
            out= new FileOutputStream(sdApk);
            byte[] buffer = new byte[1024];
            int byteCount=0;
            while((byteCount=in.read(buffer))!=-1){
                out.write(buffer, 0, byteCount);
            }
            out.flush();
            // 安装插件
            DroidPluginBridge.INSTANCE.installPackage(sdApk.getAbsolutePath(), 0);
            // 启动插件
            PackageManager pm = context.getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage("com.mula.travel");
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                //asset.close();// asset是系统的，不能close
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    public void test() {
        HttpManager.config()
                .setTimeout(40)
                .setParamsInterceptor(new MulaParamsInterceptor())
                .setLoadingCallback(new HttpConfig.LoadingCallback() {
                    @Override
                    public BaseLoading newLoading(Context context, String message, boolean cancelable) {
                        return new CarLoading(context, message, cancelable);
                    }
                });
        mObserver = new MulaObserver<String>() {
            @Override
            protected void onHttpSuccess(MulaResult<String> result) {

            }
        };
        HttpManager.create(HomeApi.class)
                .test()
                .compose(HttpManager.<MulaResult<String>>composeThread())
                .compose(HttpManager.<MulaResult<String>>composeLifecycle(this, Lifecycle.Event.ON_PAUSE))
                .subscribe(mObserver);
        //mObserver.cancel();
        HttpManager.create(StringApi.class)
                .getNormal("api/tms/googleKey/getGoogleKey?isVerify=0",
                        Collections.<String, Object>emptyMap(), Collections.<String, Object>emptyMap())
                .compose(HttpManager.<String>composeLifecycle(this))
                .compose(HttpManager.<String>composeThread())
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
                }.loading(this, "加载啦", true));
        HttpManager.create("http://47.74.159.3:8083/api/tms/tmsMessages/messageList?page=1&userType=2&userId=307ad3da76f24a4aac903c317653f71a&isVerify=0")
                .param("aaaaa", 5555555)
                .post()
                .bindLifecycle(this)
                .upListener(new ProgressListener() {
                    @Override
                    public void onProgress(long progress, long total, boolean done) {
                        LogUtil.e((done ? "上传完成:" : "上传中:") + "--progress:" + progress + "--total:" + total);
                    }
                })
                .downListener(new ProgressListener() {
                    @Override
                    public void onProgress(long progress, long total, boolean done) {
                        LogUtil.e((done ? "下载完成:" : "下载中:") + "--progress:" + progress + "--total:" + total);
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
                        LogUtil.e(result.toString());
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
