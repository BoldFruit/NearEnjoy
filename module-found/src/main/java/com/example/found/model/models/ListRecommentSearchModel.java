package com.example.found.model.models;

import java.util.List;

import com.example.base.model.BaseModel;
import com.example.found.model.SearchRepository;
import com.example.found.model.bean.ListRecommendResponse;
import com.example.network.exception.ApiException;
import com.example.network.observer.BaseObserver;

/**
 * @author DuLong
 * @since 2020/2/25
 * email 798382030@qq.com
 */
public class ListRecommentSearchModel extends BaseModel<List<ListRecommendResponse>> {

    public static final String TAG = "ListRecommentSearchModel";
    private String mQuery;
    private String mNum;


    public void setParameter(String mQuery, String mNum) {
        this.mNum = mNum;
        this.mQuery = mQuery;
    }

    @Override
    protected void load() {
        SearchRepository.getInstance().listSearchRecommend(mQuery, mNum, new BaseObserver<List<ListRecommendResponse>>(this) {
            @Override
            public void onError(ApiException e) {
                loadFail(e.getMessage());
            }

            @Override
            public void onNext(List<ListRecommendResponse> mListRecommendResponses) {
                loadSuccess(mListRecommendResponses);
            }
        });
    }
}
