package com.example.found.paging.datasource

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.found.model.bean.SearchGoodsResponse
import com.example.found.model.bean.SearchUserResponse
import com.example.found.paging.NetworkState
import com.example.found.paging.SearchApi
import com.example.network.HttpClient
import com.example.network.observer.BaseObserver
import com.example.network.response.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor
import java.util.concurrent.PriorityBlockingQueue

/**
 * @author DuLong
 * @since 2020/3/27
 * email 798382030@qq.com
 */
class UserDataSource(
        private val name: String,
        private val retryExecutor: Executor
      ) : PageKeyedDataSource<String, SearchUserResponse>(){

    //重新加载的函数
    var retry: (() -> Any)? = null

    var networkState = MutableLiveData<NetworkState>(NetworkState.LOADED)

    var initState = MutableLiveData<NetworkState>()

    //记录上一页的数据
    var beforeData : List<SearchUserResponse>? = ArrayList()

    var mPage: Int = 1

    var searchApi = HttpClient.getInstance().createService(SearchApi::class.java)

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

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, SearchUserResponse>) {
        //进行网络请求
        val searchApi = HttpClient.getInstance().createService(SearchApi::class.java)
        networkState.postValue(NetworkState.LOADING);
        initState.postValue(NetworkState.LOADING);
        searchApi.getUserResult(name, mPage.toString(), params.requestedLoadSize.toString()).enqueue(object : Callback<ApiResponse<List<SearchUserResponse>>> {
            override fun onFailure(call: Call<ApiResponse<List<SearchUserResponse>>>, t: Throwable) {
                retry = {
                    loadInitial(params, callback)
                }
                val error = NetworkState.error(t.message ?: "unknown error")
                networkState.postValue(error)
                initState.postValue(error)
            }

            override fun onResponse(call: Call<ApiResponse<List<SearchUserResponse>>>, response: Response<ApiResponse<List<SearchUserResponse>>>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    //更新状态
                    retry = null
                    networkState.postValue(NetworkState.LOADED)
//                val fadeData = getFadeData()
//                callback.onResult(fadeData, "0", mPage.let { it.plus(1)
//                    it.toString()})
                    if (data!!.isNotEmpty()) {
                        callback.onResult(data as MutableList<SearchUserResponse>, null, mPage.let { mPage += 1
                            mPage.toString()})
                        initState.postValue(NetworkState.LOADED)
                    } else {
                        initState.postValue(NetworkState.EMPTY)
                    }
                    initState.postValue(NetworkState.LOADED)
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

    private fun getFadeData(): List<SearchUserResponse> {
        var datas = ArrayList<SearchUserResponse>()
        for (i in 1..10) {
            val item = SearchUserResponse()
            item.avatar = "http://q7opnl93o.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720190414205835.png"
            item.id = i
            item.name = "大果子${i}号"
            item.schoolId = 1
            item.signature = "大果子爱吃🐟"
            datas.add(item)
        }
        return datas
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, SearchUserResponse>) {
        networkState.postValue(NetworkState.LOADING)

        searchApi.getUserResult(name, mPage.toString(), params.requestedLoadSize.toString()).enqueue(object : retrofit2.Callback<ApiResponse<List<SearchUserResponse>>> {
            //请求失败
            override fun onFailure(call: Call<ApiResponse<List<SearchUserResponse>>>, t: Throwable) {
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(NetworkState.error(t.message ?: "unknown error"))
            }

            override fun onResponse(call: Call<ApiResponse<List<SearchUserResponse>>>, response: Response<ApiResponse<List<SearchUserResponse>>>) =
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        retry = null
                        //改变页数，为下一次请求做准备
                        callback.onResult(data.orEmpty(), mPage.let {mPage += 1
                            mPage.toString()})
//                        val fadeData = getFadeData()
//                        callback.onResult(fadeData, mPage.let { it.plus(1)
//                            it.toString()})
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        retry = {
                            loadAfter(params, callback)
                        }
                        networkState.postValue(NetworkState.error("error code:${response.code()}"))
                    }
        })
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, SearchUserResponse>) {
    }

}