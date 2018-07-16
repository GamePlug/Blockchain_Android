package com.leichao.app;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.alipay.mobilesecuritysdk.face.SecurityClientMobile;
import com.leichao.retrofit.HttpManager;
import com.leichao.retrofit.IStores;
import com.leichao.retrofit.config.ConfigImpl;
import com.leichao.retrofit.loading.CarLoading;
import com.leichao.retrofit.observer.BaseObserver;
import com.leichao.retrofit.observer.MulaObserver;
import com.leichao.retrofit.result.MulaResult;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by leichao on 2018/1/5.
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_hello)
    TextView textView;

    private Unbinder unbinder;
    private BaseObserver mObserver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        SecurityClientMobile mobile = new SecurityClientMobile();
        textView.setText("aaaaaaa");
        textView.setText("Kotlin:" + TestKotlinKt.getA());

        test();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mObserver.cancel();
    }

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
    public static final String KEY_VALUE="key=aaaaaa";

    public void test() {
        HttpManager.config(new ConfigImpl() {
            @Override
            public Map<String, String> getCommonParams(String url) {
                Map<String, String> params = new HashMap<>();
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
            public Dialog getLoading(Context context, String message, boolean cancelable) {
                return new CarLoading(context, message, cancelable);
            }
        });
        mObserver = HttpManager.subscribe(HttpManager.create(IStores.class).getGoogleKey(),
                new MulaObserver<String>(MainActivity.this) {
                    @Override
                    protected void onHttpSuccess(MulaResult<String> result) {

                    }

                    @Override
                    protected void onHttpFailure(MulaResult<String> result) {
                        super.onHttpFailure(result);
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
     *
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
