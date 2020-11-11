package com.example.lib_common.linkage_kt.viewholder

import android.view.View
import com.example.lib_common.databinding.CommonLinkdataItemHeaderBinding
import com.example.lib_common.linkage_kt.data.ItemHeaderData

/**
 * Time:2020/4/10 12:07
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class ItemHeaderViewHolder(view: View):
        BaseBindingVH<CommonLinkdataItemHeaderBinding, ItemHeaderData>(itemView = view) {

    override fun bindViewData(data: ItemHeaderData) {
        dataBinding?.title = data.title
    }
}