package com.example.module_splash;

import com.example.network.response.ApiResponse;
import com.yalantis.ucrop.UCropActivity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author DuLong
 * @since 2020/5/4
 * email 798382030@qq.com
 */
public interface ApiService {
    /**
     * 获取所有展示页
     * @param page
     * @param size
     * @return
     */
    @GET("bootPages/listAll")
    Observable<ApiResponse<List<GetSplashBean>>> getSplash(@Query("page") String page, @Query("size") String size);
}
