package com.leichao.retrofit.util;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class DataUtil {

    public static Map<String, Object> formatParams(Map<String, Object> params) {
        Map<String, Object> newParams = new LinkedHashMap<>();
        for (String key : params.keySet()) {
            addParams(newParams, key, params.get(key));
        }
        return newParams;
    }

    public static void addParams(Map<String, Object> params, String key, Object object) {
        if (object instanceof File) {
            File file = (File) object;
            RequestBody requestBody = RequestBody
                    .create(MediaType.parse(Constant.CONTENT_TYPE_PART), file);
            params.put(key + "\"; filename=\"" + file.getName(), requestBody);
        } else {
            String string = object.toString();
            RequestBody requestBody = RequestBody
                    .create(MediaType.parse(Constant.CONTENT_TYPE_PART), string);
            params.put(key, requestBody);
        }
    }

}
