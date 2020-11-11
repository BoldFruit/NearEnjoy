package com.example.found.model;

import com.example.found.model.bean.LabelBean;
import com.example.found.model.bean.ListRecommendResponse;
import com.example.found.model.bean.NewTopSearchResponseBean;
import com.example.found.model.bean.SearchGoodsResponse;
import com.example.found.model.bean.SearchUserResponse;
import com.example.found.model.bean.TopSearchResponseBean;
import com.example.found.model.models.HotSearchModel;

import java.util.List;
import java.util.Observable;

import io.reactivex.Observer;

import com.example.found.model.models.SearchGoodsModel;
import com.example.network.HttpClient;

/**
 * @author DuLong
 * @since 2020/2/25
 * email 798382030@qq.com
 */
public class SearchRepository {

    private SearchApi mApi;

    private SearchRepository(SearchApi api) {mApi = api;}

    private static class InstanceHolder {
        private static final SearchRepository INSTANCE = new SearchRepository(HttpClient.getInstance().createService(SearchApi.class));
    }

    public static SearchRepository getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void getHotSearch(String schoolId, Observer<NewTopSearchResponseBean> mObserver) {
        HttpClient.getInstance().apiSubscribe(mApi.getNewTopSearch(schoolId), mObserver);
    }

    public void searchUsers(String name, String page, String size, Observer<List<SearchUserResponse>> mObserver) {
        HttpClient.getInstance().apiSubscribe(mApi.searchUsers(name, page, size), mObserver);
    }

    public void searchGoods(String size, String name, String page, @SearchGoodsModel.SortMode String sortMode, String[] labels,
                            int minPrice, int maxPrice, Observer<List<SearchGoodsResponse>> mObserver) {
        HttpClient.getInstance().apiSubscribe(mApi.searchGoods(size, name, page, sortMode, labels, minPrice,maxPrice), mObserver);
    }

    public void listSearchRecommend(String query, String num, Observer<List<ListRecommendResponse>> mObserver) {
        HttpClient.getInstance().apiSubscribe(mApi.getSearchRecommend(query, num), mObserver);
    }

    public void getAllLabels(Observer<List<LabelBean>> mObservable) {
        HttpClient.getInstance().apiSubscribe(mApi.getAllLabels(), mObservable);
    }
}
