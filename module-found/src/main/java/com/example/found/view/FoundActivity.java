package com.example.found.view;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.base.utils.ToastUtil;
import com.example.base.view.activity.MvvmNetworkActivity;
import com.example.found.FoundApp;
import com.example.found.model.bean.SearchBean;
import com.example.found.model.models.HotSearchModel;
import com.example.found.viewmodel.SearchViewModel;
import com.example.found.R;
import com.example.found.databinding.*;


import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;

import com.example.arouter.Constants;
import com.example.base.model.bean.BaseNetworkStatus;
import com.example.fw_annotations.BindPath;
import com.example.found.databinding.ActivityFoundBinding;
import com.example.lib_common.util.SPUtils;
import com.example.lib_common.util.statusbar.StatusBarUtil;
import com.example.lib_data.room.bean.SearchHistoryBean;
import com.example.lib_data.room.manager.SearchHistoryDataManager;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
import static com.example.found.viewmodel.SearchViewModel.GOODS_DETAIL_FRAGMENT;
import static com.example.found.viewmodel.SearchViewModel.RECOMMEND_FRAGMENT;
import static com.example.found.viewmodel.SearchViewModel.SEARCH_FRAGMENT;
import static com.example.found.viewmodel.SearchViewModel.USER_DETAIL_FRAGMENT;


@BindPath(Constants.ACTIVITY_SEARCH)
public class FoundActivity extends MvvmNetworkActivity<ActivityFoundBinding, SearchViewModel> {
    public static final String TAG = "FoundActivity";

    private MutableLiveData<String> mEditContent;
    //搜索框中的内容
    private ObservableField<String> mContent;
    //记录当点击推荐内容跳转时，editView中的内容。
    private String mPreEditContent;
    private boolean isFirst = true;

    /**
     * 代理在各个fragment中的返回事件
     */
    @Override
    public void onBackPressed() {
        int fragment = mViewModel.getFragment();
        switch (fragment) {
            case SEARCH_FRAGMENT:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case RECOMMEND_FRAGMENT:
                //清空
                mContent.set("");
                break;
            case GOODS_DETAIL_FRAGMENT:
            case USER_DETAIL_FRAGMENT:
                if (!mPreEditContent.equals("")) {
                    mContent.set(mPreEditContent);
                    mPreEditContent = "";
                    goSearch();
                    mViewModel.setFragment(RECOMMEND_FRAGMENT);
                    Navigation.findNavController(this, R.id.fragment).navigateUp();
                } else {
                    mContent.set("");
                    goSearch();
                    mViewModel.setFragment(SEARCH_FRAGMENT);
                    Navigation.findNavController(this, R.id.fragment).navigateUp();
                }
                break;


        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_found;
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
    protected void initParameters() {
    }

    /**
     * 进行UI事件的绑定
     */
    @Override
    protected void setUIReaction() {
        //输入框中的内容变换时，同步到LiveData中
        mContent.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mEditContent.postValue(mContent.get());
            }
        });

