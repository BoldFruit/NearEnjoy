package com.example.mvvm_simple.model.models

import com.example.base.model.BaseModel
import com.example.mvvm_simple.model.MainRepository
import com.example.mvvm_simple.model.bean.RotationChartsBean
import com.example.network.exception.ApiException
import com.example.network.observer.BaseObserver
import io.reactivex.Observer

/**
 * @author DuLong
 * @since 2020/4/6
 * email 798382030@qq.com
 */
class GetRotationChartsModel: BaseModel<List<RotationChartsBean>>(){
    companion object {
        const val TAG = "RotationChartsModel"
    }

    private lateinit var mId: String

    fun initParam(id: String) {
        mId = id
    }

    override fun load() {
        MainRepository.INSTANCE.getRotationCharts(mId, observer = object : BaseObserver<List<RotationChartsBean>>(this) {
            override fun onError(e: ApiException?) {
                loadFail(e.toString())
            }

            override fun onNext(t: List<RotationChartsBean>) {
                loadSuccess(t)
            }

        })
    }
}