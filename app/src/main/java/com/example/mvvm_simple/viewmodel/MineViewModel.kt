package com.example.mvvm_simple.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.base.viewmodel.MvvmNetworkViewModel
import com.example.mvvm_simple.model.bean.UserGoodsBean
import com.example.mvvm_simple.model.bean.UserInfoBean
import com.example.mvvm_simple.model.models.*
import java.io.File

/**
 * @author DuLong
 * @since 2020/4/14
 * email 798382030@qq.com
 *
 */
class MineViewModel: MvvmNetworkViewModel() {
    lateinit var mUserInfo: MutableLiveData<UserInfoBean>
    lateinit var mUserGoods: MutableLiveData<UserGoodsBean>
    lateinit var mFileUrl: MutableLiveData<String>

    override fun initModels() {
        registerModel(GetUserInfoModel.TAG, GetUserInfoModel())
        registerModel(GetUserGoodsModel.TAG, GetUserGoodsModel())
        registerModel(UploadFileModel.TAG, UploadFileModel())
        registerModel(UpdateAvatarModel.TAG, UpdateAvatarModel())

        mUserInfo = getDataLiveData(GetUserInfoModel.TAG)
        mUserGoods = getDataLiveData(GetUserGoodsModel.TAG)
        mFileUrl = getDataLiveData(UploadFileModel.TAG)
    }

    fun getUserInfo() {
        getCachedDataAndLoad(GetUserInfoModel.TAG)
    }

    fun getUserGoodsInfo(page: String, size: String) {
        (mModelMap[GetUserGoodsModel.TAG] as GetUserGoodsModel).initParam(page = page, size = size)
        getCachedDataAndLoad(GetUserGoodsModel.TAG)
    }

    /**
     * 上传文件
     */
    fun uploadFile(file: File) {
        (mModelMap[UploadFileModel.TAG] as UploadFileModel).initParam(file)
        getCachedDataAndLoad(UploadFileModel.TAG)
    }

    /**
     * 更新头像
     */
    fun updateAvatar(url: String) {
        (mModelMap[UpdateAvatarModel.TAG] as UpdateAvatarModel).initParam(url)
        getCachedDataAndLoad(UpdateAvatarModel.TAG)
    }

}