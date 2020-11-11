package com.example.found.view.fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

import android.view.View;

import com.example.base.model.bean.BaseNetworkStatus;
import com.example.base.utils.ToastUtil;
import com.example.base.view.fragment.MvvmBaseFragment;
import com.example.found.R;
import com.example.found.databinding.FragmentSearchResultBinding;
import com.example.found.model.bean.SearchUserResponse;
import com.example.found.paging.Listing;
import com.example.found.paging.NetworkState;
import com.example.found.paging.Status;
import com.example.found.paging.adapter.UserDetailAdapter;
import com.example.found.paging.repository.UserRepository;
import com.example.found.viewmodel.SearchViewModel;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

/**
 * 用户搜索结果
 */
public class SearchUserResultFragment extends MvvmBaseFragment<FragmentSearchResultBinding, SearchViewModel> {

    //用于网络请求的线程池
    private ExecutorService NETWORK_IO = Executors.newFixedThreadPool(5);
    private UserDetailAdapter mAdapter;
    private Listing<SearchUserResponse> mListing;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_result;
    }

    @Override
    protected void initViewModel() {
        mViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(SearchViewModel.class);
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
        initAdapter();
    }


    private void initAdapter() {
        UserRepository mRepository = new UserRepository(NETWORK_IO);
        mListing = mRepository.createListing(mViewModel.getEditContent().getValue(), 20);
        mAdapter = new UserDetailAdapter(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                mListing.getRetry().invoke();
                return null;
            }
        });
        mViewDataBinding.relayout.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewDataBinding.relayout.setAdapter(mAdapter);
        mViewDataBinding.include.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListing.getRetry().invoke();
            }
        });
        //监听网络状态
        mListing.getNetworkState().observe(SearchUserResultFragment.this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState mNetworkState) {
                mAdapter.setNetWorkState(mNetworkState);
            }
        });
        //监听数据变化
        mListing.getPagedList().observe(SearchUserResultFragment.this, new Observer<PagedList<SearchUserResponse>>() {
            @Override
            public void onChanged(PagedList<SearchUserResponse> mSearchUserResponses) {
                mAdapter.submitList(mSearchUserResponses, new Runnable() {
                    @Override
                    public void run() {
                        int position = new LinearLayoutManager(getContext()).findFirstCompletelyVisibleItemPosition();
                        if (position != NO_POSITION) {
                            mViewDataBinding.relayout.scrollToPosition(position);
                        }
                    }
                });
            }
        });
        //监听初始化状态
        mListing.getRefreshState().observe(SearchUserResultFragment.this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState mNetworkState) {
                Status mStatus = mNetworkState.getStatus();
                mViewDataBinding.relayout.setVisibility(toVisible(mStatus == Status.SUCCESS));
                mViewDataBinding.include.rlayoutNoData.setVisibility(toVisible(mStatus == Status.FAILED || mStatus == Status.NO_DATA));
                mViewDataBinding.include.btnRetry.setVisibility(toVisible(mStatus == Status.FAILED));
                mViewDataBinding.include.progressBar.setVisibility(toVisible(mStatus == Status.RUNNING));
                mViewDataBinding.include.txtProblem.setText(mStatus == Status.NO_DATA ? "没有找到相关的用户" : "网络请求失败了");
            }
        });
    }

    private int toVisible(boolean constraint) {
        if (constraint) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
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
        ToastUtil.show(getContext(), "网络加载好像失败了，试试重新加载");
    }

    @Override
    public void onNoNetwork(String key, BaseNetworkStatus status) {
        ToastUtil.show(getContext(), "网络没有了");
    }

    @Override
    public void onNetInit(String key, BaseNetworkStatus status) {

    }
}
