package com.example.lib_common.linkage_kt.viewholder

import android.view.View
import com.bumptech.glide.Glide
import com.example.lib_common.databinding.CommonLinkdataItemMainContentBinding
import com.example.lib_common.linkage_kt.data.SecondLinkData

/**
 * Time:2020/4/10 15:23
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class ContentViewHolder(itemView: View): BaseBindingVH<CommonLinkdataItemMainContentBinding, SecondLinkData>(itemView = itemView) {
    override fun bindViewData(data: SecondLinkData) {
        dataBinding?.linkDataMainItemImg?.context?.let {
            Glide.with(it)
                    .load(data.image)
                    .into(dataBinding?.linkDataMainItemImg!!)
        }
        dataBinding?.secondLinkData = data
    }
}