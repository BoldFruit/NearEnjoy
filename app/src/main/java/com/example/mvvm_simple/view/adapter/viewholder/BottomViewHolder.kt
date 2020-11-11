package com.example.mvvm_simple.view.adapter.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_simple.R
import com.example.mvvm_simple.databinding.MainItemBottomBinding
import com.example.mvvm_simple.view.paging.NetworkState
import com.example.mvvm_simple.view.paging.Status


class BottomViewHolder(private val mDataBinding: MainItemBottomBinding,
                       private val retryCallBack: () -> Unit)
    : RecyclerView.ViewHolder(mDataBinding.root) {


    fun bindTo(networkState: NetworkState?, initNetworkState: NetworkState?) {
        //只有当第一次加载成功的时候才会显示最后一个item
        if (initNetworkState == NetworkState.LOADED) {
            mDataBinding.root.visibility = VISIBLE
            when (networkState?.status) {
                Status.FAILED -> mDataBinding.txtBottom.text = "点击重新加载"
                Status.SUCCESS -> mDataBinding.txtBottom.text = "到底了"
                else -> Unit
            }

            mDataBinding.txtBottom.visibility = toVisibility(networkState?.status != Status.RUNNING)
            mDataBinding.leftDot.visibility = toVisibility(networkState?.status != Status.RUNNING)
            mDataBinding.rightDot.visibility = toVisibility(networkState?.status != Status.RUNNING)

            mDataBinding.progressBar.visibility = toVisibility(networkState?.status == Status.RUNNING)
            mDataBinding.btnRetry.visibility = GONE;
            if (networkState?.status == Status.FAILED) {
                mDataBinding.txtBottom.setOnClickListener { retryCallBack.invoke() }
            }
        } else {
            mDataBinding.root.visibility = GONE
        }
    }

    companion object {
        fun create(parent: ViewGroup, retryCallBack: () -> Unit): BottomViewHolder {
             val mDataBinding = DataBindingUtil.inflate<MainItemBottomBinding>(LayoutInflater.from(parent.context), R.layout.main_item_bottom, parent, false)
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