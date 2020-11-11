package com.example.mvvm_simple.model

import com.example.mvvm_simple.model.bean.*
import com.example.network.HttpClient
import com.example.network.observer.BaseObserver
import io.reactivex.Observer
import okhttp3.MultipartBody

/**
 * @author DuLong
 * @since 2020/4/6
 * email 798382030@qq.com
 */
class MainRepository private constructor(val api: Api){


    //静态内部类的实现
    private object SearchRepositoryHolder {
        val holder = MainRepository(api = HttpClient.getInstance().createService(Api::class.java))
    }

    companion object {
        val INSTANCE = SearchRepositoryHolder.holder;
    }

    fun getRotationCharts(schoolId: String, observer: Observer<List<RotationChartsBean>>) {
        HttpClient.getInstance().apiSubscribe(api.getRotationCharts(schoolId), observer)
    }

    fun getClassifications(observer: Observer<List<ClassificationBean>>) {
        HttpClient.getInstance().apiSubscribe(api.getAllClassification(), observer)
    }

    fun getFirstClassification(firstId: Int, page: String, size: String, observer: Observer<FirstClassificationBean>) {
        HttpClient.getInstance().apiSubscribe(api.getFirstClassification(firstId, page, size), observer)
    }

    fun getSecondClassification(secondId: String, page: Int, size: Int, observer: Observer<FirstClassificationBean>) {
        HttpClient.getInstance().apiSubscribe(api.getSecondClassification(secondId, page, size), observer)
    }

    fun getAllLabels(observer: Observer<List<LabelBean>>) {
        HttpClient.getInstance().apiSubscribe(api.getAllLabels(), observer)
    }

    fun getMainCategories(observer: Observer<List<MainCategoryBean>>) {
        HttpClient.getInstance().apiSubscribe(api.getMainCategories(), observer)
    }

    fun getLabelCategories(observer: Observer<List<LabelCategoriesBean>>) {
        HttpClient.getInstance().apiSubscribe(api.getLabelCategories(), observer)
    }

    fun getUserInfo(observer: Observer<UserInfoBean>) {
        HttpClient.getInstance().apiSubscribe(api.getUserInfo(), observer)
    }

    fun getUserGoods(size: String, page: String, observer: Observer<UserGoodsBean>) {
        HttpClient.getInstance().apiSubscribe(api.getUserGoods(page, size), observer)
    }

    fun getGoodsByCategories(page: String, size: String, categoryIds: String, observer: Observer<FirstClassificationBean>) {
        HttpClient.getInstance().apiSubscribe(api.getGoodsByCategory(page, size, categoryIds), observer)
    }

    fun uploadFile(file: MultipartBody.Part, observer: Observer<String>) {
        HttpClient.getInstance().apiSubscribe(api.uploadFile(file), observer)
    }

    fun publicGoods(body: PublicGoodsReqBean, observer: BaseObserver<Int>) {
        HttpClient.getInstance().apiSubscribe(api.publicGoods(body), observer)
    }

    fun updateAvatar(avatar: String, observer: Observer<String>) {
        val data = UpdateAvatarBean()
        data.avatar = avatar
        HttpClient.getInstance().apiSubscribe(api.updateAvatar(data), observer)
    }

    //    companion object {
//        //kotlin双重锁的实现方法
//        val INSTANCE: SearchRepository by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
//            SearchRepository(api = HttpClient.getInstance().createService(Api::class.java))
//        }
//    }

}