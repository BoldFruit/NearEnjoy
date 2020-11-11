package com.example.found.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.found.R;
import com.example.found.databinding.ItemGlayoutGoodsBinding;
import com.example.found.databinding.ItemLlayoutGoodsBinding;
import com.example.found.model.bean.SearchGoodsResponse;
import java.util.Arrays;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author DuLong
 * @since 2020/3/4
 * email 798382030@qq.com
 */

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {

    private boolean isLinearLayout;
    private Context mContext;
    private List<SearchGoodsResponse.SearchListBean> mData;
    private ItemGlayoutGoodsBinding mGlayoutGoodsBinding;
    private ItemLlayoutGoodsBinding mLlayoutGoodsBinding;

    public GoodsAdapter(Context mContext, List<SearchGoodsResponse.SearchListBean> mData, boolean isLinearLayout) {
        this.mContext = mContext;
        this.mData = mData;
        this.isLinearLayout = isLinearLayout;
    }
    @NonNull
    @Override
    public GoodsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        //根据不同的排列方式，选择不同的item
        if (isLinearLayout) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_llayout_goods, parent, false);
            mLlayoutGoodsBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_llayout_goods, parent, false);
        } else {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_glayout_goods, parent, false);
            mGlayoutGoodsBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_glayout_goods, parent, false);
        }
        return new ViewHolder(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull GoodsAdapter.ViewHolder holder, int position) {
        RequestOptions options = new RequestOptions()
                .error(R.drawable.pic_placehodler)
                .placeholder(R.drawable.pic_placehodler);
        SearchGoodsResponse.SearchListBean data = mData.get(position);
        if (isLinearLayout) {
            //结合View与数据
            Glide.with(mContext).load(data.getUserAvatar()).apply(options).into(mLlayoutGoodsBinding.imgAvatar);
            mLlayoutGoodsBinding.txtGoodsName.setText(data.getName());
            mLlayoutGoodsBinding.txtPrice.setText(String.valueOf(data.getPrice()));
            //            //标签
            String[] tags = data.getDescription().split(",");
            List<String> tagList = Arrays.asList(tags);
            mLlayoutGoodsBinding.tlayoutTag.setAdapter(new MyTagAdapter(tagList, mContext, MyTagAdapter.YELLOW_TAG));
            mLlayoutGoodsBinding.txtName.setText(data.getUserName());
            mLlayoutGoodsBinding.txtNumberLove.setText(String.format("%d 想要", data.getWants()));
        } else {
            Glide.with(mContext).load(data.getUserAvatar()).apply(options).into(mGlayoutGoodsBinding.imgAvatar);
            mGlayoutGoodsBinding.txtGoodsName.setText(data.getName());
            mGlayoutGoodsBinding.txtPrice.setText(String.valueOf(data.getPrice()));
            String[] tags = data.getDescription().split(",");
            List<String> tagList = Arrays.asList(tags);
            mGlayoutGoodsBinding.tlayoutTag.setAdapter(new MyTagAdapter(tagList, mContext, MyTagAdapter.YELLOW_TAG));
            mGlayoutGoodsBinding.txtName.setText(data.getUserName());
            mGlayoutGoodsBinding.txtNumberLove.setText(String.format("%d 想要", data.getWants()));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();

    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
