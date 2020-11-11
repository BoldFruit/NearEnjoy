package com.example.found.view.fragment;

import android.content.Context;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import com.example.base.model.bean.BaseNetworkStatus;
import com.example.base.view.fragment.MvvmBaseFragment;
import com.example.found.R;
import com.example.found.databinding.FragmentSearchBinding;
import com.example.found.model.bean.HotSearchBean;
import com.example.found.model.bean.NewTopSearchResponseBean;
import com.example.found.view.adapter.HotSearchTagAdapter;
import com.example.found.view.adapter.MyTagAdapter;
import com.example.found.viewmodel.SearchViewModel;
import com.example.lib_common.widget.dialog.SelfDialog;
import com.example.lib_common.widget.flowlayout.FlowLayout;
import com.example.lib_common.widget.flowlayout.TagFlowLayout;
import com.example.lib_data.data_user.NearEnjoyUser;
import com.example.lib_data.data_user.UserInfoIO;
import com.example.lib_data.room.bean.SearchHistoryBean;
import com.example.lib_data.room.manager.SearchHistoryDataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.transform.Transformer;

public class SearchFragment extends MvvmBaseFragment<FragmentSearchBinding, SearchViewModel> {

    public static final String TAG = "SearchFragment";
    private Context mContext;
    private View preView;
    private int prePosition;

