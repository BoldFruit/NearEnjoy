package com.example.mvvm_simple.categories.mvvm.model

import com.example.lib_common.linkage_kt.data.LinkData
import com.example.mvvm_simple.model.bean.FirstClassificationBean
import com.example.network.response.ApiResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Time:2020/4/10 15:47
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
interface ICategoriesApi {

    @GET("categories/listFg")
    fun getCategories(): Observable<ApiResponse<ArrayList<LinkData>>>

    /**
     * 获取分类结果
     */
    @GET("goods/list")
    fun getCategoriesList(
            @Query(value = "page")
            page: String,
            @Query(value = "size")
            size: String,
            @Query(value = "categoryIds")
            categoryIds: String
    ): Observable<ApiResponse<FirstClassificationBean>>

}