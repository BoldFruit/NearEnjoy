package com.example.mvvm_simple.model.models

import com.example.base.model.BaseModel
import com.example.mvvm_simple.model.MainRepository
import com.example.mvvm_simple.model.bean.MainCategoryBean
import com.example.mvvm_simple.model.bean.UserInfoBean
import com.example.network.exception.ApiException
import com.example.network.observer.BaseObserver

/**
 * @author DuLong
 * @since 2020/4/14
 * email 798382030@qq.com
 */
class GetUserInfoModel: BaseModel<UserInfoBean>() {
    companion object{
        const val TAG = "GetUserInfoModel"
    }

    override fun load() {
        MainRepository.INSTANCE.getUserInfo(observer = object : BaseObserver<UserInfoBean>(this){
            override fun onError(e: ApiException?) {
                loadFail(e?.message)
            }

            override fun onNext(t: UserInfoBean) {
                loadSuccess(t)
            }

        })
    }
}