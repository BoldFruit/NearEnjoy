package com.example.mvvm_simple.categories.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.lib_common.dialog.ListDialog
import com.example.mvvm_simple.R
import com.example.mvvm_simple.model.bean.FirstClassificationBean

/**
 * Time:2020/4/13 20:29
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class CategoryPagingAdapter: PagedListAdapter<FirstClassificationBean.SearchListBean, RecyclerView.ViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return when(viewType) {
            Int.MIN_VALUE -> CategoryFooterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_category_footer, parent, false))
           else -> CategoryViewHolder.create(getItem(viewType), parent, object : CategoryWithFooterAdapter.OnGoodsItemClick {
               override fun onClick(goodId: Int) {

               }

           })
        }
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.registerAdapterDataObserver(observer)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//       if (itemCount == 1) {
//           val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
//           layoutParams.isFullSpan = true
//       } else {
//           if (position < itemCount - 1 ) {
//               (holder as CategoryViewHolder).bindTo(getItem(position), position)
//           } else {
//               val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
//               layoutParams.isFullSpan = true
//           }
//       }
        if (position < itemCount - 1 ) {
            (holder as CategoryViewHolder).bindTo(getItem(position), position)
        } else {
            val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            layoutParams.isFullSpan = true
        }
    }


    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1) {
            return Int.MIN_VALUE
        }
        return position
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    companion object {

        val DIFF = object : DiffUtil.ItemCallback<FirstClassificationBean.SearchListBean>() {
            override fun areItemsTheSame(oldItem: FirstClassificationBean.SearchListBean, newItem: FirstClassificationBean.SearchListBean): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: FirstClassificationBean.SearchListBean, newItem: FirstClassificationBean.SearchListBean): Boolean = oldItem.id == newItem.id

        }
    }
}