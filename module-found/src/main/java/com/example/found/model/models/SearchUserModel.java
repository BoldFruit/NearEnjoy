package com.example.found.model.models;

import java.util.List;

import com.example.base.model.BaseModel;
import com.example.found.model.SearchRepository;
import com.example.found.model.bean.SearchUserResponse;
import com.example.network.exception.ApiException;
import com.example.network.observer.BaseObserver;

/**
 * @author DuLong
 * @since 2020/2/25
 * email 798382030@qq.com
 */
public class SearchUserModel extends BaseModel<List<SearchUserResponse>> {

    public static final String TAG = "SearchUserModel";

    private String mName;
    private String mPage;
    private String mSize;

    public void setParameter(String mName, String mPage, String mSize) {
        this.mName = mName;
        this.mPage = mPage;
        this.mSize = mSize;
    }

    @Override
    protected void load() {
        SearchRepository.getInstance().searchUsers(mName, mPage, mSize, new BaseObserver<List<SearchUserResponse>>(this) {
            @Override
            public void onError(ApiException e) {
                loadFail(e.toString());
            }

            @Override
            public void onNext(List<SearchUserResponse> mSearchUserResponses) {
                loadSuccess(mSearchUserResponses);
            }
        });
    }
}
