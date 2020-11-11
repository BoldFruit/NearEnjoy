package com.example.mvvm_simple.model.models

import com.example.base.model.BaseModel
import com.example.mvvm_simple.model.MainRepository
import com.example.network.exception.ApiException
import com.example.network.observer.BaseObserver

/**
 * @author DuLong
 * @since 2020/5/8
 * email 798382030@qq.com
 */
class UpdateAvatarModel: BaseModel<String>() {
    companion object {
        const val TAG = "UpdateAvatarModel"
    }
    lateinit var url: String
    fun initParam(url: String) {
        this.url = url
    }
    override fun load() {
        MainRepository.INSTANCE.updateAvatar(url, object : BaseObserver<String>(this) {
            override fun onError(e: ApiException?) {
                loadFail(e?.message)
            }

            override fun onNext(t: String) {
                loadSuccess(t)
            }

        })
    }
}