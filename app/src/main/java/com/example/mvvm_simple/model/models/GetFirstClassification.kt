package com.example.mvvm_simple.model.models

import com.example.base.model.BaseModel
import com.example.mvvm_simple.model.bean.FirstClassificationBean

/**
 * @author DuLong
 * @since 2020/4/6
 * email 798382030@qq.com
 */
class GetFirstClassification: BaseModel<FirstClassificationBean>() {
    companion object {
        const val TAG = "FirstClassificationModel"
    }
    override fun load() {
        TODO("Not yet implemented")
    }
}