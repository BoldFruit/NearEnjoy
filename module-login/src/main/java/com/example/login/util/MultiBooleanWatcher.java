package com.example.login.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

/**
 * Time:2020/2/24 14:48
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:对多个LiveData的布尔值进行监听
 */
public class MultiBooleanWatcher {

    private MediatorLiveData<Boolean> mediatorLiveData;
    private List<LiveData<Boolean>> mutableLiveDataList;

    public MultiBooleanWatcher() {
        mediatorLiveData = new MediatorLiveData<>();
        mutableLiveDataList = new ArrayList<>();
    }

    /**
     * 对多个LiveData进行监听，判断是否所有的LiveData的值都为false
     * (逻辑有可能有问题，大佬如果看到了多担待，帮忙改改）
     * @param isEdtNullDataList
     */
    public void setWatcher(boolean value, LiveData<Boolean>...isEdtNullDataList) {
        //true代表一个Edit为空
        mutableLiveDataList.addAll(Arrays.asList(isEdtNullDataList));
        for (LiveData<Boolean> liveData: isEdtNullDataList) {
            mediatorLiveData.addSource(liveData, aBoolean -> {
                for (LiveData<Boolean> mData : isEdtNullDataList) {
                   if (mData.getValue() != value) {
                       mediatorLiveData.setValue(false);
                       break;
                   } else {
                       mediatorLiveData.setValue(true);
                   }
                }
            });
        }
    }

    public LiveData<Boolean> getWatcherLiveData() {
        return mediatorLiveData;
    }

}
