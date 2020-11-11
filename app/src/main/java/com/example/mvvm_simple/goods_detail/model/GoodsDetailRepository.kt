package com.example.mvvm_simple.goods_detail.model

import com.example.network.HttpClient
import com.rxjava.rxlife.Scope
import com.rxjava.rxlife.life
import io.reactivex.Observer

/**
 * Time:2020/4/19 11:52
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
object GoodsDetailRepository {

    var api = HttpClient.getInstance().createService(GoodsApi::class.java)

    fun getDetail(id: String, observer: Observer<GoodsDetailBean>, scope: Scope) {
        val observable = api.getGoodsDetail(id)
        observable.life(scope)
        HttpClient.getInstance().apiSubscribe(observable, observer)
    }

}