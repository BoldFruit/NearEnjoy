package com.example.lib_common.linkage_kt

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lib_common.linkage_kt.adapter.OnScrollChange
import com.example.lib_common.linkage_kt.adapter.SideListAdapter
import java.lang.IllegalArgumentException

/**
 * Time:2020/4/10 20:51
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
object LinkDataController {

    lateinit var sideListRv: RecyclerView
    lateinit var mainRv: RecyclerView
    lateinit var sideAdapter: OnScrollChange
    lateinit var mainLayoutManager: LinearLayoutManager
    lateinit var sideLayoutManager: LinearLayoutManager
    var isScroll = true

    fun setSideRV(side: RecyclerView, sideAdapter: OnScrollChange): LinkDataController {
        this.sideAdapter = sideAdapter
        sideListRv = side
        //取消点击动画
        sideListRv.itemAnimator?.changeDuration = 0
        sideLayoutManager = sideListRv.layoutManager as LinearLayoutManager
        return this
    }

    fun setManRV(main: RecyclerView): LinkDataController {
        mainRv = main
        mainLayoutManager = mainRv.layoutManager as LinearLayoutManager
        return this
    }

    fun setUpTogether() {
        mainRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var firstPosition: Int = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var lastItemPosition: Int = 0
                var firstItemPosition: Int = 0
                lastItemPosition = mainLayoutManager.findLastVisibleItemPosition()
                firstItemPosition = mainLayoutManager.findFirstVisibleItemPosition()

                if (isScroll) sideAdapter.setSelectedPosition(firstItemPosition)
                else isScroll = !isScroll

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isScroll = newState == RecyclerView.SCROLL_STATE_DRAGGING

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position: Int = mainLayoutManager.findFirstVisibleItemPosition()
                    val top: Int? = mainLayoutManager.getChildAt(position)?.top
                    val height: Int? = mainLayoutManager.getChildAt(position)?.height
                    if (top != null) {
                        if (top < 0.1 * height!!)
                            mainLayoutManager.scrollToPositionWithOffset(position, 0)
                    }

                }
            }
        })

        (sideAdapter as SideListAdapter).addItemClickListener(object : SideListAdapter.OnSideItemClickListener {
            override fun onClickSideItem(position: Int) {
                isScroll = false
                mainLayoutManager.scrollToPositionWithOffset(position, 0)
//                mainRv.scrollToPosition(position)
            }

        })
    }


}