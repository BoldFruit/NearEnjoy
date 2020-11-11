package com.example.mvvm_simple.view

import android.app.Service
import android.content.Intent
import android.os.AsyncTask
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.base.model.bean.BaseNetworkStatus
import com.example.base.view.activity.MvvmNetworkActivity
import com.example.lib_common.linkage_kt.LinkDataController
import com.example.lib_common.linkage_kt.adapter.ContentAdapter
import com.example.lib_common.linkage_kt.adapter.MainAdapter
import com.example.lib_common.linkage_kt.adapter.SideListAdapter
import com.example.lib_common.linkage_kt.data.LinkData
import com.example.lib_common.linkage_kt.data.LinkDataClassifier
import com.example.lib_common.linkage_kt.data.SecondLinkData
import com.example.mvvm_simple.R
import com.example.mvvm_simple.databinding.ActivityCategoriesBinding
import com.example.mvvm_simple.categories.mvvm.model.CategoriesModel
import com.example.mvvm_simple.viewmodel.CategoriesViewModel
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicStampedReference
import kotlin.collections.ArrayList

/**
 * Time:2020/4/10 15:33
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class CategoriesActivity: MvvmNetworkActivity<ActivityCategoriesBinding, CategoriesViewModel>() {
    private lateinit var listMutableLiveData: MutableLiveData<ArrayList<LinkData>>
    private lateinit var adapter: MainAdapter
    private lateinit var sideAdapter: SideListAdapter
    private var isScroll = false
    private var heightList: ArrayList<Int> = ArrayList()
    private val listObserver = Observer<ArrayList<LinkData>> {
        values -> values?.let {
        adapter = MainAdapter(LinkDataClassifier.getLinkDataOriginal(it), object : ContentAdapter.OnItemClickListener {
            override fun onCtgItemClick(data: SecondLinkData, position: Int, itemView: View) {

            }

            override fun onMainCtgItemClick(data: LinkData, position: Int, itemView: View) {

            }

        })
        sideAdapter = SideListAdapter(LinkDataClassifier.getSideList(it))
//        sideAdapter.addItemClickListener(object : SideListAdapter.OnSideItemClickListener {
//            override fun onClickSideItem(position: Int) {
//                isScroll = false
//                var height = 0
//                (mViewDataBinding.activityCategoriesRecycler.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
//
//                mViewDataBinding.activityCategoriesNested.scrollBy(0, )
//            }
//        })
        mViewDataBinding.activityCategoriesRecycler.adapter = adapter
        mViewDataBinding.activityCategoriesSideRecycler.adapter = sideAdapter
        val categoriesRecycler = mViewDataBinding.activityCategoriesRecycler
//        val childCount = categoriesRecycler.adapter?.itemCount
//        heightList[0] = 0
//        for (i in 1 until childCount - 1) {
//            heightList[i] = heightList[i - 1] + categoriesRecycler.getChildAt(i).height
//        }

//        val footerView = categoriesRecycler.getChildAt(childCount!! - 1)
//        val footerLayoutParams = footerView.layoutParams
//        footerLayoutParams.height += mViewDataBinding.activityCategoriesNested.measuredHeight -
//                categoriesRecycler.getChildAt(childCount - 2).measuredHeight
//        footerView.layoutParams = footerLayoutParams

//        mViewDataBinding.activityCategoriesNested
//                .setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener
//                { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//                    if (isScroll) {
//                        for (i in adapter.heightList.indices) {
//                            if (scrollY == adapter.heightList[i]) {
//                                sideAdapter.setSelectedPosition(i)
//                                break
//                            }
//                        }
//                    }
//                    else isScroll = true
//                })

        LinkDataController.setManRV(mViewDataBinding.activityCategoriesRecycler)
                .setSideRV(mViewDataBinding.activityCategoriesSideRecycler, sideAdapter)
                .setUpTogether()
      }
    }
    override fun getLayoutId(): Int = R.layout.activity_categories

    override fun getViewModel(): Class<out ViewModel> = CategoriesViewModel::class.java

    override fun getBindingVariable(): Int = 0

    override fun initParameters() {

    }

    override fun initDataAndView() {
       mViewModel.loadLinkList()
        (mViewModel.models.get(CategoriesModel.TAG)?.modelLiveData
                as MutableLiveData<ArrayList<LinkData>>).observe(this, listObserver)
//        mViewDataBinding.activityCategoriesRecycler.isNestedScrollingEnabled = false

        setUpLinkAgeList()

        makeMargin()
    }

    private fun setUpLinkAgeList() {


    }

    private fun makeMargin() {
//        val layoutParams = mViewDataBinding.activityCategoriesRecycler.layoutParams as LinearLayout.LayoutParams
//        layoutParams.bottomMargin = mViewDataBinding
//                .activityCategoriesRecycler.layoutManager
//                ?.getChildAt(mViewDataBinding.activityCategoriesRecycler.childCount - 1)
//                ?.top ?: 0
    }

    override fun onNetDone(key: String?, status: BaseNetworkStatus?) {
        super.onNetDone(key, status)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}