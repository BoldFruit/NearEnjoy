package com.example.found.paging.adapter.viewHolder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.found.R
import com.example.found.databinding.ItemBottomBinding
import com.example.found.paging.NetworkState
import com.example.found.paging.Status

/**
 * @author DuLong
 * @since 2020/3/14
 * email 798382030@qq.com
 */
class BottomViewHolder(private val mDataBinding: ItemBottomBinding,
                       private val retryCallBack: () -> Unit)
    : RecyclerView.ViewHolder(mDataBinding.root) {

    private var firstLoaded = true

    fun bindTo(networkState: NetworkState?) {

        when (networkState?.status) {
            Status.FAILED -> mDataBinding.txtBottom.text = "点击重新加载"
            Status.SUCCESS -> mDataBinding.txtBottom.text = "到底了"
            else -> ""
        }
        if (!firstLoaded) {
            mDataBinding.txtBottom.visibility = toVisibility(networkState?.status != Status.RUNNING)
            mDataBinding.leftDot.visibility = toVisibility(networkState?.status != Status.RUNNING)
            mDataBinding.rightDot.visibility = toVisibility(networkState?.status != Status.RUNNING)
        } else {
            firstLoaded = false
            mDataBinding.txtBottom.visibility = GONE
            mDataBinding.leftDot.visibility = GONE
            mDataBinding.rightDot.visibility = GONE
        }
        mDataBinding.progressBar.visibility = toVisibility(networkState?.status == Status.RUNNING)
        mDataBinding.btnRetry.visibility = GONE;
        if (networkState?.status == Status.FAILED) {
            mDataBinding.txtBottom.setOnClickListener { retryCallBack.invoke() }
        }
    }

    companion object {
        fun create(parent: ViewGroup, retryCallBack: () -> Unit): BottomViewHolder {
             val mDataBinding = DataBindingUtil.inflate<ItemBottomBinding>(LayoutInflater.from(parent.context), R.layout.item_bottom, parent, false)
            return BottomViewHolder(mDataBinding, retryCallBack)
        }

        fun toVisibility(constraint : Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }



}