package com.example.found.paging.repository

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.example.found.model.bean.SearchGoodsResponse
import com.example.found.paging.Listing
import com.example.found.paging.factory.GoodsDataSourceFactory
import java.util.concurrent.Executor

/**
 * @author DuLong
 * @since 2020/3/28
 * email 798382030@qq.com
 */
/**
 * 将pagedList转化为Listing,
 */
class GoodsRepository(private val networkExecutor: Executor) {

    @MainThread
    fun createListing(name: String,
                      pageSize: Int,
                      sortMode: String?,
                      labels: List<Int>?,
                      minPrice: Int?,
                      maxPrice: Int?): Listing<SearchGoodsResponse.SearchListBean> {
        val sourceFactory = GoodsDataSourceFactory(name = name, sortMode = sortMode, labels = labels,
                minPrice = minPrice, maxPrice = maxPrice, retryExecutor = networkExecutor)
        val livePagedList = sourceFactory.toLiveData(pageSize = pageSize, fetchExecutor = networkExecutor)
        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initState
        }
        return Listing(
                pagedList = livePagedList,
                refreshState = refreshState,
                refresh = { sourceFactory.sourceLiveData.value?.invalidate() },
                retry = {sourceFactory.sourceLiveData.value?.retryFinal()},
                networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                    it.networkState
                }
        )
    }
}