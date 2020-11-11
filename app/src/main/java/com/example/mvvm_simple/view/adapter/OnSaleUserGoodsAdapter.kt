package com.example.mvvm_simple.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_simple.model.bean.UserGoodsBean
import com.example.mvvm_simple.view.adapter.viewholder.OnSaleGoodsViewHolder


/**
 * @author DuLong
 * @since 2020/5/1
 * email 798382030@qq.com
 */
class OnSaleUserGoodsAdapter(private val context: Context, private val retryCallback: () -> Unit): PagedListAdapter<UserGoodsBean.SearchListBean, RecyclerView.ViewHolder>(GOODS_COMPARATOR){
    companion object {
        private val PAYLOAD = Any()

        val GOODS_COMPARATOR = object : DiffUtil.ItemCallback<UserGoodsBean.SearchListBean>() {
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: UserGoodsBean.SearchListBean, newItem: UserGoodsBean.SearchListBean): Boolean =
                    oldItem == newItem


            override fun areItemsTheSame(oldItem: UserGoodsBean.SearchListBean, newItem: UserGoodsBean.SearchListBean): Boolean =
                    oldItem.id == newItem.id

            //todo 在特定情况下放回payload,使对应的item实现局部刷新
            override fun getChangePayload(oldItem: UserGoodsBean.SearchListBean, newItem: UserGoodsBean.SearchListBean): Any? {
                return null}

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OnSaleGoodsViewHolder.create(parent, context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OnSaleGoodsViewHolder).bindTo(getItem(position))
    }

}