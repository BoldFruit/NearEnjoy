package com.example.mvvm_simple.model.models

import com.example.base.model.BaseModel
import com.example.mvvm_simple.model.MainRepository
import com.example.mvvm_simple.model.bean.MainCategoryBean
import com.example.network.exception.ApiException
import com.example.network.observer.BaseObserver

/**
 * @author DuLong
 * @since 2020/4/10
 * email 798382030@qq.com
 */
class GetMainCategoriesModel: BaseModel<List<MainCategoryBean>>() {
    companion object {
        const val  TAG = "GetMainCategoriesModel"
    }

    override fun load() {
        MainRepository.INSTANCE.getMainCategories(object : BaseObserver<List<MainCategoryBean>>(this) {
            override fun onNext(t: List<MainCategoryBean>) {
                loadSuccess(t)
            }

            override fun onError(e: ApiException?) {
                loadFail(e?.message)
            }

        })
    }
}