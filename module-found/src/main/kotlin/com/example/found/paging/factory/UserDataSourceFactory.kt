package com.example.found.paging.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.found.model.bean.SearchGoodsResponse
import com.example.found.model.bean.SearchUserResponse
import com.example.found.paging.SearchApi
import com.example.found.paging.datasource.UserDataSource
import java.util.concurrent.Executor

/**
 * @author DuLong
 * @since 2020/3/27
 * email 798382030@qq.com
 */
class UserDataSourceFactory(
        private val name: String,
        private val retryExecutor: Executor) : androidx.paging.DataSource.Factory<String, SearchUserResponse>(){
    val sourceLiveData = MutableLiveData<UserDataSource>()

    override fun create(): DataSource<String, SearchUserResponse> {
        val dataSource = UserDataSource(name, retryExecutor)
        sourceLiveData.postValue(dataSource)
        return dataSource
    }

}