package com.example.mvvm_simple.viewmodel

import com.example.base.viewmodel.MvvmNetworkViewModel
import com.example.mvvm_simple.categories.mvvm.model.CategoriesModel

/**
 * Time:2020/4/10 15:35
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class CategoriesViewModel: MvvmNetworkViewModel() {

    override fun initModels() {
        registerModel(CategoriesModel.TAG, CategoriesModel())
    }

    fun loadLinkList() {
        getCachedDataAndLoad(CategoriesModel.TAG)
    }

}