package com.example.mvvm_simple.model.models

import com.bumptech.glide.util.Util
import com.example.base.model.BaseModel
import com.example.base.model.bean.BaseCachedData
import com.example.lib_common.util.SPUtils
import com.example.lib_data.data_user.token.TokenManager
import com.example.mvvm_simple.model.MainRepository
import com.example.mvvm_simple.model.bean.ClassificationBean
import com.example.network.exception.ApiException
import com.example.network.observer.BaseObserver
import java.lang.StringBuilder

/**
 * @author DuLong
 * @since 2020/4/6
 * email 798382030@qq.com
 */
class GetClassificationsModel: BaseModel<List<ClassificationBean>>() {
    companion object {
        const val TAG = "ClassificationModel"
    }
    override fun load() {
            MainRepository.INSTANCE.getClassifications(object : BaseObserver<List<ClassificationBean>>(this) {
                override fun onError(e: ApiException?) {
                    loadFail(e?.message)
                }

                override fun onNext(t: List<ClassificationBean>) {
                    loadSuccess(t)
                }

            })
    }

    override fun saveDataToPreference(data: BaseCachedData<List<ClassificationBean>>?) {
        val listData = data?.data
        val firstMap = HashMap<Int, ClassificationBean>()
        val secondMap = HashMap<Int, String>()
        val relationMap = HashMap<Int, String>()
        //存储
        listData?.forEach {
            firstMap[it.id] = it
            //以“1，2，3”的形式记录下二级分类的全部内容
            var secondContent = StringBuilder("")
            it.secondList?.forEachIndexed { index, item->
                //存储二级分类id与内容的对应关系
                secondMap[item.id] = item.name
                if (index != it.secondList.size - 1) {
                    secondContent.append("${item.id},")
                } else {
                    secondContent.append("${item.id}")
                }
            }
            relationMap[it.id] = secondContent.toString();
        }
        SPUtils.put("firstClassification", firstMap)
        SPUtils.put("secondClassification", secondMap)
        SPUtils.put("classificationRelation", relationMap)
        if (listData != null) {
            SPUtils.put("classification", listData)
        }
    }

    override fun getCachedPreferenceKey(): String {
        return "classification"
    }
}