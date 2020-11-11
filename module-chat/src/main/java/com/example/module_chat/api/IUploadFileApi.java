package com.example.module_chat.api;

import com.example.network.response.ApiResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Time:2020/4/7 21:54
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public interface IUploadFileApi {

    @POST("images/upload")
    Observable<ApiResponse<String>> uploadImage(@Part("image")MultipartBody.Part body);

}
