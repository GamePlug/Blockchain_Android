package com.leichao.retrofit.core;

import android.util.Log;

import com.google.gson.Gson;
import com.leichao.retrofit.Http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Util {

    // 打印log
    public static void log(String str) {
        if (Http.config().isDebug()) {
            Log.e("HTTP", str);
        }
    }

    // 从type中获取class类型
    public static Class getClassFromType(Type type) {
        if (type instanceof Class) {
            return (Class) type;

        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class) {
                return  (Class) rawType;
            }
        }
        return null;
    }

    // 是否是String或者基本类型
    public static boolean isStringOrBase(Class clazz) {
        if (clazz == String.class
                || clazz == boolean.class || clazz == Boolean.class
                || clazz == byte.class || clazz == Byte.class
                || clazz == char.class || clazz == Character.class
                || clazz == double.class || clazz == Double.class
                || clazz == float.class || clazz == Float.class
                || clazz == int.class || clazz == Integer.class
                || clazz == long.class || clazz == Long.class
                || clazz == short.class || clazz == Short.class) {
            return true;
        }
        return false;
    }

    // 转换成RequestBody
    public static RequestBody requestBody(Object object) {
        Class clazz = object.getClass();
        if (isStringOrBase(clazz)) {
            // 如果是String或者基本类型，则转换成String
            return RequestBody.create(MediaType.parse(Constant.CONTENT_TYPE_TEXT + Constant.CHARSET_UTF_8), (String) object);

        } else if (clazz == File.class) {
            // 如果是File类型，则转换成File
            return RequestBody.create(MediaType.parse(Constant.CONTENT_TYPE_PART), (File) object);

        } else {
            // 如果是其他类型，则转换成json
            String jsonStr = "";
            try {
                jsonStr = new Gson().toJson(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return RequestBody.create(MediaType.parse(Constant.CONTENT_TYPE_JSON + Constant.CHARSET_UTF_8), jsonStr);
        }
    }

    // 获取需上传的文件的key
    public static String fileKey(String key, File file) {
        return fileKey(key, file.getName());
    }

    // 获取需上传的文件的key
    public static String fileKey(String key, String fileName) {
        if (!key.contains("filename")) {
            key += "\"; filename=\"" + fileName;
        }
        return key;
    }

    // 删除文件
    public static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles != null) {
                for (File childFile : childFiles) {
                    deleteFile(childFile);
                }
            }
        }
        return file.delete();
    }

    // 保存文件
    public static void saveFile(File file, InputStream is) throws IOException {
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            throw new FileNotFoundException(file.getAbsolutePath() + "(Path error or Permission denied)");
        }
        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = bis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            fos.flush();
        }
        fos.close();
        bis.close();
        is.close();
    }

}
