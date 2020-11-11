package com.example.mvvm_simple.model.models

import com.example.base.model.BaseModel
import com.example.mvvm_simple.model.MainRepository
import com.example.mvvm_simple.model.bean.PublicGoodsReqBean
import com.example.network.exception.ApiException
import com.example.network.observer.BaseObserver

/**
 * @author DuLong
 * @since 2020/4/29
 * email 798382030@qq.com
 */
class PublicGoodsModel: BaseModel<Int>() {
    companion object {
        const val TAG = "PublicGoodsModel"
    }

    private lateinit var body: PublicGoodsReqBean

    fun initParam(param: PublicGoodsReqBean) {

        body = param
    }
    override fun load() {
        MainRepository.INSTANCE.publicGoods(body = body, observer = object : BaseObserver<Int>(this) {
            override fun onNext(t: Int) {
              loadSuccess(t)
            }

            override fun onError(e: ApiException?) {
                loadFail(e?.message)
            }

        })

    }
}