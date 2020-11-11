package com.example.mvvm_simple.view.paging.factory

import androidx.lifecycle.MutableLiveData
import com.example.mvvm_simple.model.bean.UserGoodsBean
import com.example.mvvm_simple.view.paging.datasource.UserGoodsDataSource
import java.util.concurrent.Executor
import javax.sql.DataSource

/**
 * @author DuLong
 * @since 2020/5/1
 * email 798382030@qq.com
 */
class UserGoodsFactory(private val executor: Executor): androidx.paging.DataSource.Factory<Int, UserGoodsBean.SearchListBean>() {
    val liveDataSource = MutableLiveData<UserGoodsDataSource>()

    override fun create(): androidx.paging.DataSource<Int, UserGoodsBean.SearchListBean> {
        val dataSource = UserGoodsDataSource(executor)
        liveDataSource.postValue(dataSource)
        return dataSource
    }
}