package com.example.mvvm_simple.goods_detail.model

import com.example.network.response.ApiResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Time:2020/4/19 11:49
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
interface GoodsApi {

    @GET(value = "goods/getDetail")
    fun getGoodsDetail(@Query(value = "id")id: String): Observable<ApiResponse<GoodsDetailBean>>

}