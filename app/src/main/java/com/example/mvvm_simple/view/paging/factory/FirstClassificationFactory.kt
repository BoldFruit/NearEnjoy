package com.example.mvvm_simple.view.paging.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.mvvm_simple.model.bean.FirstClassificationBean
import com.example.mvvm_simple.view.paging.datasource.FirstClassificationDataSource
import java.util.concurrent.Executor

/**
 * @author DuLong
 * @since 2020/4/8
 * email 798382030@qq.com
 */
class FirstClassificationFactory(private val firstId: String,
                                 private val executor: Executor) : DataSource.Factory<String, FirstClassificationBean.SearchListBean>() {
    val liveDataSource = MutableLiveData<FirstClassificationDataSource>()

    override fun create(): DataSource<String, FirstClassificationBean.SearchListBean> {
        val dataSource = FirstClassificationDataSource(firstId, executor)
        liveDataSource.postValue(dataSource)
        return dataSource
    }

}