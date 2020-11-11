package com.example.mvvm_simple.view.paging.repository

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.example.lib_common.widget.banner.transformer.MultiplePagerScaleInTransformer
import com.example.mvvm_simple.model.bean.UserGoodsBean
import com.example.mvvm_simple.view.paging.Listing
import com.example.mvvm_simple.view.paging.PagingObserver
import com.example.mvvm_simple.view.paging.factory.UserGoodsFactory
import java.util.concurrent.Executor

/**
 * @author DuLong
 * @since 2020/5/1
 * email 798382030@qq.com
 */
class UserGoodRepository(private val executor: Executor) {

    @MainThread
    fun createListing(pageSize: Int, initialLoadKey: Int? = null): Listing<UserGoodsBean.SearchListBean> {
        val factory = UserGoodsFactory(executor)
        val livePagedList = factory.toLiveData(pageSize = pageSize, initialLoadKey = initialLoadKey, fetchExecutor = executor)
        val refreshState = Transformations.switchMap(factory.liveDataSource) {
            it.initState
        }
        return Listing(
                pagedList = livePagedList,
                retry = {factory.liveDataSource.value?.retryFinal()},
                refresh = {factory.liveDataSource.value?.invalidate()},
                refreshState = refreshState,
                networkState = Transformations.switchMap(factory.liveDataSource) {
                    it.networkState
                }

        )

    }
}