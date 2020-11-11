package com.example.lib_common.linkage_kt.viewholder

import android.view.View
import com.example.lib_common.databinding.CommonLinkdataMainContentBinding
import com.example.lib_common.linkage_kt.adapter.ContentAdapter
import com.example.lib_common.linkage_kt.data.LinkData

/**
 * Time:2020/4/10 15:09
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class MainViewHolder(itemView: View): BaseBindingVH<CommonLinkdataMainContentBinding, LinkData>(itemView = itemView) {

    fun bindViewData(data: LinkData, itemClickListener: ContentAdapter.OnItemClickListener, position: Int) {
        bindViewData(data)
        dataBinding?.linkDataMainMoreTxt?.setOnClickListener {
            itemClickListener.onMainCtgItemClick(data, position, it)
        }
        (dataBinding?.linkDataMainRecycler?.adapter as ContentAdapter).addListenerToItem(itemClickListener)
    }

    override fun bindViewData(data: LinkData) {
        dataBinding?.linkDatas = data
        val contentAdapter = ContentAdapter(data.secondList)
        dataBinding?.linkDataMainRecycler?.adapter = contentAdapter
        dataBinding?.executePendingBindings()
    }
}