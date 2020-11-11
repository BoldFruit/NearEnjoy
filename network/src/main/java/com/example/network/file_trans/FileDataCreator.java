package com.example.network.file_trans;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Time:2020/4/7 20:58
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: 用于将文件转换成MultiPartBody.Part
 */
public class FileDataCreator {

    public static MultipartBody.Part createMultiPart(String filePath, String key) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create( file, MediaType.parse("multipart/form-data"));
        return MultipartBody.Part.createFormData(key, file.getName(), requestFile);
    }

}
