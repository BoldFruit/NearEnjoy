package com.example.lib_common.linkage_kt.viewholder

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.lib_common.linkage_kt.data.IBaseLinkData

/**
 * Time:2020/4/10 8:55
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
abstract class BaseBindingVH<D: ViewDataBinding, T: IBaseLinkData>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var dataBinding: D? = null

    init {
        this.dataBinding = DataBindingUtil.bind(itemView)
    }

    abstract fun bindViewData(data: T)

}