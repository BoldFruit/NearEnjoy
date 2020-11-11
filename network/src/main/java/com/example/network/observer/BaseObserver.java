package com.example.network.observer;

import com.example.base.BaseApplication;
import com.example.base.model.SuperBaseModel;
import com.example.base.utils.ToastUtil;
import com.example.network.exception.ApiException;
import com.example.network.exception.ExceptionType;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author YangZhaoxin.
 * @since 2020/2/7 19:34.
 * email yangzhaoxin@hrsoft.net.
 * Function
 */

public abstract class BaseObserver<T> implements Observer<T> {

    private SuperBaseModel mBaseModel;

    public BaseObserver(SuperBaseModel baseModel) {
        mBaseModel = baseModel;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (mBaseModel != null) {
            mBaseModel.addDisposable(d);
        }
    }

    @Override
    public void onError(Throwable e) {
        ApiException apiException;
        if (e instanceof ApiException) {
            apiException = (ApiException) e;
        } else {
            apiException = new ApiException(e, ExceptionType.UNKNOWN);
        }
        // TODO: 这儿能不能用BaseApplication的Application
        // error things
        ToastUtil.show(BaseApplication.sApplication, apiException.getMsg());
        onError(apiException);
    }

    @Override
    public void onComplete() {}

    public abstract void onError(ApiException e);
}
