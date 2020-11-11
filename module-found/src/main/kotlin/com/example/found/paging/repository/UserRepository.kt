package com.example.found.paging.repository

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.example.found.model.bean.SearchGoodsResponse
import com.example.found.model.bean.SearchUserResponse
import com.example.found.paging.Listing
import com.example.found.paging.factory.GoodsDataSourceFactory
import com.example.found.paging.factory.UserDataSourceFactory
import java.util.concurrent.Executor

/**
 * @author DuLong
 * @since 2020/3/28
 * email 798382030@qq.com
 */
/**
 * 将pagedList转化为Listing,
 */
class UserRepository(private val networkExecutor: Executor) {

    @MainThread
    fun createListing(name: String, pageSize: Int): Listing<SearchUserResponse> {
        val sourceFactory = UserDataSourceFactory(name = name, retryExecutor = networkExecutor)
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