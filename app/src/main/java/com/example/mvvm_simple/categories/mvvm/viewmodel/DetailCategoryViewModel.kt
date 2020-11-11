package com.example.mvvm_simple.categories.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.base.viewmodel.MvvmNetworkViewModel
import com.example.mvvm_simple.categories.mvvm.model.datasource.CategoryDataSourceFactory
import com.example.mvvm_simple.model.bean.FirstClassificationBean
import com.example.mvvm_simple.view.paging.Listing
import java.util.concurrent.Executors

/**
 * Time:2020/4/12 16:52
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class DetailCategoryViewModel: MvvmNetworkViewModel() {
    var resultList: LiveData<PagedList<FirstClassificationBean.SearchListBean>> = MutableLiveData()
    var ids: MutableLiveData<String> = MutableLiveData()
    //用于网络请求的线程池
    private val executor = Executors.newFixedThreadPool(5)
    override fun initModels() {

    }
    fun createData(): Listing<FirstClassificationBean.SearchListBean> {
        val factory = CategoryDataSourceFactory(executor, ids.value!!)
        val refreshState = Transformations.switchMap(factory.sourceLiveData) {
            it.initialLoad
        }
        resultList = factory.toLiveData(pageSize = 20,
                fetchExecutor = executor)
        return Listing(
                pagedList = resultList,
                refreshState = refreshState,
                refresh = {factory.sourceLiveData.value?.invalidate()},
                retry = {factory.sourceLiveData.value?.retryAllFailed()},
                networkState = Transformations.switchMap(factory.sourceLiveData) {
                    it.networkState
                }
        )
    }
}