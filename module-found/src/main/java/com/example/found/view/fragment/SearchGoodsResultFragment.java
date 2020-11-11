package com.example.found.view.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.ArraySet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.HttpAuthHandler;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.base.model.bean.BaseNetworkStatus;
import com.example.base.nettype.annotation.Network;
import com.example.base.utils.ToastUtil;
import com.example.base.view.fragment.MvvmBaseFragment;
import com.example.found.FoundApp;
import com.example.found.R;
import com.example.found.databinding.FragmentListBinding;
import com.example.found.databinding.FragmentSearchGoodsResultBinding;
import com.example.found.model.bean.SearchGoodsResponse;
import com.example.found.model.models.SearchGoodsModel;
import com.example.found.paging.Listing;
import com.example.found.paging.NetworkState;
import com.example.found.paging.SearchApi;
import com.example.found.paging.Status;
import com.example.found.paging.adapter.GoodsDetailAdapter;
import com.example.found.paging.adapter.viewHolder.GirdGoodsViewHolder;
import com.example.found.paging.datasource.GoodsDataSource;
import com.example.found.paging.factory.GoodsDataSourceFactory;
import com.example.found.paging.repository.GoodsRepository;
import com.example.found.view.FoundActivity;
import com.example.found.view.adapter.MyTagAdapter;
import com.example.found.viewmodel.SearchViewModel;
import com.example.lib_common.util.DensityUtil;
import com.example.lib_common.util.SPUtils;
import com.example.lib_common.widget.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.BiConsumer;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Ref;
import retrofit2.Retrofit;

import static android.view.ViewGroup.getChildMeasureSpec;
import static android.widget.ListPopupWindow.MATCH_PARENT;
import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;
import static com.example.found.model.models.SearchGoodsModel.MAX_PRICE;
import static com.example.found.model.models.SearchGoodsModel.MIN_PRICE;
import static com.example.found.model.models.SearchGoodsModel.NEWEST;
import static com.example.found.model.models.SearchGoodsModel.OVERALL;

/**
 * @author DuLong
 * @since 2020/3/3
 * email 798382030@qq.com
 */
public class SearchGoodsResultFragment extends MvvmBaseFragment<FragmentSearchGoodsResultBinding, SearchViewModel> {

    //用于网络请求的线程池
    private ExecutorService NETWORK_IO = Executors.newFixedThreadPool(5);
    private GoodsDetailAdapter mAdapter;
    //滑动排序方式界面的动画
    private ObjectAnimator mSortSlidAnimator;
    //滑动筛选界面的动画
    private ObjectAnimator mSelectSlidAnimator;

    //判断是否在展示排序方式
    private boolean mIsShowingSortWay;
    //判断是否在展示搜索条件
    private boolean mIsShowingSortCondition;
    private String sortMode;

    //与EditView进行了双向绑定的数据
    private ObservableField<String> mMinValue;
    private ObservableField<String> mMaxValue;

    //用于映射adapter中的指数到标签的id
    private HashMap<Integer, Integer> mLabelKeyMap;

    //目前recyclerview中处于最顶端的item的序号
    private int mCurPosition;
    private StaggeredGridLayoutManager mGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isShowingRocket = false;

