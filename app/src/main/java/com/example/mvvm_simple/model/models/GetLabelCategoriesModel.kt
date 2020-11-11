package com.example.mvvm_simple.model.models

import com.example.base.model.BaseModel
import com.example.mvvm_simple.model.MainRepository
import com.example.mvvm_simple.model.bean.LabelCategoriesBean
import com.example.network.exception.ApiException
import com.example.network.observer.BaseObserver
import java.util.*

/**
 * @author DuLong
 * @since 2020/4/10
 * email 798382030@qq.com
 */
class GetLabelCategoriesModel: BaseModel<List<LabelCategoriesBean>>() {
    companion object {
        const val  TAG = "GetLabelCategoriesModel"
    }

    override fun load() {
        MainRepository.INSTANCE.getLabelCategories(object : BaseObserver<List<LabelCategoriesBean>>(this) {
            override fun onNext(t: List<LabelCategoriesBean>) {
                loadSuccess(t)
            }

            override fun onError(e: ApiException?) {
                loadFail(e?.message)
            }


        })
    }
}