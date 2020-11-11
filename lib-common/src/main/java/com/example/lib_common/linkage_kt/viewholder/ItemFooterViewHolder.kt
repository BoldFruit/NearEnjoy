package com.example.lib_common.linkage_kt.viewholder

import android.view.View
import com.example.lib_common.databinding.CommonLinkdataFooterBinding
import com.example.lib_common.linkage_kt.data.ItemFooterData

/**
 * Time:2020/4/10 12:14
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class ItemFooterViewHolder(iView: View):
        BaseBindingVH<CommonLinkdataFooterBinding, ItemFooterData>(itemView = iView) {
    override fun bindViewData(data: ItemFooterData) {
        dataBinding?.footTxt = data.footStr
        dataBinding?.executePendingBindings()
    }
}