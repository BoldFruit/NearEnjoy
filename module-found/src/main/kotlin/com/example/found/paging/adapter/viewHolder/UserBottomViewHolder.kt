package com.example.found.paging.adapter.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.base.utils.ToastUtil
import com.example.found.R
import com.example.found.databinding.ItemBottomBinding
import com.example.found.paging.NetworkState
import com.example.found.paging.Status
import android.widget.Toast.makeText as makeText1

/**
 * @author DuLong
 * @since 2020/3/29
 * email 798382030@qq.com
 */
class UserBottomViewHolder(private val item: View, private val retryCallback: () -> Unit
                          )
    : RecyclerView.ViewHolder(item) {

    private val txtBottom = itemView.findViewById<TextView>(R.id.txt_bottom)
    private val leftDot = itemView.findViewById<View>(R.id.left_dot)
    private val rightDot = itemView.findViewById<View>(R.id.right_dot)
    private val progressBar = itemView.findViewById<ProgressBar>(R.id.progress_bar)
    private val btnRetry = itemView.findViewById<Button>(R.id.btn_retry)

    init {
        btnRetry.setOnClickListener {
            retryCallback()
        }
    }
    fun bindTo(networkState: NetworkState?) {
        txtBottom.visibility = View.GONE
        leftDot.visibility = View.GONE
        rightDot.visibility = View.GONE
        progressBar.visibility = toVisibility(networkState?.status == Status.RUNNING)
        btnRetry.visibility = toVisibility(networkState?.status == Status.FAILED)
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): UserBottomViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_bottom, parent, false);
            return UserBottomViewHolder(itemView, retryCallback)
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