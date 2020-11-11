package com.example.found.viewmodel;

import com.example.found.model.bean.ListRecommendResponse;
import com.example.found.model.bean.NewTopSearchResponseBean;
import com.example.found.model.bean.SearchGoodsResponse;
import com.example.found.model.bean.SearchUserResponse;
import com.example.found.model.models.GetAllLabelModel;
import com.example.found.model.models.HotSearchModel;
import com.example.found.model.models.ListRecommentSearchModel;
import com.example.found.model.models.SearchGoodsModel;
import com.example.found.model.models.SearchUserModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import androidx.annotation.IntDef;
import androidx.lifecycle.MutableLiveData;
import com.example.base.viewmodel.MvvmNetworkViewModel;

/**
 * @author DuLong
 * @since 2020/2/23
 * email 798382030@qq.com
 */
public class SearchViewModel extends MvvmNetworkViewModel {

    //网络请求的返回内容
    private MutableLiveData<List<ListRecommendResponse>> mRecommentContent;
    private MutableLiveData<NewTopSearchResponseBean> mTopSearchContent;
    private MutableLiveData<List<SearchUserResponse>> mSearchUserResult;
    private MutableLiveData<List<SearchGoodsResponse>> mSearchGoodsResult;

    //model
    private ListRecommentSearchModel mListRecommentSearchModel;
    private SearchUserModel mSearchUserModel;
    private SearchGoodsModel mSearchGoodsModel;
    private HotSearchModel mHotSearchModel;
    private GetAllLabelModel mGetAllLabelModel;

    //记录搜索内容
    private MutableLiveData<Boolean> isSearchGoods;
    //记录点击的Tag中的内容
    private MutableLiveData<String> mTagString;
    //记录目前位于哪个fragment
    private MutableLiveData<Integer> mFragment;
    public final static int SEARCH_FRAGMENT = 0;
    public final static int RECOMMEND_FRAGMENT = 1;
    public final static int USER_DETAIL_FRAGMENT = 2;
    public final static int GOODS_DETAIL_FRAGMENT = 3;
    //搜索框中的内容
    private MutableLiveData<String> mEditContent;
    //排列的方式
    private MutableLiveData<Boolean> isLinearLayout;

    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
    @IntDef({SEARCH_FRAGMENT, RECOMMEND_FRAGMENT, USER_DETAIL_FRAGMENT, GOODS_DETAIL_FRAGMENT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FragmentState {
    }

    @Override
    protected void initModels() {
        mListRecommentSearchModel = new ListRecommentSearchModel();
        mSearchGoodsModel = new SearchGoodsModel();
        mSearchUserModel = new SearchUserModel();
        mHotSearchModel = new HotSearchModel();
        mGetAllLabelModel = new GetAllLabelModel();

        registerModel(HotSearchModel.TAG, mHotSearchModel);
        registerModel(ListRecommentSearchModel.TAG, mListRecommentSearchModel);
        registerModel(SearchGoodsModel.TAG, mSearchGoodsModel);
        registerModel(SearchUserModel.TAG, mSearchUserModel);
        registerModel(GetAllLabelModel.TAG, mGetAllLabelModel);

        mRecommentContent = getDataLiveData(ListRecommentSearchModel.TAG);
        mTopSearchContent = getDataLiveData(HotSearchModel.TAG);
        mSearchGoodsResult = getDataLiveData(SearchGoodsModel.TAG);
        mSearchUserResult = getDataLiveData(SearchUserModel.TAG);

       isSearchGoods = new MutableLiveData<>(true);
       mTagString = new MutableLiveData<>("");
       mFragment = new MutableLiveData<>(SEARCH_FRAGMENT);
       mEditContent = new MutableLiveData<>();
       isLinearLayout = new MutableLiveData<>(true);
    }

    public void getAllLabels() {
        getCachedDataAndLoad(GetAllLabelModel.TAG);
    }
    /**
     * 获得热门搜索
     */
    public void getHotSearch(String schoolId) {
        mHotSearchModel.setParameter(schoolId);
        getCachedDataAndLoad(HotSearchModel.TAG);
    }

    /**
     * 获取商品查询内容
     */
    public void getGoods(String size, String name, String page
    , @SearchGoodsModel.SortMode String searchMode, String[] labels, int minPrice, int maxPrice) {
        mSearchGoodsModel.setParameter(size, name, page, searchMode, labels, minPrice, maxPrice);
        getCachedDataAndLoad(SearchGoodsModel.TAG);
    }

    /**
     * 获取用户查询结果
     */
    public void getUsers(String name, String page, String size) {
        mSearchUserModel.setParameter(name, page, size);
        getCachedDataAndLoad(SearchUserModel.TAG);
    }

    /**
     * 获取与搜索内容相关的结果
     */
    public void getRecomment(String query, String num) {
        mListRecommentSearchModel.setParameter(query, num);
        getCachedDataAndLoad(ListRecommentSearchModel.TAG);
    }






    public MutableLiveData<List<ListRecommendResponse>> getRecommentContent() {
        return mRecommentContent;
    }

    public void setRecommentContent(MutableLiveData<List<ListRecommendResponse>> mRecommentContent) {
        this.mRecommentContent = mRecommentContent;
    }

    public MutableLiveData<NewTopSearchResponseBean> getTopSearchContent() {
        return mTopSearchContent;
    }

    public MutableLiveData<List<SearchUserResponse>> getSearchUserResult() {
        return mSearchUserResult;
    }

    public void setSearchUserResult(MutableLiveData<List<SearchUserResponse>> mSearchUserResult) {
        this.mSearchUserResult = mSearchUserResult;
    }

    public MutableLiveData<List<SearchGoodsResponse>> getSearchGoodsResult() {
        return mSearchGoodsResult;
    }

    public void setSearchGoodsResult(MutableLiveData<List<SearchGoodsResponse>> mSearchGoodsResult) {
        this.mSearchGoodsResult = mSearchGoodsResult;
    }

    public MutableLiveData<Boolean> getIsSearchGoods() {
        return isSearchGoods;
    }

    public void setIsSearchGoods(MutableLiveData<Boolean> mIsSearchGoods) {
        isSearchGoods = mIsSearchGoods;
    }

    public MutableLiveData<String> getTagString() {
        return mTagString;
    }

    public void setTagString(MutableLiveData<String> mTagString) {
        this.mTagString = mTagString;
    }

    public Integer getFragment() {
        return mFragment.getValue();
    }

    public void setFragment(@FragmentState int mFragmentState) {
        this.mFragment.postValue(mFragmentState);
    }

    public MutableLiveData<String> getEditContent() {
        return mEditContent;
    }

    public void setEditContent(MutableLiveData<String> mEditContent) {
        this.mEditContent = mEditContent;
    }

    public MutableLiveData<Boolean> getIsLinearLayout() {
        return isLinearLayout;
    }

    public void setIsLinearLayout(boolean mIsLinearLayout) {
        isLinearLayout.postValue(mIsLinearLayout);
    }
}
