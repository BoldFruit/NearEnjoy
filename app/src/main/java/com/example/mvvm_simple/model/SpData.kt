package com.example.mvvm_simple.model

import com.example.lib_common.util.SPUtils
import com.example.mvvm_simple.model.bean.ClassificationBean
import com.google.gson.reflect.TypeToken

/**
 * @author DuLong
 * @since 2020/5/1
 * email 798382030@qq.com
 */
class SpData {
    companion object {
        const val CATEGORY = "classification"

        /**
         * 这里又是个坑
         * 返回的东西很奇怪，要处理一下
         */
        fun getCategory(): List<ClassificationBean> {
            return SPUtils.getDataList(CATEGORY, object : TypeToken<List<ClassificationBean>>() {}.type)
        }
    }
}