package com.example.lib_common.linkage_kt.viewholder

import android.view.View
import com.example.lib_common.databinding.CommonLinkdataItemMainContentBinding
import com.example.lib_common.linkage_kt.data.SecondLinkData

/**
 * Time:2020/4/10 11:16
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class MainContentViewHolder(itemView: View):
        BaseBindingVH<CommonLinkdataItemMainContentBinding, SecondLinkData>(itemView = itemView) {

    override fun bindViewData(data: SecondLinkData) {
        dataBinding?.secondLinkData = data
    }
}