    private Listing<SearchGoodsResponse.SearchListBean> mListing;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_goods_result;
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
        return "SearchGoodsResultFragment";

    }

    @Override
    protected void initViewModel() {
        mViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
    }

    @Override
    protected void initParameters() {

    }

    @Override
    protected void initDataAndView() {

        mGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mCurPosition = 0;
        sortMode = OVERALL;
        mIsShowingSortCondition = false;
        mIsShowingSortWay = false;
        //双向绑定DataBinding
        mMaxValue = new ObservableField<>("");
        mMinValue = new ObservableField<>("");
        mViewDataBinding.setMaxValue(mMaxValue);
        mViewDataBinding.setMinValue(mMinValue);
        mViewDataBinding.setEventPresenter(new EventPresenter());
        initAdapter();
        addObserve();
    }

    private void initAdapter() {
        if (mViewModel.getIsLinearLayout().getValue() != null) {
            mAdapter = new GoodsDetailAdapter(getActivity(), mViewModel.getIsLinearLayout().getValue(), new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    mListing.getRetry().invoke();
                    return null;
                }
            });
        }
        if (mViewModel.getIsLinearLayout().getValue()) {
            mViewDataBinding.relayout.setLayoutManager(mLinearLayoutManager);
        } else {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            lp.leftMargin = DensityUtil.dpToPx(requireContext(), 4);
            lp.rightMargin = DensityUtil.dpToPx(getContext(), 4);
            mViewDataBinding.relayout.setLayoutParams(lp);
            mViewDataBinding.relayout.setLayoutManager(mGridLayoutManager);
        }
        mViewDataBinding.relayout.setAdapter(mAdapter);
        //解决开始是top不在顶端的问题
        mViewDataBinding.relayout.scrollToPosition(0);
        updateLivePagedList(null, null, null, null);

        //初始化tagLayout的adapter
        mLabelKeyMap = new HashMap<>();
        List<String> mLabelsName = new ArrayList<>();

        HashMap<Integer, String> mLabelsMap = SPUtils.get("labels", new HashMap<Integer, String>());
        int index = 0;
        //遍历label,同时记录下adapter中的position和labelId的对应关系。
        for (Map.Entry<Integer, String> mEntry : mLabelsMap.entrySet()) {
            mLabelsName.add(mEntry.getValue());
            mLabelKeyMap.put(index++, mEntry.getKey());
        }
        MyTagAdapter mTagAdapter = new MyTagAdapter(mLabelsName, getContext(), MyTagAdapter.CLICKABLE_TAG);
        mViewDataBinding.tlayout.setAdapter(mTagAdapter);
    }

    private void addObserve() {
        //添加对滑动的进度的监听,以此来判断是否显示小火箭
        mViewDataBinding.relayout.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    //解决StaggeredGirdLayout有时跳跃的问题
                    StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager)recyclerView.getLayoutManager();
                    layoutManager.invalidateSpanAssignments();
                }
            }

            /**
             * 控制小火箭的显示以及记录下最后一个item的position
             * @param recyclerView
             * @param dx
             * @param dy
             */
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                RecyclerView.LayoutManager mManager = recyclerView.getLayoutManager();
                if (mManager instanceof LinearLayoutManager) {
                    if (((LinearLayoutManager) mManager).findFirstCompletelyVisibleItemPosition() > 0 && !isShowingRocket) {
                        int distance = DensityUtil.dpToPx(getContext(), 72);
                        mViewDataBinding.fba.animate()
                                .translationY(-distance)
                                .setInterpolator(new AccelerateInterpolator())
                                .setDuration(100)
                                .start();
                        isShowingRocket = true;
                    } else if (((LinearLayoutManager) mManager).findFirstCompletelyVisibleItemPosition() == 0 && isShowingRocket){
                        int distance = DensityUtil.dpToPx(getContext(), 72);
                        mViewDataBinding.fba.animate()
                                .translationY(distance)
                                .setInterpolator(new AccelerateInterpolator())
                                .setDuration(100)
                                .start();
                        isShowingRocket = false;
                    }
                } else {
                    int[] firstVisibleItems = new int[2];
                    assert mManager != null;
                    ((StaggeredGridLayoutManager) mManager).findFirstCompletelyVisibleItemPositions(firstVisibleItems);
                    if (getMinPosition(firstVisibleItems) > 0 && !isShowingRocket) {
                        int distance = DensityUtil.dpToPx(getContext(), 72);
                        mViewDataBinding.fba.animate()
                                .translationY(-distance)
                                .setInterpolator(new AccelerateInterpolator())
                                .setDuration(100)
                                .start();
                        isShowingRocket = true;
                    } else if (getMinPosition(firstVisibleItems) == 0 && isShowingRocket){
                        int distance = DensityUtil.dpToPx(getContext(), 72);
                        mViewDataBinding.fba.animate()
                                .translationY(distance)
                                .setInterpolator(new AccelerateInterpolator())
                                .setDuration(100)
                                .start();
                        isShowingRocket = false;
                    }
                }
            }
        });



        //布局模式改变时
        mViewModel.getIsLinearLayout().observe(SearchGoodsResultFragment.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean mBoolean) {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                if (mBoolean) {
                    //记录当前的位置,便于改变布局后重新回到当前item
                    int[] curPositions = new int[2];
                    mGridLayoutManager.findFirstCompletelyVisibleItemPositions(curPositions);
                    mCurPosition = getMinPosition(curPositions);
                    mAdapter.switchLayout(true);
                    mViewDataBinding.relayout.setLayoutParams(lp);
                    mViewDataBinding.relayout.setLayoutManager(mLinearLayoutManager);
                    if (mCurPosition != NO_POSITION) {
                        mViewDataBinding.relayout.scrollToPosition(mCurPosition);
                    }
                } else {
                    //记录当前的位置,便于改变布局后重新回到当前item
                    mCurPosition = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    //改变布局参数
                    lp.leftMargin = DensityUtil.dpToPx(getContext(), 4);
                    lp.rightMargin = DensityUtil.dpToPx(getContext(), 4);
                    mAdapter.switchLayout(false);
                    mViewDataBinding.relayout.setLayoutParams(lp);
                    mViewDataBinding.relayout.setLayoutManager(mGridLayoutManager);
                    mViewDataBinding.relayout.scrollToPosition(mCurPosition);
                }
            }
        });
    }

    /**
     * 获取数组中较大的那一个
     * @param positions
     * @return
     */
    private int getMaxPosition(int[] positions) {
        int max = positions[0];
        for (int i = 1; i < positions.length; i++) {
            if (positions[i] > max) {
                max = positions[i];
            }
        }
        return max;
    }

    private int getMinPosition(int[] positions) {
        int min = positions[0];
        for (int i = 1; i < positions.length; i++) {
            if (positions[i] < min) {
                min = positions[i];
            }
        }
        return min;
    }

    public class EventPresenter {

        public void onClick(View v) {
            int mId = v.getId();
            if (mId == R.id.view_shadow) {
                if (mIsShowingSortCondition) {
                    slideSortConditionWidow();
                } else if (mIsShowingSortWay) {
                    slideSortWayWindow();
                }
            } else if (mId == R.id.llayout_comprehensive) {
                slideSortWayWindow();
            } else if (mId == R.id.llayout_select) {
                slideSortConditionWidow();
            } else if (mId == R.id.txt_sort_comprehensive) {
                onSortWayChanged(OVERALL);
            } else if (mId == R.id.txt_sort_down) {
                onSortWayChanged(MAX_PRICE);
            } else if (mId == R.id.txt_sort_up) {
                onSortWayChanged(MIN_PRICE);
            } else if (mId == R.id.txt_new_publication) {
                onSortWayChanged(NEWEST);
            } else if (mId == R.id.btn_reset) {
                mMaxValue.set("");
                mMinValue.set("");
                mViewDataBinding.tlayout.getAdapter().setSelectedList(new HashSet<>());
            } else if (mId == R.id.btn_determine) {
                onFilter();
            } else if (mId == R.id.fba) {//滑动到顶部
                mViewDataBinding.relayout.scrollToPosition(0);
            }

        }
    }

    /**
     * 确定筛选条件时
     */
    private void onFilter() {
        //获取选中的标签
        Set<Integer> labels = mViewDataBinding.tlayout.getSelectedList();
        List<Integer> mLabelIds = new ArrayList<>();
        for (int position : labels) {
            mLabelIds.add(mLabelKeyMap.get(position));
        }
        Integer minValue = mMinValue.get().equals("") ? null : Integer.parseInt(mMinValue.get());
        Integer maxValue = mMaxValue.get().equals("") ? null : Integer.parseInt(mMaxValue.get());
        updateLivePagedList(sortMode, minValue, maxValue, mLabelIds);
        //ui改变
        mViewDataBinding.viewShadow.setVisibility(View.GONE);
        slideSortConditionWidow();
    }



    /**
     * 改变搜索方式
     * 同时更新数据
     * @param mSortMode 当前选中的搜索方式
     */
    private void onSortWayChanged(String mSortMode) {
        if (!mSortMode.equals(sortMode)) {
            changeTextColor(mSortMode);
            sortMode = mSortMode;
        }
        updateLivePagedList(sortMode, null, null, null);
        //ui改变
        mViewDataBinding.viewShadow.setVisibility(View.GONE);
        slideSortWayWindow();
    }

    /**
     * 利用条件创建新的LivePagedList
     * @param sortMode 搜索模式
     * @param minPrice  最低价
     * @param maxPrice  最高价
     * @param labels    标签
     */
    private void updateLivePagedList(String sortMode, Integer minPrice, Integer maxPrice, List<Integer> labels) {
        GoodsRepository mRepository = new GoodsRepository(NETWORK_IO);
        //清空Adapter中的数据
        if (mListing != null) {
            mAdapter.submitList(null);
        }
        int mPageSize = 10;
        mListing = mRepository.createListing(mViewModel.getEditContent().getValue(), mPageSize, sortMode, labels, minPrice, maxPrice);
        //监听网络状态
        mListing.getNetworkState().observe(SearchGoodsResultFragment.this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState mNetworkState) {
                mAdapter.setNetWorkState(mNetworkState);
            }
        });
        mListing.getPagedList().observe(SearchGoodsResultFragment.this, new Observer<PagedList<SearchGoodsResponse.SearchListBean>>() {
            @Override
            public void onChanged(PagedList<SearchGoodsResponse.SearchListBean> mSearchListBeans) {
                mAdapter.submitList(mSearchListBeans, new Runnable() {
                    @Override
                    public void run() {
                        if (mViewModel.getIsLinearLayout().getValue()) {
                            int position = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                            if (position != NO_POSITION) {
                                mViewDataBinding.relayout.scrollToPosition(position);
                            }
                        } else {
                            int[] firstItems = new int[2];
                            mGridLayoutManager.findFirstCompletelyVisibleItemPositions(firstItems);
                            int position = getMinPosition(firstItems);
                            if (position != NO_POSITION) {
                                mViewDataBinding.relayout.scrollToPosition(position);
                            }
                        }
                    }
                });
            }
        });
        //监听初次网络请求的状况
        mListing.getRefreshState().observe(SearchGoodsResultFragment.this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState mNetworkState) {
                Status mStatus = mNetworkState.getStatus();
                mViewDataBinding.relayout.setVisibility(toVisible(mStatus == Status.SUCCESS));
                mViewDataBinding.include.rlayoutNoData.setVisibility(toVisible(mStatus == Status.FAILED || mStatus == Status.NO_DATA));
                mViewDataBinding.include.btnRetry.setVisibility(toVisible(mStatus == Status.FAILED));
                mViewDataBinding.include.progressBar.setVisibility(toVisible(mStatus == Status.RUNNING));
                mViewDataBinding.include.txtProblem.setText(mStatus == Status.NO_DATA ? "没有找到相关的商品" : "网络请求失败了");
            }
        });

        mViewDataBinding.include.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListing.getRetry().invoke();
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

    /**
     * 改变排序方式中选中字体颜色
     */
    private void changeTextColor(@SearchGoodsModel.SortMode String mSortMode) {
        switch (sortMode) {
            case OVERALL:
                mViewDataBinding.txtSortComprehensive.setTextColor(getResources().getColor(android.R.color.black));
                break;
            case MIN_PRICE:
                mViewDataBinding.txtSortUp.setTextColor(getResources().getColor(android.R.color.black));
                break;
            case MAX_PRICE:
                mViewDataBinding.txtSortDown.setTextColor(getResources().getColor(android.R.color.black));
                break;
            case NEWEST:
                mViewDataBinding.txtNewPublication.setTextColor(getResources().getColor(android.R.color.black));
                break;
        }
        switch (mSortMode) {
            case OVERALL:
                mViewDataBinding.txtSortComprehensive.setTextColor(getResources().getColor(R.color.bg_tag_yellow));
                mViewDataBinding.txtComprehensive.setText("综合");
                break;
            case MIN_PRICE:
                mViewDataBinding.txtSortUp.setTextColor(getResources().getColor(R.color.bg_tag_yellow));
                mViewDataBinding.txtComprehensive.setText("低到高");
                break;
            case MAX_PRICE:
                mViewDataBinding.txtSortDown.setTextColor(getResources().getColor(R.color.bg_tag_yellow));
                mViewDataBinding.txtComprehensive.setText("高到低");
                break;
            case NEWEST:
                mViewDataBinding.txtNewPublication.setTextColor(getResources().getColor(R.color.bg_tag_yellow));
                mViewDataBinding.txtComprehensive.setText("新发布");
                break;
        }
    }

    /**
     * 搜索方式界面上下滑动的动画
     *
     */
    private void slideSortWayWindow() {
        int height = mViewDataBinding.llayoutSortWay.getHeight();
        float distance = mIsShowingSortWay ? -height : height;
        //防止用户快速激活动画，导致ViewGroup初始位置发生变化
        if (mSortSlidAnimator != null && mSortSlidAnimator.isRunning()) {
            mSortSlidAnimator.cancel();
            //还没滑到底部时又再次点击上滑，即下滑动画未完成
            if (mIsShowingSortWay) {
                distance += (height - (float) mSortSlidAnimator.getAnimatedValue());
            } else {
                distance -= (float) mSortSlidAnimator.getAnimatedValue();
            }
        }

        //防止两个界面同时处于展开的状态。
        if (mIsShowingSortCondition && !mIsShowingSortWay) {
            slideSortConditionWidow();
        }
        float curTranslationY = mViewDataBinding.llayoutSortWay.getTranslationY();
        mSortSlidAnimator = ObjectAnimator.ofFloat(mViewDataBinding.llayoutSortWay, "translationY", curTranslationY, curTranslationY + distance);
        mSortSlidAnimator.start();
        mViewDataBinding.imgPull.setImageResource(!mIsShowingSortWay ? R.drawable.ic_pull_up : R.drawable.ic_pull_down);
        //显示或关闭蒙层，做成dialog的形式
        if (mIsShowingSortWay) {
            mViewDataBinding.txtComprehensive.setTextColor(getResources().getColor(R.color.txt_screen_gray));
            mViewDataBinding.viewShadow.setVisibility(View.GONE);
            mIsShowingSortWay = false;
        } else {
            mViewDataBinding.txtComprehensive.setTextColor(getResources().getColor(android.R.color.black));
            mViewDataBinding.viewShadow.setVisibility(View.VISIBLE);
            mIsShowingSortWay = true;
        }
    }

    /**
     * 筛选界面上下滑动的动画
     */
    private void slideSortConditionWidow() {
        int height = mViewDataBinding.llayoutScreen.getHeight();
        float distance = mIsShowingSortCondition ? -height : height;
        //防止用户快速激活动画，导致ViewGroup初始位置发生变化
        if (mSelectSlidAnimator != null && mSelectSlidAnimator.isRunning()) {
            mSelectSlidAnimator.cancel();
            //还没滑到底部时又再次点击上滑，即下滑动画未完成
            if (mIsShowingSortCondition) {
                distance += (height - (float) mSelectSlidAnimator.getAnimatedValue());
            } else {
                distance -= (float) mSelectSlidAnimator.getAnimatedValue();
            }
        }
        //防止两个界面同时处于展开的状态。
        if (!mIsShowingSortCondition && mIsShowingSortWay) {
            slideSortWayWindow();
        }
        float curTranslationY = mViewDataBinding.llayoutScreen.getTranslationY();
        mSelectSlidAnimator = ObjectAnimator.ofFloat(mViewDataBinding.llayoutScreen, "translationY", curTranslationY, curTranslationY + distance);
        mSelectSlidAnimator.start();
        if (mIsShowingSortCondition) {
            mViewDataBinding.txtSelect.setTextColor(getResources().getColor(R.color.txt_screen_gray));
            mViewDataBinding.viewShadow.setVisibility(View.GONE);
            mIsShowingSortCondition = false;
        } else {
            mViewDataBinding.txtSelect.setTextColor(getResources().getColor(android.R.color.black));
            mViewDataBinding.viewShadow.setVisibility(View.VISIBLE);
            mIsShowingSortCondition = true;
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

    }

    @Override
    public void onNoNetwork(String key, BaseNetworkStatus status) {

    }

    @Override
    public void onNetInit(String key, BaseNetworkStatus status) {

    }
}
