package com.example.found.view.fragment;
import android.content.Context;
import android.widget.SearchView;

import com.example.base.model.bean.BaseNetworkStatus;
import com.example.base.view.fragment.MvvmBaseFragment;
import com.example.found.R;
import com.example.found.databinding.ActivityFoundBinding;
import com.example.found.databinding.FragmentListBinding;
import com.example.found.model.bean.ListRecommendResponse;
import com.example.found.view.adapter.RecommendAdapter;
import com.example.found.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

public class ListFragment extends MvvmBaseFragment<FragmentListBinding, SearchViewModel> {
    private Context mContext;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    public Class<? extends ViewModel> getViewModel() {
        return SearchViewModel.class;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    protected String getFragmentTag() {
        return null;
    }

    @Override
    protected void initParameters() {

    }

    @Override
    protected void initDataAndView() {
        mContext = getContext();
        addObserver();
    }

    /**
     * 添加监听
     */
    private void addObserver() {
        /**
         * 当网络状态返回时
         */
        mViewModel.getRecommentContent().observe((LifecycleOwner) mContext, new Observer<List<ListRecommendResponse>>() {
            @Override
            public void onChanged(List<ListRecommendResponse> mListRecommendResponses) {
                //更新数据
                String base = mViewModel.getEditContent().getValue();
                //刷新
                mViewDataBinding.relayout.setLayoutManager(new LinearLayoutManager(mContext));
                mViewDataBinding.relayout.setAdapter(new RecommendAdapter(mContext, mListRecommendResponses, base, mViewModel));
            }
        });

        /**
         * 当搜索栏变化时
         */
        mViewModel.getEditContent().observe((LifecycleOwner) mContext, new Observer<String>() {
            @Override
            public void onChanged(String mS) {
                if (!mS.equals("")) {
                    mViewModel.getRecomment(mS, "10");
                }
            }
        });
    }

    @Override
    protected void initViewModel() {
        mViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(SearchViewModel.class);
    }

    @Override
    public void onLoadMoreFailure(String message) {

    }

    @Override
    public void onLoadMoreEmpty() {

    }

    @Override
    public void onNetLoading(String key, BaseNetworkStatus status) {

    }

    @Override
    public void onNetDone(String key, BaseNetworkStatus status) {

    }

    @Override
    public void onNetFailed(String key, BaseNetworkStatus status) {

    }

    @Override
    public void onNoNetwork(String key, BaseNetworkStatus status) {

    }

    @Override
    public void onNetInit(String key, BaseNetworkStatus status) {

    }
}


