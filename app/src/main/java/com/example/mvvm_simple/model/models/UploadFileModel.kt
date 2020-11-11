package com.example.mvvm_simple.model.models

import android.database.Observable
import android.text.style.UpdateLayout
import android.util.Log
import com.example.base.model.BaseModel
import com.example.lib_common.widget.banner.transformer.MultiplePagerScaleInTransformer
import com.example.mvvm_simple.model.MainRepository
import com.example.mvvm_simple.model.bean.UploadFileBean
import com.example.network.HttpClient
import com.example.network.exception.ApiException
import com.example.network.observer.BaseObserver
import io.reactivex.Observer
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/**
 * @author DuLong
 * @since 2020/4/27
 * email 798382030@qq.com
 */
class UploadFileModel: BaseModel<String>() {
    companion object {
        const val TAG = "UploadFileModel"
    }
    private lateinit var file : File

    fun initParam(file: File) {
        this.file = file
    }
    override fun load() {
        val body = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val trueBody = MultipartBody.Part.createFormData("image", file.name, body)
        MainRepository.INSTANCE.uploadFile(trueBody, observer = object : BaseObserver<String>(this) {

            override fun onError(e: ApiException?) {
                loadFail(e?.message)
                Log.e("加载失败", e?.message)
            }

            override fun onNext(t: String) {
                loadSuccess(t)
                Log.e("加载成功", t)
            }

        })


    }


}