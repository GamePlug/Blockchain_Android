package com.leichao.retrofit.cookie;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 磁盘cookie管理器
 * 参考：https://www.jianshu.com/p/41b4cbe1dbec
 */
public class DiskCookies implements CookieJar {

    private final DiskCookieStore cookieStore;

    public DiskCookies(Context context) {
        cookieStore = new DiskCookieStore(context);
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        for (Cookie item : cookies) {
            cookieStore.add(url, item);
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.get(url);
    }

}
