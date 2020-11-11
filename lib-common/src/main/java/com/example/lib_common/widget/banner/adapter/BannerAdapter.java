package com.example.lib_common.widget.banner.adapter;

import android.view.ViewGroup;

import com.example.lib_common.widget.banner.IViewHolder;
import com.example.lib_common.widget.banner.listener.OnBannerListener;
import com.example.lib_common.widget.banner.util.BannerUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;


public abstract class BannerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements IViewHolder<T, VH> {
    protected List<T> mDatas = new ArrayList<>();
    private OnBannerListener listener;

    public BannerAdapter(List<T> datas) {
        setDatas(datas);
    }

    public void setDatas(List<T> datas) {
        mDatas.clear();
        //防止空对象
        if (datas == null) {
            datas = new ArrayList<>();
        }
        mDatas.addAll(datas);
        int count = datas.size();
        //这里很奇妙啊，
        if (count > 1) {
            mDatas.add(0, datas.get(count - 1));
            mDatas.add(datas.get(0));
        }
        notifyDataSetChanged();
    }

    public T getData(int position) {
        return mDatas.get(position);
    }

    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        int real = BannerUtils.getRealPosition(position, getRealCount());
        onBindView(holder, mDatas.get(position), real, getRealCount());
        if (listener != null)
            holder.itemView.setOnClickListener(view -> listener.OnBannerClick(mDatas.get(position), real));
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //只是多了一层封装
        return onCreateHolder(parent, viewType);
    }

    @Override
    public int getItemCount() {
        //这里并非真实的数量
        return mDatas.size();
    }

    public int getRealCount() {
        int count = mDatas.size();
        return count <= 1 ? count : count - 2;
    }

    //包括设置点击的监听以及页面跳转的监听
    public void setOnBannerListener(OnBannerListener listener) {
        this.listener = listener;
    }
}