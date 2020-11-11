package com.example.found.paging.datasource

import androidx.annotation.Nullable
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import androidx.room.Index
import com.example.base.network.NetWorkStatus
import com.example.found.model.SearchApi
import com.example.found.model.SearchRepository
import com.example.found.model.bean.SearchGoodsResponse
import com.example.found.paging.NetworkState
import com.example.network.HttpClient
import com.example.network.response.ApiResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor
import kotlin.math.max

/**
 * @author DuLong
 * @since 2020/3/11
 * email 798382030@qq.com
 */
/**
 * A data source that uses the page as keys returned in page request
 */
class   GoodsDataSource(
        private val name: String,
        private val sortMode: String?,
        private val labels: List<Int>?,
        private val minPrice: Int?,
        private val maxPrice: Int?,
        private val retryExecutor: Executor) : PageKeyedDataSource<String, SearchGoodsResponse.SearchListBean>(){

    //重新加载的函数
    private var retry: (() -> Any)? = null

    var networkState = MutableLiveData<NetworkState>(NetworkState.LOADED)

    //用来监测第一次的加载情况。
    var initState = MutableLiveData<NetworkState>()

    var mPage: Int = 1

    private var searchApi = HttpClient.getInstance().createService(com.example.found.paging.SearchApi::class.java)
    /**
     * 重新加载
     */
    fun retryFinal() {
        val preRetry = retry
        retry = null
        preRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    /**
     * 第一次加载
     */
    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, SearchGoodsResponse.SearchListBean>) {
        networkState.postValue(NetworkState.LOADING);
        initState.postValue(NetworkState.LOADING);
        //进行网络请求
        searchApi.getSearchResult(params.requestedLoadSize.toString(), name, mPage.toString(), sortMode, labels, minPrice, maxPrice).enqueue(object : Callback<ApiResponse<SearchGoodsResponse>> {
            override fun onFailure(call: Call<ApiResponse<SearchGoodsResponse>>, t: Throwable) {
                retry = {
                    loadInitial(params, callback)
                }
                val error = NetworkState.error(t.message ?: "unknown error")
                networkState.postValue(error)
                initState.postValue(error)
            }

            override fun onResponse(call: Call<ApiResponse<SearchGoodsResponse>>, response: Response<ApiResponse<SearchGoodsResponse>>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    val listData = data?.searchList ?: emptyList()
                    //更新状态
                    retry = null;
                    networkState.postValue(NetworkState.LOADED)
//                val fadeData = getFadeData()
//                callback.onResult(fadeData.searchList, fadeData.prevPage.toString(), fadeData.nextPage.toString())
//                    initState.postValue(NetworkState.LOADED)
                    if (listData.isNotEmpty()) {
                        initState.postValue(NetworkState.LOADED)
                        callback.onResult(listData as MutableList<SearchGoodsResponse.SearchListBean>, data?.prevPage.toString(), data?.nextPage.toString())
                    } else {
                        initState.postValue(NetworkState.EMPTY)
                    }
                } else {
                    retry = {
                        loadInitial(params, callback)
                    }
                    initState.postValue(NetworkState.error("error code: ${response.code()}"))
                    networkState.postValue(
                            NetworkState.error("error code: ${response.code()}"))
                }
            }

        })

    }

    /**
     *  后一页加载
     */
    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, SearchGoodsResponse.SearchListBean>) {
        networkState.postValue(NetworkState.LOADING)
        searchApi.getSearchResult(params.requestedLoadSize.toString(), name, params.key, sortMode, labels, minPrice, maxPrice).enqueue(object : retrofit2.Callback<ApiResponse<SearchGoodsResponse>> {
            //请求失败
            override fun onFailure(call: Call<ApiResponse<SearchGoodsResponse>>, t: Throwable) {
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(NetworkState.error(t.message ?: "unknown error"))
            }

            override fun onResponse(call: Call<ApiResponse<SearchGoodsResponse>>, response: Response<ApiResponse<SearchGoodsResponse>>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    retry = null
                    callback.onResult(data?.searchList ?: emptyList(), data?.nextPage.toString())
//                    val fadeData = getFadeData2()
//                    callback.onResult(fadeData.searchList, mPage.let { it.plus(1)
//                        it.toString()})
                    networkState.postValue(NetworkState.LOADED)
                } else {
                    retry = {
                        loadAfter(params, callback)
                    }
                    networkState.postValue(NetworkState.error("error code:${response.code()}"))
                }
            }
        })
    }

    /**
     *  前一页加载
     */
    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, SearchGoodsResponse.SearchListBean>) {

    }

    /**
     * 获取虚假数据，用于测试使用
     * 3:4
     * http://q7opnl93o.bkt.clouddn.com/3%EF%BC%9A4.png
     * 4:3
     * http://q7opnl93o.bkt.clouddn.com/4%EF%BC%9A3.png
     */
    fun getFadeData() : SearchGoodsResponse {
       var goods: MutableList<SearchGoodsResponse.SearchListBean> = MutableList(10) { index ->
           var item = SearchGoodsResponse.SearchListBean()
           item.wants = index
           item.name = "龙猫"
           item.labelIds = "[\"2\",\"3\"]"
           item.price = 100.00
           item.images = when (index % 3) {
               0, 3 -> "[\"http://q7opnl93o.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720190414205835.png\"]"
               1 -> "[\"http://q7opnl93o.bkt.clouddn.com/3%EF%BC%9A4.png\"]"
               2 -> "[\"http://q7opnl93o.bkt.clouddn.com/4%EF%BC%9A3.png\"]"
               else -> "[\"http://q7opnl93o.bkt.clouddn.com/3%EF%BC%9A4.png\"]"
           }
           item.description = "这是一些测试的数据"
           item.userAvatar = "http://q7opnl93o.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720190414205835.png"
           item.id = System.currentTimeMillis().toInt()
           item.userName = "大果子"
           item
       }
        var data: SearchGoodsResponse = SearchGoodsResponse()
        data.searchList = goods
        data.nextPage = 2
        data.prevPage = 0
        return data
    }

    fun getFadeData2() : SearchGoodsResponse {
        var goods: MutableList<SearchGoodsResponse.SearchListBean> = MutableList(10) { index ->
            var item = SearchGoodsResponse.SearchListBean()
            item.wants = index
            item.name = "大果子"
            item.labelIds = "[\"2\",\"3\"]"
            item.price = 100.00
            item.images = when (index % 3) {
                0, 3 -> "[\"http://q7opnl93o.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720190414205835.png\"]"
                1 -> "[\"http://q7opnl93o.bkt.clouddn.com/3%EF%BC%9A4.png\"]"
                2 -> "[\"http://q7opnl93o.bkt.clouddn.com/4%EF%BC%9A3.png\"]"
                else -> "[\"http://q7opnl93o.bkt.clouddn.com/3%EF%BC%9A4.png\"]"
            }
            item.description = "这是一些测试的数据"
            item.userAvatar = "http://q7opnl93o.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720190414205835.png"
            item.id = System.currentTimeMillis().toInt()
            item.userName = "龙猫"
            item
        }
        var data: SearchGoodsResponse = SearchGoodsResponse()
        data.searchList = goods
        data.nextPage = 2
        data.prevPage = 0
        return data
    }
}

