package com.example.found.paging.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.found.R
import com.example.found.model.bean.SearchUserResponse
import com.example.found.paging.NetworkState
import com.example.found.paging.adapter.viewHolder.BottomViewHolder
import com.example.found.paging.adapter.viewHolder.UserBottomViewHolder
import com.example.found.paging.adapter.viewHolder.UserViewHolder
import io.reactivex.Observable
import java.lang.Error
import java.lang.Exception
import java.util.*
import kotlin.math.log

/**
 * @author DuLong
 * @since 2020/3/27
 * email 798382030@qq.com
 */
class UserDetailAdapter(private val retryCallback: () -> Unit): PagedListAdapter<SearchUserResponse, RecyclerView.ViewHolder>(GOODS_COMPARATOR){

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_bottom -> UserBottomViewHolder.create(parent, retryCallback)
            R.layout.item_user -> UserViewHolder.create(parent)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_user -> (holder as UserViewHolder).bindTo(getItem(position), position)
            R.layout.item_bottom -> (holder as UserBottomViewHolder).bindTo(networkState)
        }
    }

    //todo:用于一些item的局部刷刷新
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
        } else {
            onBindViewHolder(holder, position)
        }
    }



    companion object {
        private val PAYLOAD = Any()
        val GOODS_COMPARATOR = object : DiffUtil.ItemCallback<SearchUserResponse>() {
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: SearchUserResponse, newItem: SearchUserResponse): Boolean =
                    oldItem == newItem


            override fun areItemsTheSame(oldItem: SearchUserResponse, newItem: SearchUserResponse): Boolean =
                    oldItem.id == newItem.id

            //todo 在特定情况下放回payload,使对应的item实现局部刷新
            override fun getChangePayload(oldItem: SearchUserResponse, newItem: SearchUserResponse): Any? {
                return null}

        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    /**
     * 当网络在加载时，或者加载失败时才展示加载条
     */
    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_bottom
        } else R.layout.item_user
    }

    /**
     * 外界设置网络状态的接口
     */
    fun setNetWorkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hasExtraRow != hadExtraRow) {
            if (hasExtraRow) {
                notifyItemInserted(super.getItemCount())
            } else {
                notifyItemRemoved(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}