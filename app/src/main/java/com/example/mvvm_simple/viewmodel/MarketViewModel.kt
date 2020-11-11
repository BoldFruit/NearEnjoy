package com.example.mvvm_simple.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.base.viewmodel.MvvmNetworkViewModel
import com.example.mvvm_simple.model.bean.ClassificationBean
import com.example.mvvm_simple.model.bean.LabelCategoriesBean
import com.example.mvvm_simple.model.bean.MainCategoryBean
import com.example.mvvm_simple.model.bean.RotationChartsBean
import com.example.mvvm_simple.model.models.*

/**
 * @author DuLong
 * @since 2020/4/6
 * email 798382030@qq.com
 */
class MarketViewModel: MvvmNetworkViewModel() {
    lateinit var rotationCharts : MutableLiveData<ArrayList<RotationChartsBean>>
    lateinit var mainCategories : MutableLiveData<List<MainCategoryBean>>
    lateinit var labelCategories: MutableLiveData<List<LabelCategoriesBean>>
    lateinit var classification: MutableLiveData<List<ClassificationBean>>

    override fun initModels() {
        registerModel(GetRotationChartsModel.TAG, GetRotationChartsModel())
        registerModel(GetClassificationsModel.TAG, GetClassificationsModel())
        registerModel(GetFirstClassification.TAG, GetFirstClassification())
        registerModel(GetSecondClassification.TAG, GetSecondClassification())
        registerModel(GetAllLabelModel.TAG, GetAllLabelModel())
        registerModel(GetMainCategoriesModel.TAG, GetMainCategoriesModel())
        registerModel(GetLabelCategoriesModel.TAG, GetLabelCategoriesModel())

        rotationCharts = getDataLiveData(GetRotationChartsModel.TAG)
        mainCategories = getDataLiveData(GetMainCategoriesModel.TAG)
        labelCategories = getDataLiveData(GetLabelCategoriesModel.TAG)
        classification = getDataLiveData(GetClassificationsModel.TAG)
    }

    fun getRotationCharts(schoolId: String) {
        (mModelMap[GetRotationChartsModel.TAG] as GetRotationChartsModel).initParam(schoolId)
        getCachedDataAndLoad(GetRotationChartsModel.TAG)
    }

    //获取所有的分类
    fun getClassification() {
        getCachedDataAndLoad(GetClassificationsModel.TAG)
    }

    fun getAllLabels() {
        getCachedDataAndLoad(GetAllLabelModel.TAG)
    }

    fun getMainCategories() {
        getCachedDataAndLoad(GetMainCategoriesModel.TAG)
    }

    fun getLabelCategories() {
        getCachedDataAndLoad(GetLabelCategoriesModel.TAG)
    }


}