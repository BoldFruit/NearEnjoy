package com.example.mvvm_simple.model.models

import androidx.databinding.BaseObservable
import com.example.base.model.BaseModel
import com.example.mvvm_simple.model.MainRepository
import com.example.mvvm_simple.model.bean.UserGoodsBean
import com.example.network.HttpClient
import com.example.network.exception.ApiException
import com.example.network.observer.BaseObserver

/**
 * @author DuLong
 * @since 2020/4/14
 * email 798382030@qq.com
 */
class GetUserGoodsModel: BaseModel<UserGoodsBean>() {

    companion object {
        const val TAG = "GetUserGoodsModel"
    }

    private lateinit var size: String
    private lateinit var page: String

    fun initParam(size: String, page: String) {
        this.size = size
        this.page = page
    }
    override fun load() {
        MainRepository.INSTANCE.getUserGoods(size = size, page = page, observer = object : BaseObserver<UserGoodsBean>(this) {
            override fun onNext(t: UserGoodsBean) {
                loadSuccess(t)
            }

            override fun onError(e: ApiException?) {
                loadFail(e?.message)
            }
        })
    }

}