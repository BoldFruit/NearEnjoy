package com.example.found.paging

import com.example.found.model.bean.SearchGoodsResponse
import com.example.found.model.bean.SearchUserResponse
import com.example.network.response.ApiResponse
import io.reactivex.Observable
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author DuLong
 * @since 2020/3/11
 * email 798382030@qq.com
 */
interface SearchApi {

    @GET("goods/searchInGoods")
    fun getSearchResult(@Query("size") size: String,
                        @Query("name") name: String,
                        @Query("page") page: String,
                        @Query("sortMode") sortMode: String?,
                        @Query("labels") labels: List<Int>?,
                        @Query("minPrice") minPrice: Int?,
                        @Query("maxPrice") maxPrice: Int?): Call<ApiResponse<SearchGoodsResponse>>

    @GET("users/listByLike")
    fun getUserResult(
                      @Query("name") name: String,
                      @Query("page") page: String,
                      @Query("size") size: String): Call<ApiResponse<List<SearchUserResponse>>>

    companion object {
        private const val BASE_URL = "http://blog.csxjh.vip:8000/"
        //非空
        fun create(): SearchApi = create(BASE_URL.toHttpUrlOrNull()!!)
        fun create(httpUrl: HttpUrl): SearchApi {
            val client = OkHttpClient.Builder()
                    .build()

            return Retrofit.Builder()
                    .baseUrl(httpUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(SearchApi::class.java)
        }
    }
}