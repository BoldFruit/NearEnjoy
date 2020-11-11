package com.example.login.new_part

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.base.BaseApplication
import com.example.base.viewmodel.MvvmRxLifeViewModel
import com.example.lib_common.linkage.multi_link.thr_link.SchoolData
import com.example.lib_data.bean.LoginResponseBean
import com.example.login.api.LoginRepository
import com.example.network.exception.ApiException
import com.example.network.observer.BaseObserver

/**
 * Time:2020/5/6 7:30
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class QuestionViewModel(application: Application) : MvvmRxLifeViewModel(application) {

    enum class LoadingType {
        LOADING,
        DONE,
        FAILED
    }

    var isLoading: MutableLiveData<LoadingType>
            = MutableLiveData(LoadingType.LOADING)
    var result: MutableLiveData<List<QuestionBean>>
            = MutableLiveData()
    var schools: MutableLiveData<List<SchoolData>> = MutableLiveData()
    private var schoolId = "1"
    private var page = "1"
    private var size = "3"
    override fun initModels() {

    }

    fun getQuestions() {
        isLoading.value = LoadingType.LOADING
        LoginRepository.getInstance().getQuestions(schoolId, page, size, object : BaseObserver<List<QuestionBean>>(null) {
            override fun onNext(t: List<QuestionBean>) {
                isLoading.value = LoadingType.DONE
                result.value = t
            }

            override fun onError(e: ApiException?) {
                isLoading.value = LoadingType.FAILED
            }

        }, this)
    }

    fun getSchools() {
        LoginRepository.getInstance().getSchools(object : BaseObserver<List<SchoolData>>(null) {
            override fun onNext(t: List<SchoolData>) {
                schools.value = t
            }

            override fun onError(e: ApiException?) {

            }

        }, this)
    }

}