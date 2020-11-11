package com.example.lib_common.linkage_kt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.lib_common.R
import com.example.lib_common.linkage_kt.data.IBaseLinkData
import com.example.lib_common.linkage_kt.data.LinkData
import com.example.lib_common.linkage_kt.data.SecondLinkData
import com.example.lib_common.linkage_kt.viewholder.BaseBindingVH
import com.example.lib_common.linkage_kt.viewholder.ContentViewHolder

/**
 * Time:2020/4/10 15:22
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class ContentAdapter(var secondList: ArrayList<SecondLinkData>): RecyclerView.Adapter<ContentViewHolder>() {

    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder = ContentViewHolder(inflateView(parent))

    private fun inflateView(parent:ViewGroup): View
            = LayoutInflater.from(parent.context).inflate(R.layout.common_linkdata_item_main_content, parent, false)

    override fun getItemCount(): Int = secondList.size
    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bindViewData(secondList[position])
        holder.itemView.setOnClickListener {
            listener.onCtgItemClick(secondList[position], position, it)
        }
    }

    override fun getItemViewType(position: Int) = position

    interface OnItemClickListener {
        fun onCtgItemClick(data: SecondLinkData, position: Int, itemView: View)
        fun onMainCtgItemClick(data: LinkData, position: Int, itemView: View)
    }

    fun addListenerToItem(listener: OnItemClickListener) {
        this.listener = listener
    }
}