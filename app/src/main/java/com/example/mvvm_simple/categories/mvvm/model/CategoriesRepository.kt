package com.example.mvvm_simple.categories.mvvm.model

import com.example.lib_common.linkage_kt.data.LinkData
import com.example.mvvm_simple.model.bean.FirstClassificationBean
import com.example.network.HttpClient
import io.reactivex.Observer
import kotlin.collections.ArrayList

/**
 * Time:2020/4/10 15:54
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
object CategoriesRepository {

    var api: ICategoriesApi = HttpClient.getInstance().createService(ICategoriesApi::class.java)

    fun getCategoriesList(observer: Observer<ArrayList<LinkData>>) {
        HttpClient.getInstance().apiSubscribe(api.getCategories(), observer)
    }

    /**
     * 获得所有的分类详情
     */
    fun getCategoriesDetail(observer: Observer<FirstClassificationBean>, page: String, size: String, ids: String) {
        HttpClient.getInstance().apiSubscribe(api.getCategoriesList(page, size, ids), observer)
    }

}