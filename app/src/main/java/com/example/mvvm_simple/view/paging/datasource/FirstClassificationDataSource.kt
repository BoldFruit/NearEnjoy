package com.example.mvvm_simple.view.paging.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.mvvm_simple.model.MainRepository
import com.example.mvvm_simple.model.bean.FirstClassificationBean
import com.example.mvvm_simple.view.paging.NetworkState
import com.example.mvvm_simple.view.paging.PagingObserver
import java.util.concurrent.Executor

/**
 * @author DuLong
 * @since 2020/4/8
 * email 798382030@qq.com
 */
class FirstClassificationDataSource(private val categoryId: String, private val retryExecutor: Executor) : PageKeyedDataSource<String, FirstClassificationBean.SearchListBean>(){

    private var retry: (() -> Any)? = null

    var networkState = MutableLiveData<NetworkState>(NetworkState.LOADED)

    var initState = MutableLiveData<NetworkState>()

    private var mPage = 1
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

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, FirstClassificationBean.SearchListBean>) {
        networkState.postValue(NetworkState.LOADING);
            MainRepository.INSTANCE.getGoodsByCategories(categoryIds = categoryId, page = params.key, size = params.requestedLoadSize.toString(), observer = object : PagingObserver<FirstClassificationBean>() {
                override fun onNext(t: FirstClassificationBean) {
                    //更新状态
                    val listData = t.searchList
                    retry = null;
                    networkState.postValue(NetworkState.LOADED)
                    if (listData.isNullOrEmpty()) {
                        callback.onResult(listData, t.nextPage.toString())
                    }
//                    callback.onResult(getFadeData2().searchList, "1")
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    retry = {
                        loadAfter(params, callback)
                    }
                    val error = NetworkState.error(e.message
                            ?: "unknown error")
                    networkState.postValue(error)

                }
            })
        }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, FirstClassificationBean.SearchListBean>) {
    }

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, FirstClassificationBean.SearchListBean>) {
        networkState.postValue(NetworkState.LOADING)
        initState.postValue(NetworkState.LOADING)
        MainRepository.INSTANCE.getGoodsByCategories(categoryIds = categoryId, page = mPage.toString(), size = params.requestedLoadSize.toString(), observer = object : PagingObserver<FirstClassificationBean>() {
            override fun onNext(t: FirstClassificationBean) {
                //更新状态
                val listData = t.searchList
                retry = null;
                networkState.postValue(NetworkState.LOADED)
                    if (!listData.isNullOrEmpty()) {
                        initState.postValue(NetworkState.LOADED)
                        callback.onResult(listData, t.prevPage.toString(), t.nextPage.toString())
                    } else {
                        initState.postValue(NetworkState.EMPTY)
                    }
//                initState.postValue(NetworkState.LOADED)
//                callback.onResult(getFadeData2().searchList, null, getFadeData2().nextPage.toString())
            }

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
        })
    }


    fun getFadeData2() : FirstClassificationBean {
        var goods: MutableList<FirstClassificationBean.SearchListBean> = MutableList(10) { index ->
            var item = FirstClassificationBean.SearchListBean()
            item.wants = index
            item.name = "大果子"
            item.labelIds = "[\"2\",\"3\"]"
            item.price = 100.00f
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
        var data: FirstClassificationBean = FirstClassificationBean()
        data.searchList = goods
        data.nextPage = 2
        data.prevPage = 0
        return data
    }

}