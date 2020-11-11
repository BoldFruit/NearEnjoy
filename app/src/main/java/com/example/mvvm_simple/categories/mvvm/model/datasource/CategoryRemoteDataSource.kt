package com.example.mvvm_simple.categories.mvvm.model.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.mvvm_simple.categories.mvvm.model.CategoriesRepository
import com.example.mvvm_simple.model.bean.FirstClassificationBean
import com.example.mvvm_simple.view.paging.NetworkState
import com.example.network.exception.ApiException
import com.example.network.observer.BaseObserver
import java.util.concurrent.Executor

/**
 * Time:2020/4/13 10:33
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class CategoryRemoteDataSource(
        private val retryExecutor: Executor,
        private val ids: String
) : PageKeyedDataSource<String, FirstClassificationBean.SearchListBean>() {

    private var retry: (() -> Any)? = null
    private var page = 1
    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()
    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry.let {
            retryExecutor.execute(
                    it?.invoke() as Runnable?
            )
        }
    }
    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, FirstClassificationBean.SearchListBean>) {
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)
        CategoriesRepository.getCategoriesDetail(object : BaseObserver<FirstClassificationBean>(null) {
            override fun onNext(t: FirstClassificationBean) {
                retry = null
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                val footerData = FirstClassificationBean.SearchListBean()
                //添加footer
                footerData.id = -1
                t.searchList.add(footerData)
                callback.onResult(t.searchList, t.prevPage.toString(), t.nextPage.toString())
                page++
            }

            override fun onError(e: ApiException?) {
                retry = {
                    loadInitial(params, callback)
                }
                val error = NetworkState.error(e?.msg)
                networkState.postValue(error)
                initialLoad.postValue(error)
            }

        }, "1", params.requestedLoadSize.toString(), ids)
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, FirstClassificationBean.SearchListBean>) {
        networkState.postValue(NetworkState.LOADING)
        CategoriesRepository.getCategoriesDetail(object : BaseObserver<FirstClassificationBean>(null) {
            override fun onNext(t: FirstClassificationBean) {
                networkState.postValue(NetworkState.LOADED)
               if (t.searchList.isNotEmpty()) {
                   val footerData = FirstClassificationBean.SearchListBean()
                   footerData.id = -1
                   //添加footer
                   t.searchList.add(footerData)
                   callback.onResult(t.searchList, t.nextPage.toString())
               }
            }

            override fun onError(e: ApiException?) {
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(NetworkState.error(e?.msg))
            }

        }, page.toString(), params.requestedLoadSize.toString(), ids)
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, FirstClassificationBean.SearchListBean>) {

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
        val footerData = FirstClassificationBean.SearchListBean()
        footerData.id = -1
        goods.add(footerData)
        data.searchList = goods
        data.nextPage = 2
        data.prevPage = 0
        return data
    }

}