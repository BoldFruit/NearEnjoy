package com.example.mvvm_simple.view.paging.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.mvvm_simple.model.MainRepository
import com.example.mvvm_simple.model.bean.UserGoodsBean
import com.example.mvvm_simple.view.paging.NetworkState
import com.example.mvvm_simple.view.paging.PagingObserver
import java.util.concurrent.Executor

/**
 * @author DuLong
 * @since 2020/5/1
 * email 798382030@qq.com
 */
class UserGoodsDataSource(private val retryExecutor: Executor): PageKeyedDataSource<Int, UserGoodsBean.SearchListBean>() {

    private var retry: (() -> Any)? = null

    var networkState = MutableLiveData<NetworkState>(NetworkState.LOADED)

    var initState = MutableLiveData<NetworkState>()

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


    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UserGoodsBean.SearchListBean>) {
        networkState.postValue(NetworkState.LOADING)
        MainRepository.INSTANCE.getUserGoods(params.requestedLoadSize.toString(), params.key.toString(), object : PagingObserver<UserGoodsBean>() {
            override fun onError(e: Throwable) {
                super.onError(e)
                retry = {
                    loadAfter(params, callback)
                }
                val error = NetworkState.error(e.message
                        ?: "unknown error")
                networkState.postValue(error)
            }

            override fun onNext(t: UserGoodsBean) {
                //更新状态
                val listData = t.searchList
                retry = null
                networkState.postValue(NetworkState.LOADED)
                if (!listData.isNullOrEmpty()) {
                    callback.onResult(listData, t.nextPage)
                }
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UserGoodsBean.SearchListBean>) {
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, UserGoodsBean.SearchListBean>) {
        networkState.postValue(NetworkState.LOADING)
        initState.postValue(NetworkState.LOADING)
        MainRepository.INSTANCE.getUserGoods(size = params.requestedLoadSize.toString(), page = "1", observer = object : PagingObserver<UserGoodsBean>() {
            override fun onError(e: Throwable) {
                super.onError(e)
                retry = {
                    loadInitial(params, callback)
                }
                val error = NetworkState.error(e.message
                        ?: "unknown error")
                networkState.postValue(error)
                initState.postValue(error)
            }

            override fun onNext(t: UserGoodsBean) {
                //更新状态
                val listData = t.searchList
                retry = null
                networkState.postValue(NetworkState.LOADED)
                if (!listData.isNullOrEmpty()) {
                    initState.postValue(NetworkState.LOADED)
                    callback.onResult(listData, t.prevPage, t.nextPage)
                } else {
                    initState.postValue(NetworkState.EMPTY)
                }
            }
        })
    }
}