package com.example.found.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.found.R;
import com.example.found.databinding.ActivityFoundBinding;
import com.example.found.databinding.ItemRecommentBinding;
import com.example.found.model.bean.ListRecommendResponse;
import com.example.found.viewmodel.SearchViewModel;
import com.example.lib_data.room.bean.SearchHistoryBean;
import com.example.lib_data.room.manager.SearchHistoryDataManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author DuLong
 * @since 2020/3/3
 * email 798382030@qq.com
 */
public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {
    private Context mContext;
    private List<ListRecommendResponse> mData;
    //搜索的内容
    private String mBase;
    private ItemRecommentBinding mBinding;
    private SearchViewModel mViewModel;

    public RecommendAdapter(Context mContext, List<ListRecommendResponse> mData, String mBase, SearchViewModel mViewModel) {
        this.mContext = mContext;
        this.mData = mData;
        this.mBase = mBase;
        this.mViewModel = mViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_recomment, parent, false);
        return new ViewHolder(mBinding.getRoot());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mBinding.executePendingBindings();
        ListRecommendResponse data = mData.get(position);
        mBinding.txtSearchResult.setText(String.format("%d个结果", data.getResultNum()));
        String name = data.getName();
        //获得第一次出现的下标
        int index = name.indexOf(mBase);
        //将带有搜索内容的字体加黑
        if (index != -1) {
            SpannableString spannableString = new SpannableString(name);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), index, index + mBase.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            mBinding.txtName.setText(spannableString);
        } else  {
            mBinding.txtName.setText(name);
        }

        //设置点击事件
        mBinding.llayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //存储搜索记录
                SearchHistoryBean data = new SearchHistoryBean();
                data.setTime(System.currentTimeMillis());
                data.setGoods(mViewModel.getIsSearchGoods().getValue());
                data.setSearchContent(name);
                SearchHistoryDataManager.insertHistory(data, mContext);
                //跳转界面
                mViewModel.getTagString().postValue(name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View ItemView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ItemView = itemView;
        }
    }
}
