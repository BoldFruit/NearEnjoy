package com.example.mvvm_simple.goods_detail.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.base.viewmodel.MvvmRxLifeViewModel
import com.example.mvvm_simple.goods_detail.model.GoodsDetailBean
import com.example.mvvm_simple.goods_detail.model.GoodsDetailRepository
import com.example.network.exception.ApiException
import com.example.network.observer.BaseObserver
import org.json.JSONArray

/**
 * Time:2020/4/18 16:45
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class GoodsDetailViewModel(application: Application) : MvvmRxLifeViewModel(application) {
    enum class RequestType {
        LOADING,
        DONE,
        FAILED
    }
    var isLoading: MutableLiveData<RequestType> = MutableLiveData(RequestType.LOADING)
    var responseBean: MutableLiveData<GoodsDetailBean> = MutableLiveData()
    var imagesList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    var tagsDataList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    override fun initModels() {

    }

    fun requestDetail(id: String) {

        GoodsDetailRepository.getDetail(id, object : BaseObserver<GoodsDetailBean>(null) {
            override fun onNext(t: GoodsDetailBean) {
                isLoading.value = RequestType.DONE
                responseBean.value = t

                val tagsList = ArrayList<String>()
                val imageList = ArrayList<String>()
                if (!t.labelIds.isNullOrEmpty()) {
                    val jsonArray = JSONArray(t.labelIds)
                    for (i in 0 until jsonArray.length()) {
                        tagsList.add(jsonArray[i].toString())
                    }
                    tagsDataList.value = tagsList
                }

                if (!t.images.isNullOrEmpty()) {
                    val jsonArray1 = JSONArray(t.images)
                    for (i in 0 until jsonArray1.length()) {
                        imageList.add(jsonArray1[i].toString())
                    }

                    imagesList.value = imageList
                }


            }

            override fun onError(e: ApiException?) {
                isLoading.value = RequestType.FAILED
            }

        }, this)

    }

}