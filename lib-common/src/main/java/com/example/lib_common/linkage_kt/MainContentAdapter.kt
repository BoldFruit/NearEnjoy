package com.example.lib_common.linkage_kt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.lib_common.R
import com.example.lib_common.linkage_kt.data.DataType
import com.example.lib_common.linkage_kt.data.IBaseLinkData
import com.example.lib_common.linkage_kt.viewholder.BaseBindingVH
import com.example.lib_common.linkage_kt.viewholder.ItemFooterViewHolder
import com.example.lib_common.linkage_kt.viewholder.ItemHeaderViewHolder
import com.example.lib_common.linkage_kt.viewholder.MainContentViewHolder
import java.lang.IllegalArgumentException

/**
 * Time:2020/4/10 8:54
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class MainContentAdapter(private var linkList: ArrayList<IBaseLinkData>):
        RecyclerView.Adapter<BaseBindingVH<ViewDataBinding, IBaseLinkData>>() {
    var maxSpan: Int = 3
    var itemHeaderSpan = 3
    var itemSpan = 1

    override fun onBindViewHolder(holder: BaseBindingVH<ViewDataBinding, IBaseLinkData>, position: Int) {
       holder.bindViewData(linkList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingVH<ViewDataBinding, IBaseLinkData> {
       when(viewType) {
           DataType.ITEM_DATA -> MainContentViewHolder(inflateView(parent, getLayout(viewType)))
           DataType.ITEM_HEADER -> ItemHeaderViewHolder(inflateView(parent, getLayout(viewType)))
           DataType.FOOTER -> ItemFooterViewHolder(inflateView(parent, getLayout(viewType)))
       }
        throw IllegalArgumentException("can't find the corresponding ViewHolder")
    }

    private fun getLayout(viewType: Int): Int {
        when(viewType) {
            DataType.ITEM_HEADER -> R.layout.common_linkdata_item_header
            DataType.ITEM_DATA -> R.layout.common_linkdata_item_main_content
            DataType.FOOTER -> R.layout.common_linkdata_footer
        }
        throw IllegalArgumentException("can't find the corresponding layout res")
    }

    private fun inflateView(parent:ViewGroup, layout: Int): View
            = LayoutInflater.from(parent.context).inflate(layout, parent, false)

    override fun getItemViewType(position: Int): Int = linkList[position].getType()

    override fun getItemCount(): Int = linkList.size


}