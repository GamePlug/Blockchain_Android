package com.leichao.retrofit.interceptor;

import android.net.Uri;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * App相关请求添加默认的参数及签名
 * Created by Administrator on 2017/3/3.
 */
public class MulaParamsInterceptor extends ParamsInterceptor {

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

    @Override
    public Map<String, String> getCommonParams(String url) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put(NONCE_STR, String.valueOf(System.currentTimeMillis()));// 增加nonce_str参数
        params.put(LANGUAGE, getLanguage());// 增加language参数
        params.put(VERSION, getVersion());// 增加version参数
        params.put(CLIENT, "android");// 增加client参数

        // 增加userId参数
        Uri uri = Uri.parse(url);
        if (!TextUtils.isEmpty(getUserId()) && !uri.getQueryParameterNames().contains(USER_ID)) {
            params.put(USER_ID, getUserId());
        }
        // 增加sign参数
        StringBuilder urlBuilder = new StringBuilder(url);
        for (String key : params.keySet()) {
            urlBuilder.append(urlBuilder.toString().contains("?") ? "&" : "?")
                    .append(key).append("=").append(params.get(key));
        }
        params.put(SIGN, getSign(urlBuilder.toString()));
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
     * 获取userId
     */
    private String getUserId() {
        return "";
    }

    /**
     * 获取secret
     */
    private String getSecret() {
        return "";
    }

    /**
     * 获取secretKey
     */
    private String getSecretKey() {
        return "";
    }

    /**
     * 获取语言类型
     */
    private String getLanguage() {
        return "Zh";
    }

    /**
     * 获取版本号
     */
    private String getVersion() {
        return "2.0.0";
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
