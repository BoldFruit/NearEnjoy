package com.example.mvvm_simple.categories.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mvvm_simple.R
import com.example.mvvm_simple.model.bean.FirstClassificationBean

/**
 * Time:2020/4/13 20:29
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class CategoryWithFooterAdapter(var clickListener: OnGoodsItemClick): PagedListAdapter<FirstClassificationBean.SearchListBean, RecyclerView.ViewHolder>(DIFF) {

    interface OnGoodsItemClick {
        fun onClick(goodId: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return when(viewType) {
           FOOTER_TYPE -> CategoryFooterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_category_footer, parent, false))
           NULL_TYPE -> CategoryFooterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_paging_null_footer, parent, false))
           else -> CategoryViewHolder.create(getItem(viewType), parent, listener = clickListener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItem(position)?.id == -1 ) {
            if (position == itemCount - 1) {
                val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                layoutParams.isFullSpan = true
            }
        } else {
            (holder as CategoryViewHolder).bindTo(getItem(position), position)
        }
    }


    override fun getItemViewType(position: Int): Int {
        if (getItem(position)?.id == -1 ) {
            return if (position == itemCount - 1) {
                FOOTER_TYPE
            } else {
                NULL_TYPE
            }
        }
        return position
    }



    companion object {

        val DIFF = object : DiffUtil.ItemCallback<FirstClassificationBean.SearchListBean>() {
            override fun areItemsTheSame(oldItem: FirstClassificationBean.SearchListBean, newItem: FirstClassificationBean.SearchListBean): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: FirstClassificationBean.SearchListBean, newItem: FirstClassificationBean.SearchListBean): Boolean = oldItem.id == newItem.id

        }

        const val FOOTER_TYPE = Int.MIN_VALUE
        const val NULL_TYPE = -1
    }
}