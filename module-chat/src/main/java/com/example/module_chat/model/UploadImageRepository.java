package com.example.module_chat.model;

import com.example.module_chat.api.IUploadFileApi;
import com.example.network.HttpClient;
import com.example.network.file_trans.FileDataCreator;
import com.example.network.observer.BaseObserver;

/**
 * Time:2020/4/7 21:58
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class UploadImageRepository {

    private static final Object o = new Object();
    private static UploadImageRepository instance = null;

    public static UploadImageRepository getInstance() {
        if (instance == null) {
            synchronized (o) {
                if (instance == null) {
                    instance = new UploadImageRepository();
                }
            }
        }
        return instance;
    }

    private IUploadFileApi getUploadApi() {
        return HttpClient.getInstance().createService(IUploadFileApi.class);
    }

    public void startUploadImg(String path, BaseObserver<String> observer) {
        HttpClient.getInstance().apiSubscribe(getUploadApi().
                        uploadImage(FileDataCreator.createMultiPart(path, "image")), observer);
    }

}
