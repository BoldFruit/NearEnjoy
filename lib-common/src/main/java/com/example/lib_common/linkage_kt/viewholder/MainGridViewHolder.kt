package com.example.lib_common.linkage_kt.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ListAdapter
import android.widget.TextView
import com.example.lib_common.R
import com.example.lib_common.databinding.CommonLinkdataMainContentGridBinding
import com.example.lib_common.linkage_kt.adapter.ContentAdapter
import com.example.lib_common.linkage_kt.adapter.ContentGridAdapter
import com.example.lib_common.linkage_kt.data.LinkData
import okhttp3.internal.notify


/**
 * Time:2020/4/10 15:09
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class MainGridViewHolder(itemView: View): BaseBindingVH<CommonLinkdataMainContentGridBinding, LinkData>(itemView = itemView) {

    lateinit var itemClickListener: ContentAdapter.OnItemClickListener
    fun bindViewData(data: LinkData, itemClickListener: ContentAdapter.OnItemClickListener, position: Int) {
        this.itemClickListener = itemClickListener
        bindViewData(data)
        dataBinding?.linkDataMainGridMoreTxt?.setOnClickListener {
            itemClickListener.onMainCtgItemClick(data, position, it)
        }

    }

    override fun bindViewData(data: LinkData) {
        val gridAdapter = ContentGridAdapter(data.secondList, itemView.context, itemClickListener)
        dataBinding?.gridlinkDatas = data
        dataBinding?.linkDataMainGridView?.adapter = gridAdapter
        setListViewHeightBasedOnChildren(dataBinding!!.linkDataMainGridView)
        dataBinding?.executePendingBindings()
    }

    fun setListViewHeightBasedOnChildren(listView: GridView) { // 獲取listview的adapter
        val listAdapter: ListAdapter = listView.getAdapter() ?: return
        // 固定列寬，有多少列
        val col = 3 // listView.getNumColumns();
        var totalHeight = 0
        // i每次加4，相當於listAdapter.getCount()小於等於4時 迴圈一次，計算一次item的高度，
// listAdapter.getCount()小於等於8時計算兩次高度相加
        var i = 0
        while (i < listAdapter.getCount()) {
            // 獲取listview的每一個item
            val listItem: View = listAdapter.getView(i, null, listView)
            listItem.measure(0, 0)
            // 獲取item的高度和
            totalHeight += listItem.measuredHeight
            i += col
        }
        // 獲取listview的佈局引數
        val params: ViewGroup.LayoutParams = listView.getLayoutParams()
        // 設定高度
        params.height = totalHeight
        // 設定引數
        listView.setLayoutParams(params)
    }
}