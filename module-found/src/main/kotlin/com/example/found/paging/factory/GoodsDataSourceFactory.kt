package com.example.found.paging.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.found.model.bean.SearchGoodsResponse
import com.example.found.paging.SearchApi
import com.example.found.paging.datasource.GoodsDataSource
import java.util.concurrent.Executor
import javax.sql.DataSource
import kotlin.math.max


/**
 * @author DuLong
 * @since 2020/3/14
 * email 798382030@qq.com
 */
/**
 * 用于创建dataSource的类
 */
class GoodsDataSourceFactory(
        private val name: String,
        private val sortMode: String?,
        private val labels: List<Int>?,
        private val minPrice: Int?,
        private val maxPrice: Int?,
        private val retryExecutor: Executor) : androidx.paging.DataSource.Factory<String, SearchGoodsResponse.SearchListBean>(){
    val sourceLiveData = MutableLiveData<GoodsDataSource>()

    /**
     * 用于创建datasource的方法
     */
    override fun create(): GoodsDataSource {
        val dataSource = GoodsDataSource(name, sortMode, labels, minPrice, maxPrice, retryExecutor)
        sourceLiveData.postValue(dataSource)
        return dataSource
    }
}