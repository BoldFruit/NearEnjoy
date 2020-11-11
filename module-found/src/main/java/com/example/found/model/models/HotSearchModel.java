package com.example.found.model.models;

import com.example.found.model.SearchRepository;

import java.util.List;

import com.example.base.model.BaseModel;
import com.example.found.model.bean.NewTopSearchResponseBean;
import com.example.found.model.bean.TopSearchResponseBean;
import com.example.network.exception.ApiException;
import com.example.network.observer.BaseObserver;


/**
 * @author DuLong
 * @since 2020/2/25
 * email 798382030@qq.com
 */
public class HotSearchModel extends BaseModel<NewTopSearchResponseBean> {
    public static final String TAG = "HotSearchModel";
    private String schoolId;

    public void setParameter(String schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    protected void load() {
        SearchRepository.getInstance().getHotSearch(schoolId, new BaseObserver<NewTopSearchResponseBean>(this) {
            @Override
            public void onError(ApiException e) {
                loadFail(e.toString());
            }

            @Override
            public void onNext(NewTopSearchResponseBean mNewTopSearchResponseBean) {
                loadSuccess(mNewTopSearchResponseBean);
            }
        });
    }
}
