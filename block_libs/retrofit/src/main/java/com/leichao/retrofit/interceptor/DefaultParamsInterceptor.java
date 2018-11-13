package com.leichao.retrofit.interceptor;

import java.util.Collections;
import java.util.Map;

/**
 * 默认参数拦截器
 * Created by leichao on 2017/3/3.
 */
public class DefaultParamsInterceptor extends ParamsInterceptor {

    @Override
    public Map<String, String> getCommonParams(String url) {
        return Collections.emptyMap();
    }

}
