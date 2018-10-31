package com.leichao.retrofit.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件操作类
 * Created by leichao on 2016/4/22.
 */
public class FileUtil {

    public static boolean delete(File file) {
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles != null) {
                for (File childFile : childFiles) {
                    delete(childFile);
                }
            }
        }
        return file.delete();
    }

    public static void saveFile(File file, InputStream is) throws IOException {
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            return;
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
