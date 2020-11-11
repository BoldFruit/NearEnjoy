package com.example.mvvm_simple.model.models

import android.nfc.Tag
import com.example.base.model.BaseModel
import com.example.mvvm_simple.model.bean.SecondClassificationBean

/**
 * @author DuLong
 * @since 2020/4/6
 * email 798382030@qq.com
 */
class GetSecondClassification: BaseModel<SecondClassificationBean>() {

    companion object {
        val TAG = "SecondClassificationModel"
    }
    override fun load() {
        TODO("Not yet implemented")
    }
}