    public class Listener {
        public void onClick(View v) {
            int mId = v.getId();
            if (mId == R.id.llayout_change_search_way) {
                boolean isGoods = mViewModel.getIsSearchGoods().getValue();
                mViewModel.getIsSearchGoods().setValue(!isGoods);
            } else if (mId == R.id.img_clear) {
                SelfDialog mSelfDialog = new SelfDialog(getContext());
                mSelfDialog.setLayout(SelfDialog.NO_TITLE_DIALOG);
                mSelfDialog.setMessage("确定删除全部搜索历史记录？");
                mSelfDialog.setYesOnclickListener("确定", new SelfDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        //清空数据库
                        if (mViewModel.getIsSearchGoods().getValue()) {
                            SearchHistoryDataManager.deleteAllGoodsHistory(mContext);
                            mViewDataBinding.tlayoutSearchHistory.getAdapter().getTagDatas().clear();
                            mViewDataBinding.tlayoutSearchHistory.onChanged();
                        } else {
                            SearchHistoryDataManager.deleteAllUserHistory(mContext);
                            mViewDataBinding.tlayoutSearchHistory.getAdapter().getTagDatas().clear();
                            mViewDataBinding.tlayoutSearchHistory.onChanged();
                        }
                    }
                });
                mSelfDialog.setNoOnclickListener("取消", new SelfDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        mSelfDialog.dismiss();
                    }
                });
                mSelfDialog.show();
            }
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_search;
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
        return TAG;
    }

    @Override
    protected void initParameters() {

    }

    @Override
    protected void initDataAndView() {
        mContext = getContext();
        mViewDataBinding.setListener(new Listener());
        addClickListener();
        addObserver();
        try {
            NearEnjoyUser user = UserInfoIO.getLastUser(mContext);
            int schoolId;
            //网络请求获得热门搜索
            if (user != null) {
                schoolId = user.getSchoolId();
            } else {
                schoolId = 1;
            }
            mViewModel.getHotSearch(Integer.toString(schoolId));
        } catch (Exception mE) {
            mE.printStackTrace();
        }
    }

    /**
     * 设置标签点击事件
     */
    private void addClickListener() {
        //标签点击时，改变搜索框的内容，并且进行搜索的网络请求
        mViewDataBinding.tlayoutSearchHistory.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                String content = (String) (mViewDataBinding.tlayoutSearchHistory.getAdapter().getItem(position));
                //存储搜索记录
                SearchHistoryBean data = new SearchHistoryBean();
                data.setTime(System.currentTimeMillis());
                data.setGoods(mViewModel.getIsSearchGoods().getValue());
                data.setSearchContent(content);
                SearchHistoryDataManager.insertHistory(data, mContext);
                //通知搜索栏跳转
                mViewModel.getTagString().postValue(content);
                return true;
            }
        });

        //长按删除搜索记录
        mViewDataBinding.tlayoutSearchHistory.setOnTagLongClickListener(new TagFlowLayout.OnTagLongClickListener() {

            @Override
            public boolean onTagLongClickListener(View view, int position, FlowLayout parent) {
                if (preView != null) {
                    //还原前一次的长按对象
                    preView.findViewById(R.id.img_delete).setVisibility(View.GONE);
                    preView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String content = (String) (mViewDataBinding.tlayoutSearchHistory.getAdapter().getItem(prePosition));
                            //存储搜索记录
                            SearchHistoryBean data = new SearchHistoryBean();
                            data.setTime(System.currentTimeMillis());
                            data.setGoods(mViewModel.getIsSearchGoods().getValue());
                            data.setSearchContent(content);
                            SearchHistoryDataManager.insertHistory(data, mContext);
                            //通知搜索栏跳转
                            mViewModel.getTagString().postValue(content);
                        }
                    });
                }
                //改变当前的长按对象
                view.findViewById(R.id.img_delete).setVisibility(View.VISIBLE);
                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mViewModel.getIsLinearLayout().getValue()) {
                            String deleteItem = String.valueOf(mViewDataBinding.tlayoutSearchHistory.getAdapter().getItem(position));
                            mViewDataBinding.tlayoutSearchHistory.getAdapter().getTagDatas().remove(deleteItem);
                            mViewDataBinding.tlayoutSearchHistory.onChanged();
                            preView = null;
                            SearchHistoryDataManager.deleteGoodsHistory(getContext(), deleteItem);
                        } else {
                            String deleteItem = String.valueOf(mViewDataBinding.tlayoutSearchHistory.getAdapter().getItem(position));
                            mViewDataBinding.tlayoutSearchHistory.getAdapter().getTagDatas().remove(deleteItem);
                            mViewDataBinding.tlayoutSearchHistory.onChanged();
                            preView = null;
                            SearchHistoryDataManager.deleteUserHistory(getContext(), deleteItem);
                        }
                    }
                });
                preView = view;
                prePosition = position;
                return true;
            }
        });

        //点击其它地方时，将带有❌的tag还原
        mViewDataBinding.root.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (preView != null) {
                    preView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String content = (String) (mViewDataBinding.tlayoutSearchHistory.getAdapter().getItem(prePosition));
                            //存储搜索记录
                            SearchHistoryBean data = new SearchHistoryBean();
                            data.setTime(System.currentTimeMillis());
                            data.setGoods(mViewModel.getIsSearchGoods().getValue());
                            data.setSearchContent(content);
                            SearchHistoryDataManager.insertHistory(data, mContext);
                            //通知搜索栏跳转
                            mViewModel.getTagString().postValue(content);
                        }
                    });
                    preView.findViewById(R.id.img_delete).setVisibility(View.GONE);
                }
            }
        });

        /**
         * 设置热门搜索的点击事件
         */
        mViewDataBinding.tlayoutHotSearch.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                HotSearchBean content = (HotSearchBean) (mViewDataBinding.tlayoutHotSearch.getAdapter().getItem(position));
                //存储搜索记录
                SearchHistoryBean data = new SearchHistoryBean();
                data.setTime(System.currentTimeMillis());
                data.setGoods(mViewModel.getIsSearchGoods().getValue());
                data.setSearchContent(content.getContent());
                SearchHistoryDataManager.insertHistory(data, mContext);
                mViewModel.getTagString().postValue(content.getContent());
                return true;
            }
        });
    }

    /**
     * 添加监听
     */
    private void addObserver() {
        //热门搜索数据改变时
        mViewModel.getTopSearchContent().observe((LifecycleOwner) mContext, new Observer<NewTopSearchResponseBean>() {
            @Override
            public void onChanged(NewTopSearchResponseBean mNewTopSearchResponseBean) {
                List<HotSearchBean> data = new ArrayList<>();
                for (NewTopSearchResponseBean.BgTopSearchListBean mBean : mNewTopSearchResponseBean.getBgTopSearchList()) {
                    HotSearchBean bean = new HotSearchBean();
                    bean.setManagerSet(true);
                    bean.setContent(mBean.getName());
                    data.add(bean);
                }
                for (String mBean : mNewTopSearchResponseBean.getGoodsTopSearchList()) {
                    HotSearchBean bean = new HotSearchBean();
                    bean.setContent(mBean);
                    bean.setManagerSet(false);
                    data.add(bean);
                }
                mViewDataBinding.tlayoutHotSearch.setAdapter(new HotSearchTagAdapter(data, getActivity()));
            }
        });

        //搜索类型改变时
        mViewModel.getIsSearchGoods().observe((LifecycleOwner) mContext, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean mBoolean) {
                if (mBoolean) {
                    //从搜索用户变为搜索商品,更新历史记录
                    List<SearchHistoryBean> searchHistory= SearchHistoryDataManager.getAllGoodsHistory(mContext);
                    List<String> data = new ArrayList<>();
                    for (SearchHistoryBean item : searchHistory) {
                        data.add(item.getSearchContent());
                    }
                    mViewDataBinding.tlayoutSearchHistory.setAdapter(new MyTagAdapter(data, mContext, MyTagAdapter.NORMAL));
                    //显示热门搜索
                    mViewDataBinding.llayoutTopSearch.setVisibility(View.VISIBLE);
                    mViewDataBinding.tlayoutHotSearch.setVisibility(View.VISIBLE);

                    mViewDataBinding.imgTag.setImageResource(R.drawable.ic_user);
                    mViewDataBinding.txtTagName.setText(R.string.txt_search_user);
                } else {
                    //从搜索商品变为搜索用户
                    List<SearchHistoryBean> searchHistory = SearchHistoryDataManager.getAllUserHistory(mContext);
                    List<String> data = new ArrayList<>();
                    for (SearchHistoryBean item : searchHistory) {
                        data.add(item.getSearchContent());
                    }
                    mViewDataBinding.tlayoutSearchHistory.setAdapter(new MyTagAdapter(data,mContext, MyTagAdapter.NORMAL));
                    //隐藏热门搜索
                    mViewDataBinding.llayoutTopSearch.setVisibility(View.GONE);
                    mViewDataBinding.tlayoutHotSearch.setVisibility(View.GONE);

                    mViewDataBinding.imgTag.setImageResource(R.drawable.ic_goods);
                    mViewDataBinding.txtTagName.setText(R.string.txt_search_goods);
                }
            }
        });
    }

    @Override
    protected void initViewModel() {
        mViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
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

    @Override
    public void onLoadMoreFailure(String message) {

    }

    @Override
    public void onLoadMoreEmpty() {

    }
}
