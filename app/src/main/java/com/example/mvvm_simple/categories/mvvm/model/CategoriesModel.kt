package com.example.mvvm_simple.categories.mvvm.model

import com.example.base.model.BaseModel
import com.example.lib_common.linkage_kt.data.LinkData
import com.example.network.exception.ApiException
import com.example.network.observer.BaseObserver

/**
 * Time:2020/4/10 15:45
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
open class CategoriesModel: BaseModel<ArrayList<LinkData>>() {

    companion object {
        val TAG: String = "CategoriesModel"
    }
    override fun load() {
        CategoriesRepository.getCategoriesList(object : BaseObserver<ArrayList<LinkData>>(this) {
            override fun onNext(t: ArrayList<LinkData>) {
                loadSuccess(t)
            }

            override fun onError(e: ApiException?) {
                loadFail(e?.message)
            }

        })
    }
}