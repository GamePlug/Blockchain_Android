package com.leichao.retrofit.interceptor;

import android.net.Uri;
import android.util.Base64;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Google相关请求添加默认的参数及签名
 * Created by Administrator on 2017/3/3.
 */
public class GoogleParamsInterceptor extends ParamsInterceptor {

    private static final String CLIENT_ID = "gme-mulacarinternational";// 谷歌50万次client_id(客户ID)
    private static final String PRIVATE_KEY = "TbN51m8dEi3QUXMSyClj28E67BQ=";// 谷歌50万次private_key(私钥)

    @Override
    public Map<String, String> getCommonParams(String url) {
        Map<String, String> params = new LinkedHashMap<>();
        Uri uri = Uri.parse(url);
        if (!uri.getQueryParameterNames().contains("key")) {// 如果不含key则使用私钥签名访问
            params.put("client", CLIENT_ID);// 增加客户id参数
            // 增加signature参数
            StringBuilder urlBuilder = new StringBuilder(url);
            for (String key : params.keySet()) {
                urlBuilder.append(urlBuilder.toString().contains("?") ? "&" : "?")
                        .append(key).append("=").append(params.get(key));
            }
            params.put("signature", getSign(urlBuilder.toString()));
        }
        return params;
    }

    /**
     * 给谷歌路线规划接口增加签名
     * @param urlStr 访问的全连接
     * @return 返回签名参数
     */
    public static String getSign(String urlStr) {
        String keyStr = PRIVATE_KEY.replace('-', '+').replace('_', '/');
        byte[] key = Base64.decode(keyStr, Base64.DEFAULT);
        try {
            URL url = new URL(urlStr);
            String resource = url.getPath() + '?' + url.getQuery();
            SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(sha1Key);
            byte[] sigBytes = mac.doFinal(resource.getBytes());
            String signature = Base64.encodeToString(sigBytes, Base64.DEFAULT);
            return signature.replace('+', '-').replace('/', '_');
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }

}
