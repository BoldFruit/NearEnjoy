package com.example.base.viewmodel;

import android.app.Application;

import com.example.base.model.SuperBaseModel;
import com.rxjava.rxlife.ScopeViewModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

/**
 * Time:2020/4/19 16:51
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public abstract class MvvmRxLifeViewModel extends ScopeViewModel implements IMvvmNetworkViewModel {

    private Map<String, SuperBaseModel> mModelMap;

    public MvvmRxLifeViewModel(@NonNull Application application) {
        super(application);
        mModelMap = new HashMap<>();
        initModels();
    }

    /**
     * 初始化model
     */
    protected abstract void initModels();

    @Override
    public void detachModels() {
        unBindModel();
    }

    @Override
    protected void onCleared() {
        unBindModel();
        super.onCleared();
    }

    private void unBindModel() {
        if (mModelMap != null) {
            Iterator<String> iterator = mModelMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                mModelMap.get(key).cancel();
            }
            mModelMap.clear();
        }
        mModelMap = null;
    }

    protected void registerModel(String key, SuperBaseModel model) {
        if (mModelMap == null) {
            mModelMap = new HashMap<>();
        }
        mModelMap.put(key, model);
    }

    /**
     * 根据map的 key 调用model的加载方法
     * @param key
     */
    protected void getCachedDataAndLoad(String key) {
        if (mModelMap == null) {
            return;
        }
        SuperBaseModel model;
        if ((model = mModelMap.get(key)) != null) {
            model.getCachedDataAndLoad();
        } else {
            throw new IllegalArgumentException("无该key: " + key + "对应的model");
        }
    }


    protected <T> MutableLiveData<T> getDataLiveData(String key) {

        if (mModelMap == null) {
            return null;
        }
        SuperBaseModel model;
        if ((model = mModelMap.get(key)) != null) {
            return model.getModelLiveData();
        } else {
            throw new IllegalArgumentException("无该key: " + key + "对应的model");
        }
    }

    @Override
    public Map<String, SuperBaseModel> getModels() {
        return mModelMap;
    }
}
