package com.example.found.model.bean;


import androidx.databinding.ObservableField;

/**
 * @author DuLong
 * @since 2020/2/24
 * email 798382030@qq.com
 */

public class SearchBean {

    private ObservableField<String> searchContent;

    private ObservableField<String> searchHint;

    private ObservableField<String> searchWayTxt;

//    private ObservableInt searchWayImg;

    public SearchBean(String searchContent, String searchHint, String searchWayTxt, int searchWayImg) {
        this.searchContent = new ObservableField<>(searchContent);
        this.searchHint = new ObservableField<>(searchHint);
        this.searchWayTxt = new ObservableField<>(searchWayTxt);
//        this.searchWayImg = new ObservableInt(searchWayImg);
    }

    public ObservableField<String> getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(ObservableField<String> mSearchContent) {
        searchContent = mSearchContent;
    }

    public ObservableField<String> getSearchHint() {
        return searchHint;
    }

    public void setSearchHint(ObservableField<String> mSearchHint) {
        searchHint = mSearchHint;
    }

    public ObservableField<String> getSearchWayTxt() {
        return searchWayTxt;
    }

    public void setSearchWayTxt(ObservableField<String> mSearchWayTxt) {
        searchWayTxt = mSearchWayTxt;
    }

//    public Integer getSearchWayImg() {
//        return searchWayImg.get();
//    }
//
//    public void setSearchWayImg(@DrawableRes int mSearchWayImg) {
//        searchWayImg = new ObservableInt(mSearchWayImg);
//    }
}
