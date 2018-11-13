package com.leichao.retrofit.converter;

import com.leichao.retrofit.HttpManager;
import com.leichao.retrofit.util.FileUtil;
import com.leichao.retrofit.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Converter;

// 转换成File对象
public final class FileResponseConverter implements Converter<ResponseBody, File> {

    @Override
    public File convert(ResponseBody value) throws IOException {
        File file = new File(HttpManager.config().getDownloadPath(), UUID.randomUUID().toString());
        FileUtil.saveFile(file, value.byteStream());
        LogUtil.logE("result:" + "file://"+file.getAbsolutePath());
        return file;
    }

}