        //监听搜索内容的变化，每次变化，进行网络请求，获得搜索提示
        mEditContent.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String content) {
                Log.d(TAG, "onChanged: " + content);
                //将搜索框的内容同步到viewModel中
                mViewModel.getEditContent().setValue(content);
                //根据不同的fragment做出相应的行为
                switch (mViewModel.getFragment()) {
                    case SEARCH_FRAGMENT:
                        //搜索框有内容时跳转到推荐页面
                        if (!content.equals("") && mViewModel.getTagString().getValue().equals("") && !content.equals(mViewDataBinding.editSearchContent.getHint().toString())) {
                            mViewModel.setFragment(SearchViewModel.RECOMMEND_FRAGMENT);
                            Navigation.findNavController(FoundActivity.this, R.id.fragment).navigate(R.id.action_searchFragment_to_listFragment);
                        }
                        break;
                    case SearchViewModel.RECOMMEND_FRAGMENT:
                        //搜索框中没有内容时，跳转回搜索页面
                        if (content.equals("")) {
                            mViewModel.setFragment(SEARCH_FRAGMENT);
                            Navigation.findNavController(FoundActivity.this, R.id.fragment).navigate(R.id.action_listFragment_to_searchFragment);
                        }
                        break;
                    case SearchViewModel.GOODS_DETAIL_FRAGMENT:
                        break;
                    case SearchViewModel.USER_DETAIL_FRAGMENT:
                        break;
                }
            }
        });

        //监听标签是否点击了
        mViewModel.getTagString().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String mS) {
                if (!mS.equals("")) {
                    int fragment = mViewModel.getFragment();
                    if (fragment == RECOMMEND_FRAGMENT) {
                        mPreEditContent = mContent.get();
                    }
                    mContent.set(mS);
                    mViewModel.getTagString().postValue("");
                    goResult();
                    //跳转
                    //判断搜索类型，以此来决定跳转的目的地
                    if (mViewModel.getIsSearchGoods().getValue()) {
                        if (mViewModel.getFragment() == SEARCH_FRAGMENT) {
                            Navigation.findNavController(FoundActivity.this, R.id.fragment).navigate(R.id.action_searchFragment_to_searchGoodsResultFragment);
                        } else {
                            Navigation.findNavController(FoundActivity.this, R.id.fragment).navigate(R.id.action_listFragment_to_searchGoodsResultFragment);
                        }
                        mViewModel.setFragment(SearchViewModel.GOODS_DETAIL_FRAGMENT);
                    } else {
                        if (mViewModel.getFragment() == SEARCH_FRAGMENT) {
                            Navigation.findNavController(FoundActivity.this, R.id.fragment).navigate(R.id.action_searchFragment_to_searchUserResultFragment);
                        } else {
                            Navigation.findNavController(FoundActivity.this, R.id.fragment).navigate(R.id.action_listFragment_to_searchUserResultFragment);
                        }
                        mViewModel.setFragment(SearchViewModel.USER_DETAIL_FRAGMENT);
                    }
                }
            }
        });

        //监听搜索方式的变化
        mViewModel.getIsSearchGoods().observe(this, new Observer<Boolean>() {
            //改变搜索框中的提示
            @Override
            public void onChanged(Boolean mBoolean) {
                if (mBoolean) {
                    mViewDataBinding.editSearchContent.setHint(R.string.edit_goods_hint);
                } else {
                    mViewDataBinding.editSearchContent.setHint(R.string.edit_users_hint);
                }
            }
        });

        //设置搜索框中行为的变化
        mViewDataBinding.editSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyBoard();
                    doSearch();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void initDataAndView() {
        mPreEditContent = "";
        //初始化databinding
        mEditContent = new MutableLiveData<>();
        mContent = new ObservableField<>();
        mViewDataBinding.setEditContent(mContent);
        mViewDataBinding.setClickPresenter(new ClickPresenter());
        //状态栏变化
        StatusBarUtil.setStatusBarColor(this, android.R.color.white);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        //判断是否sp已经存储了标签
        if (SPUtils.get("labels", new HashMap<Integer, String>()) == null) {
            //请求后model会自动存数据到sp中
            mViewModel.getAllLabels();
        }
        showKeyBoard();
    }


    /**
     * 弹出软键盘
     */
    public void showKeyBoard() {
        if (isFirst) {
            mViewDataBinding.editSearchContent.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            isFirst = false;
        } else {
            InputMethodManager inputManager =
                    (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(mViewDataBinding.editSearchContent, 0);
            mViewDataBinding.editSearchContent.requestFocus();
            mViewDataBinding.editSearchContent.setSelection(mViewDataBinding.editSearchContent.getText().length());
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public class ClickPresenter {

        public void onClick(View mView) {
            int mId = mView.getId();//点击搜索按钮时
            if (mId == R.id.txt_search) {
                doSearch();
            } else if (mId == R.id.img_change_layout) {
                mViewModel.setIsLinearLayout(!mViewModel.getIsLinearLayout().getValue());
            } else if (mId == R.id.llayout_search_content) {
                if (mContent.get() == null) {
                    return;
                }
                showKeyBoard();
                //这一步主要时为了返回推荐界面时在一次网络请求
                mContent.set(mContent.get());
                //点击搜索框的除标签外的其他部分时，返回推荐界面
                goSearch();
                //显示搜索框
                if (mViewModel.getFragment() == GOODS_DETAIL_FRAGMENT) {
                    Navigation.findNavController(FoundActivity.this, R.id.fragment).navigate(R.id.action_searchGoodsResultFragment_to_listFragment);
                    mViewModel.setFragment(RECOMMEND_FRAGMENT);
                } else if (mViewModel.getFragment() == USER_DETAIL_FRAGMENT) {
                    Navigation.findNavController(FoundActivity.this, R.id.fragment).navigate(R.id.action_searchUserResultFragment_to_listFragment);
                    mViewModel.setFragment(RECOMMEND_FRAGMENT);
                }
            } else if (mId == R.id.llayout_search_tag) {
                goSearch();
                //显示editView
                mContent.set("");
                if (mViewModel.getFragment() == GOODS_DETAIL_FRAGMENT) {
                    Navigation.findNavController(FoundActivity.this, R.id.fragment).navigate(R.id.action_searchGoodsResultFragment_to_searchFragment);
                    mViewModel.setFragment(SEARCH_FRAGMENT);
                } else {
                    Navigation.findNavController(FoundActivity.this, R.id.fragment).navigate(R.id.action_searchUserResultFragment_to_searchFragment);
                    mViewModel.setFragment(SEARCH_FRAGMENT);
                }
            } else if (mId == R.id.img_finish) {
                onBackPressed();
            }
        }

    }

    /**
     * 进行搜索功能
     */
    private void doSearch() {
        //如果为空，就搜索提示中的内容
        if (mContent.get() == null || "".equals(mContent.get())) {
            mContent.set(mViewDataBinding.editSearchContent.getHint().toString());
        }
        goResult();
        //存储搜索记录
        SearchHistoryBean data = new SearchHistoryBean();
        data.setTime(System.currentTimeMillis());
        data.setGoods(mViewModel.getIsSearchGoods().getValue());
        data.setSearchContent(mContent.get());
        SearchHistoryDataManager.insertHistory(data, FoundActivity.this);
        int fragment = mViewModel.getFragment();
        //进行页面转换
        if (fragment == SEARCH_FRAGMENT) {
            if (mViewModel.getIsSearchGoods().getValue()) {
                mViewModel.setFragment(SearchViewModel.GOODS_DETAIL_FRAGMENT);
                Navigation.findNavController(FoundActivity.this, R.id.fragment).navigate(R.id.action_searchFragment_to_searchGoodsResultFragment);
            } else {
                mViewModel.setFragment(SearchViewModel.USER_DETAIL_FRAGMENT);
                Navigation.findNavController(FoundActivity.this, R.id.fragment).navigate(R.id.action_searchFragment_to_searchUserResultFragment);
            }
        } else {
            if (mViewModel.getIsSearchGoods().getValue()) {
                mViewModel.setFragment(SearchViewModel.GOODS_DETAIL_FRAGMENT);
                Navigation.findNavController(FoundActivity.this, R.id.fragment).navigate(R.id.action_listFragment_to_searchGoodsResultFragment);
            } else {
                mViewModel.setFragment(SearchViewModel.USER_DETAIL_FRAGMENT);
                Navigation.findNavController(FoundActivity.this, R.id.fragment).navigate(R.id.action_listFragment_to_searchUserResultFragment);
            }
        }
    }

    /**
     * 当跳转到搜索结果时，搜索框的变化
     */
    private void goResult() {
        //隐藏按钮和搜索框
        mViewDataBinding.txtSearch.setVisibility(View.GONE);
        mViewDataBinding.editSearchContent.setVisibility(View.GONE);
        //显示标签
        mViewDataBinding.txtTagName.setText(String.format("“%s”相关商品", mContent.get()));
        mViewDataBinding.llayoutSearchTag.setVisibility(View.VISIBLE);
        //允许搜索框外部点击
        mViewDataBinding.llayoutSearchContent.setEnabled(true);
        if (mViewModel.getIsSearchGoods().getValue()) {
            mViewDataBinding.imgChangeLayout.setVisibility(View.VISIBLE);
        }
        hideKeyBoard();
    }

    /**、
     * 当从搜索结果界面跳转到其它界面时，搜索框的变化
     * 在页面跳转前调用
     */
    private void goSearch() {
        //隐藏标签
        mViewDataBinding.llayoutSearchTag.setVisibility(View.GONE);
        mViewDataBinding.llayoutSearchContent.setEnabled(false);
        //显示按钮和搜索框
        mViewDataBinding.txtSearch.setVisibility(View.VISIBLE);
        mViewDataBinding.editSearchContent.setVisibility(View.VISIBLE);
        if (mViewModel.getFragment() == SearchViewModel.GOODS_DETAIL_FRAGMENT) {
            mViewDataBinding.imgChangeLayout.setVisibility(View.GONE);
        }
        showKeyBoard();
    }

    @Override
    public void onNetDone(String key, BaseNetworkStatus status) {
        switch (key) {
            case HotSearchModel.TAG:
                Log.d(TAG, "获取热门搜索");
        }
    }
}
