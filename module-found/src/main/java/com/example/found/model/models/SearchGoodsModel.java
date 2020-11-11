package com.example.found.model.models;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import com.example.base.model.BaseModel;
import com.example.found.model.SearchRepository;
import com.example.found.model.bean.SearchGoodsResponse;
import com.example.network.exception.ApiException;
import com.example.network.observer.BaseObserver;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

/**
 * @author DuLong
 * @since 2020/2/25
 * email 798382030@qq.com
 */
public class SearchGoodsModel extends BaseModel<List<SearchGoodsResponse>> {

    public static final String TAG = "SearchGoodsModel";

    private String mSize;
    private String mName;
    private String mPage;
    private String mSortMode;
    private String[] mLabels;
    private Integer mMinPrice;
    private Integer mMaxPrice;

    /**
     * 搜索模式：综合，默认的模式
     */
    public static final String OVERALL = "overall";
    //最新
    public static final String NEWEST = "newest";
    //价格从低到高
    public static final String MIN_PRICE = "priceAsc";
    //价格从高到低
    public static final String MAX_PRICE = "priceDesc";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({OVERALL, NEWEST, MIN_PRICE, MAX_PRICE})
    public @interface SortMode {}


    public void setParameter(String mSize, String mName, String mPage, String mSortMode,@Nullable String[] mLabels,
                             @Nullable int mMinPrice, @Nullable int mMaxPrice) {
        this.mName = mName;
        this.mSize = mSize;
        this.mPage = mPage;
        this.mSortMode = mSortMode;
        this.mLabels = mLabels;
        this.mMinPrice = mMinPrice;
        this.mMaxPrice = mMaxPrice;
    }

    @Override
    protected void load() {
        SearchRepository.getInstance().searchGoods(mSize, mName, mPage, mSortMode, mLabels, mMinPrice, mMaxPrice, new BaseObserver<List<SearchGoodsResponse>>(this) {
            @Override
            public void onError(ApiException e) {
                loadFail(e.getMessage());
            }

            @Override
            public void onNext(List<SearchGoodsResponse> mSearchGoodsResponses) {
                loadSuccess(mSearchGoodsResponses);
            }
        });
    }
}
