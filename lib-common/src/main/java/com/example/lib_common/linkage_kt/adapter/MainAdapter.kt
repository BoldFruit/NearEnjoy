package com.example.lib_common.linkage_kt.adapter

import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.lib_common.R
import com.example.lib_common.linkage_kt.data.DataType
import com.example.lib_common.linkage_kt.data.IBaseLinkData
import com.example.lib_common.linkage_kt.data.ItemFooterData
import com.example.lib_common.linkage_kt.data.LinkData
import com.example.lib_common.linkage_kt.viewholder.BaseBindingVH
import com.example.lib_common.linkage_kt.viewholder.ItemFooterViewHolder
import com.example.lib_common.linkage_kt.viewholder.MainViewHolder
import java.lang.IllegalArgumentException

/**
 * Time:2020/4/10 14:26
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class MainAdapter(private var dataList: ArrayList<IBaseLinkData>, var itemClickListener: ContentAdapter.OnItemClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(val type: Int = dataList[viewType].getType()) {
            DataType.ITEM_DATA -> MainViewHolder(inflateView(parent, getLayout(type)))
            DataType.FOOTER -> ItemFooterViewHolder(inflateView(parent, getLayout(type)))
            else -> throw IllegalArgumentException("can't find responding ViewHolder")
        }
    }

    private fun inflateView(parent:ViewGroup, layout: Int): View
            = LayoutInflater.from(parent.context).inflate(layout, parent, false)

    private fun getLayout(viewType: Int): Int {
        return when(viewType) {
            DataType.ITEM_DATA -> R.layout.common_linkdata_main_content
            DataType.FOOTER -> R.layout.common_linkdata_footer
            else -> throw IllegalArgumentException("can't find responding layout res")
        }

    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is MainViewHolder) {
            holder.bindViewData(dataList[position] as LinkData, itemClickListener, position)
        }

        if (holder is ItemFooterViewHolder) {
            holder.setIsRecyclable(false)
            holder.bindViewData(dataList[position] as ItemFooterData)
        }
    }

    override fun getItemViewType(position: Int): Int = position

}