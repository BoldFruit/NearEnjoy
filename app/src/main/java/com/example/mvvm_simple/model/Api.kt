package com.example.mvvm_simple.model

import com.example.mvvm_simple.model.bean.*
import com.example.network.response.ApiResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * @author DuLong
 * @since 2020/4/5
 * email 798382030@qq.com
 */
interface Api {
    //获取轮播图的链接
    @GET("carousels/listFg")
    fun getRotationCharts(@Query("schoolId") schoolId: String) : Observable<ApiResponse<List<RotationChartsBean>>>

    //获得所有的分类
    @GET("categories/listFg")
    fun getAllClassification() : Observable<ApiResponse<List<ClassificationBean>>>

    //获取一级分类的内容
    @GET("goods/listByFirst")
    fun getFirstClassification(@Query("firstId") firstId: Int,
                               @Query("page") page: String,
                               @Query("size") size: String) : Observable<ApiResponse<FirstClassificationBean>>
    //获取二级分类的内容
    @GET("goods/listBySecond")
    fun getSecondClassification(@Query("secondId") firstId: String,
                                @Query("page") page: Int,
                                @Query("size") size: Int) : Observable<ApiResponse<FirstClassificationBean>>

    @GET("labels/listAll")
    fun getAllLabels(): Observable<ApiResponse<List<LabelBean>>>

    //获取首页五个选项的配置
    @GET("mainCategories/listFg")
    fun getMainCategories(): Observable<ApiResponse<List<MainCategoryBean>>>

    //获取tabLayout的配置
    @GET("labelCategories/listFg")
    fun getLabelCategories(): Observable<ApiResponse<List<LabelCategoriesBean>>>

    //获取用户信息
    @GET("users/getUserInfo")
    fun getUserInfo(): Observable<ApiResponse<UserInfoBean>>

    //获取用户商品
    @GET("goods/listGoodsByUserId")
    fun getUserGoods(@Query("page") page: String
                     ,@Query("size") size: String): Observable<ApiResponse<UserGoodsBean>>

    //获取某个种类下的商品
    @GET("goods/list")
    fun getGoodsByCategory(@Query("page") page:String, @Query("size") size: String
    ,@Query("categoryIds") categoryIds: String): Observable<ApiResponse<FirstClassificationBean>>

    //上传图片
    @Multipart
    @POST("images/upload")
    fun uploadFile(@Part file: MultipartBody.Part): Observable<ApiResponse<String>>

    //发布商品
    @POST("goods/create")
    fun publicGoods(@Body body: PublicGoodsReqBean): Observable<ApiResponse<Int>>

    //更新头像
    @PUT("users/updateAvatar")
    fun updateAvatar(@Body body: UpdateAvatarBean): Observable<ApiResponse<String>>
}