package com.leichao.retrofit.converter;

import com.leichao.retrofit.Http;
import com.leichao.retrofit.core.Util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Converter;

// 转换成File对象
public final class FileResponseConverter implements Converter<ResponseBody, File> {

    @Override
    public File convert(ResponseBody value) throws IOException {
        File file = new File(Http.config().getDownloadPath(), UUID.randomUUID().toString());
        Util.saveFile(file, value.byteStream());
        Util.log("result:" + "file://"+file.getAbsolutePath());
        return file;
    }

}
