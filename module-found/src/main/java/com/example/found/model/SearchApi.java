package com.example.found.model;

import com.example.found.model.bean.LabelBean;
import com.example.found.model.bean.ListRecommendResponse;
import com.example.found.model.bean.NewTopSearchResponseBean;
import com.example.found.model.bean.SearchGoodsResponse;
import com.example.found.model.bean.SearchUserResponse;
import com.example.found.model.bean.TopSearchResponseBean;

import java.util.List;

import androidx.databinding.ObservableField;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.network.response.ApiResponse;


/**
 * @author DuLong
 * @since 2020/2/25
 * email 798382030@qq.com
 */
public interface SearchApi {

    @GET("goods/getTopSearch")
    Observable<ApiResponse<List<TopSearchResponseBean>>> getTopSearch();

    @GET("topSearchs/listFg")
    Observable<ApiResponse<NewTopSearchResponseBean>> getNewTopSearch(@Query("schoolId") String schoolId);

    //搜索商品
    //后四个参数是可选参数
    @GET("goods/searchInGoods")
    Observable<ApiResponse<List<SearchGoodsResponse>>> searchGoods(@Query("size") String size, @Query("name") String name,
                                                                   @Query("page") String page, @Query("sortMode") String sortMode,
                                                                   @Query("labels") String[] labels, @Query("minPrice") int minPrice,
                                                                   @Query("maxPrice") int maxPrice);

    @GET("goods/searchInGoods")
    Call<ApiResponse<List<SearchGoodsResponse>>> searchGoodsResult(@Query("size") String size, @Query("name") String name,
                                                                   @Query("page") String page, @Query("sortMode") String sortMode,
                                                                   @Query("labels") Long[] labels, @Query("minPrice") int minPrice,
                                                                   @Query("maxPrice") int maxPrice);
    //搜索用户
    @GET("users/listByLike")
    Observable<ApiResponse<List<SearchUserResponse>>> searchUsers(@Query("name") String name, @Query("page") String page, @Query("size") String size);

    //获取搜索推荐列表
    @GET("goods/listSearchRecommend")
    Observable<ApiResponse<List<ListRecommendResponse>>> getSearchRecommend(@Query("query") String query, @Query("num") String num);

    @GET("labels/listAll")
    Observable<ApiResponse<List<LabelBean>>> getAllLabels();
}
