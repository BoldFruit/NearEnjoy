package com.example.lib_common.linkage_kt.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lib_common.R
import com.example.lib_common.databinding.CommonLinkdataItemSideBinding

/**
 * Time:2020/4/10 19:09
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class SideListAdapter(var titleList: ArrayList<String>): RecyclerView.Adapter<SideListAdapter.SideListViewHolder>(),
        OnScrollChange {
    private var curSelectedPosition: Int = 0
    interface OnSideItemClickListener {
        fun onClickSideItem(position: Int)
    }

    private var sideClickListener: OnSideItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SideListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.common_linkdata_item_side, parent, false)
        view.setOnClickListener {
            val lastSelected = curSelectedPosition
            curSelectedPosition = it.tag as Int
            notifyItemChanged(lastSelected)
            notifyItemChanged(curSelectedPosition)
            sideClickListener?.onClickSideItem(it.tag as Int)

        }
        return SideListViewHolder(view)
    }

    override fun getItemCount(): Int = titleList.size

    override fun onBindViewHolder(holder: SideListViewHolder, position: Int) {
        holder.itemView.tag = position
        val isSelected: Boolean = curSelectedPosition == position
        holder.dataBinding?.title = titleList[position]
        holder.dataBinding?.linkDataSideBackground?.setBackgroundColor(getBgColor(isSelected))
        holder.dataBinding?.linkDataSideTitle?.setTextColor(getTitleColor(isSelected))
    }

    private fun getBgColor(isSelected: Boolean): Int {
        return if (isSelected) {
            Color.parseColor("#00000000")
        } else {
            Color.parseColor("#ffffff")
        }
    }

    private fun getTitleColor(isSelected: Boolean): Int {
        return if (isSelected) {
            Color.parseColor("#ffa500")
        } else{
            Color.parseColor("#000000")
        }
    }

    override fun setSelectedPosition(position: Int) {
        if (position < titleList.size) {
            val lastSelected = curSelectedPosition
            curSelectedPosition = position
            notifyItemChanged(lastSelected)
            notifyItemChanged(curSelectedPosition)
        }
    }

    class SideListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var dataBinding: CommonLinkdataItemSideBinding? = DataBindingUtil.bind(itemView)
    }

    fun addItemClickListener(listener: OnSideItemClickListener) {
        sideClickListener = listener
    }

}