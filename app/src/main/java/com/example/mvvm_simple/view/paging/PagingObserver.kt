package com.example.mvvm_simple.view.paging

import com.example.base.BaseApplication
import com.example.base.utils.ToastUtil
import com.example.network.exception.ApiException
import com.example.network.exception.ExceptionType
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @author DuLong
 * @since 2020/4/8
 * email 798382030@qq.com
 */
abstract class PagingObserver<T> : Observer<T> {
    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onError(e: Throwable) {
        val apiException: ApiException = if (e is ApiException) {
            e
        } else {
            ApiException(e, ExceptionType.UNKNOWN)
        }
        // TODO: 这儿能不能用BaseApplication的Application
        // error things
        ToastUtil.show(BaseApplication.sApplication, apiException.msg)
    }

    override fun onNext(t: T) {
    }
}