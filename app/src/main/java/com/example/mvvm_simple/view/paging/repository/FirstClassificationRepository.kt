package com.example.mvvm_simple.view.paging.repository

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.example.mvvm_simple.model.bean.FirstClassificationBean
import com.example.mvvm_simple.view.paging.Listing
import com.example.mvvm_simple.view.paging.factory.FirstClassificationFactory
import java.util.concurrent.Executor

/**
 * @author DuLong
 * @since 2020/4/8
 * email 798382030@qq.com
 */
class FirstClassificationRepository(private val executor: Executor) {

    @MainThread
    fun createListing(firstId: String, pageSize: Int): Listing<FirstClassificationBean.SearchListBean> {
        val factory = FirstClassificationFactory(firstId, executor)
        val livePagedList = factory.toLiveData(pageSize = pageSize, fetchExecutor = executor)
        val refreshState = Transformations.switchMap(factory.liveDataSource) {
            it.initState
        }
        return Listing(
                pagedList = livePagedList,
                refreshState = refreshState,
                refresh = {factory.liveDataSource.value?.invalidate()},
                retry = {factory.liveDataSource.value?.retryFinal()},
                networkState = Transformations.switchMap(factory.liveDataSource) {
                    it.networkState
                }
        )

    }
}