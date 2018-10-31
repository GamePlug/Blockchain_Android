package com.leichao.biubiu.home;

import android.Manifest;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.leichao.retrofit.HttpManager;
import com.leichao.retrofit.HttpSimple;
import com.leichao.retrofit.api.StringApi;
import com.leichao.retrofit.core.HttpConfig;
import com.leichao.retrofit.loading.BaseLoading;
import com.leichao.retrofit.loading.CarLoading;
import com.leichao.retrofit.observer.FileObserver;
import com.leichao.retrofit.observer.MulaObserver;
import com.leichao.retrofit.observer.StringObserver;
import com.leichao.retrofit.progress.ProgressListener;
import com.leichao.retrofit.result.MulaResult;
import com.leichao.util.AppUtil;
import com.leichao.util.KeyboardUtil;
import com.leichao.util.LogUtil;
import com.leichao.util.NetworkUtil;
import com.leichao.util.PermissionUtil;
import com.leichao.util.StatusBarUtil;
import com.leichao.util.ToastUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

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

    // 单点登录传递的参数
    public static final String SECRET = "secret";
    public static final String SECRET_KEY = "secretKey";
    public static final String IS_VERIFY = "isVerify";// 是否需要验证单点登录
    // 接口需要默认传递的参数
    public static final String USER_ID = "userId";
    public static final String NONCE_STR = "nonce_str";
    public static final String SIGN = "sign";
    public static final String LANGUAGE = "language";
    public static final String VERSION = "version";
    public static final String CLIENT = "client";
    // 加密的key与值
    public static final String KEY_VALUE = "key=aaaaaa";

    public void test() {
        HttpManager.config()
                .setTimeout(40)
                .setCallback(new HttpConfig.Callback() {
                    @Override
                    public Map<String, String> getCommonParams(String url) {
                        Map<String, String> params = new LinkedHashMap<>();
                        params.put(NONCE_STR, String.valueOf(System.currentTimeMillis()));// 增加nonce_str参数
                        params.put(LANGUAGE, "Zh");// 增加language参数
                        params.put(VERSION, "2.0.0");// 增加version参数
                        params.put(CLIENT, "android");// 增加client参数

                        // 增加userId参数
                        Uri uri = Uri.parse(url);
                        if (!TextUtils.isEmpty(getUserId()) && !uri.getQueryParameterNames().contains(USER_ID)) {
                            params.put(USER_ID, getUserId());
                        }
                        // 增加sign参数
                        for (String key : params.keySet()) {
                            String value = params.get(key);
                            url += (url.contains("?") ? "&" : "?") + key + "=" + value;
                        }
                        String sign = getSign(url);
                        params.put(SIGN, sign);
                        // 增加单点登录验证,不参与签名
                        if (!uri.getQueryParameterNames().contains(IS_VERIFY)) {
                            String secret = getSecret();
                            if (TextUtils.isEmpty(secret)) {
                                params.put(IS_VERIFY, "0");
                            } else {
                                params.put(IS_VERIFY, "1");
                                params.put(SECRET, secret);
                                params.put(SECRET_KEY, getSecretKey());
                            }
                        }
                        return params;
                    }

                    @Override
                    public BaseLoading getLoading(Context context, String message, boolean cancelable) {
                        return new CarLoading(context, message, cancelable);
                    }
                });
        mObserver = new MulaObserver<String>(this) {
            @Override
            protected void onHttpSuccess(MulaResult<String> result) {

            }
        };
        HttpManager.create(HomeApi.class)
                .test()
                .compose(HttpManager.<MulaResult<String>>transformer(this, Lifecycle.Event.ON_PAUSE))
                .subscribe(mObserver);
        //mObserver.cancel();
        HttpManager.create(StringApi.class)
                .getNormal("api/tms/googleKey/getGoogleKey?isVerify=0",
                        Collections.<String, Object>emptyMap(), Collections.<String, Object>emptyMap())
                .compose(HttpManager.<String>transformer(this))
                .subscribe(new StringObserver() {
                    @Override
                    protected void onHttpSuccess(String result) {

                    }
                });
        HttpSimple.create("api/tms/googleKey/getGoogleKey?isVerify=0")
                .param("aaaaa", 5555555)
                .bindLifecycle(this)
                .progress(new ProgressListener() {
                    @Override
                    public void onProgress(long progress, long total, boolean done) {
                        LogUtil.e((done ? "下载完成:" : "下载中:") + "--progress:" + progress + "--total:" + total);
                    }
                })
                .getFile(new FileObserver() {
                    @Override
                    protected void onHttpSuccess(File file) {

                    }
                });
    }

    private String getUserId() {
        return "";
    }

    private String getSecret() {
        return "";
    }

    private String getSecretKey() {
        return "";
    }

    /**
     * 生成加密签名
     */
    public String getSign(String url) {
        Uri uri = Uri.parse(url);
        TreeSet<String> treeSet = new TreeSet<>();// TreeSet的默认排序为ASCII码从小到大排序
        for (String key : uri.getQueryParameterNames()) {
            if (!key.equals(SECRET) && !key.equals(SECRET_KEY) && !key.equals(IS_VERIFY)) {// 单点登录参数不参与签名
                String value = uri.getQueryParameter(key);
                if (!TextUtils.isEmpty(value)) {// 参数值为空不参与签名
                    treeSet.add(key);// 参数名ASCII码从小到大排序
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String key : treeSet) {
            String value = uri.getQueryParameter(key);
            sb.append(key).append("=").append(value).append("&");
        }
        sb.append(KEY_VALUE);
        return md5(sb.toString()).toUpperCase();
    }

    /**
     * md5加密
     */
    public String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}
