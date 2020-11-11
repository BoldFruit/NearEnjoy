package com.example.mvvm_simple.categories.mvvm.viewmodel

import com.example.base.viewmodel.MvvmNetworkViewModel
import com.example.mvvm_simple.categories.mvvm.model.CategoriesModel

/**
 * Time:2020/4/12 16:51
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class CategoriesViewModel: MvvmNetworkViewModel() {

    override fun initModels() {
        registerModel(CategoriesModel.TAG, CategoriesModel())
    }

    fun getCategoriesData() {
        getCachedDataAndLoad(CategoriesModel.TAG)
    }

}