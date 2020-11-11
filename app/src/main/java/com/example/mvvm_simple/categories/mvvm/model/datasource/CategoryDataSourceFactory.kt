package com.example.mvvm_simple.categories.mvvm.model.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.mvvm_simple.model.bean.FirstClassificationBean
import java.util.concurrent.Executor

/**
 * Time:2020/4/13 15:33
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class CategoryDataSourceFactory(
        private val retryExecutor: Executor,
        private val ids: String
) : DataSource.Factory<String, FirstClassificationBean.SearchListBean>() {
   val sourceLiveData = MutableLiveData<CategoryRemoteDataSource>()
    override fun create(): DataSource<String, FirstClassificationBean.SearchListBean> {
        val source = CategoryRemoteDataSource(retryExecutor, ids)
        sourceLiveData.postValue(source)
        return source
    }
}