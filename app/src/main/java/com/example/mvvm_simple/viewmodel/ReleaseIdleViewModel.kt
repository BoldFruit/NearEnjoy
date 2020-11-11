package com.example.mvvm_simple.viewmodel

import android.text.style.UpdateLayout
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.load.MultiTransformation
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.base.view.activity.MvvmNetworkActivity
import com.example.base.viewmodel.MvvmNetworkViewModel
import com.example.mvvm_simple.model.bean.PublicGoodsReqBean
import com.example.mvvm_simple.model.bean.UploadFileBean
import com.example.mvvm_simple.model.models.PublicGoodsModel
import com.example.mvvm_simple.model.models.UploadFileModel
import java.io.File

/**
 * @author DuLong
 * @since 2020/4/19
 * email 798382030@qq.com
 */
class ReleaseIdleViewModel: MvvmNetworkViewModel() {
    val goodsName = MutableLiveData<String>()
    val goodsDetail = MutableLiveData<String>()
    val price = MutableLiveData<String>("0.00")
    val number = MutableLiveData<String>()
    //上传图片返回得url
    lateinit var picUrl : MutableLiveData<String>

    override fun initModels() {
        registerModel(UploadFileModel.TAG, UploadFileModel())
        registerModel(PublicGoodsModel.TAG, PublicGoodsModel())
        picUrl = getDataLiveData(UploadFileModel.TAG)
    }

    /**
     * 上传文件
     */
    fun uploadFile(bean: File) {
        (mModelMap[UploadFileModel.TAG] as UploadFileModel).initParam(bean)
        getCachedDataAndLoad(UploadFileModel.TAG)
    }

    /**
     * 上传商品
     */
    fun uploadGoods(body: PublicGoodsReqBean) {
        (mModelMap[PublicGoodsModel.TAG] as PublicGoodsModel).initParam(body)
        getCachedDataAndLoad(PublicGoodsModel.TAG)
    }

}