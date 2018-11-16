package com.leichao.retrofit.cookie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 使用内存cookie管理器
 */
public class MemoryCookie implements CookieJar {

    private Map<String, List<Cookie>> cookieMap = new HashMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieMap.put(url.host(), cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookieMap.get(url.host());
    }

}
