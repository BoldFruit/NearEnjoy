package com.example.mvvm_simple.view

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.base.view.activity.MvvmBaseActivity
import com.example.base.view.activity.MvvmNetworkActivity
import com.example.lib_common.statusbar.StatusBarUtil
import com.example.mvvm_simple.R
import com.example.mvvm_simple.databinding.ActivityOnsaleBinding
import com.example.mvvm_simple.model.bean.UserGoodsBean
import com.example.mvvm_simple.view.adapter.OnSaleUserGoodsAdapter
import com.example.mvvm_simple.view.paging.NetworkState
import com.example.mvvm_simple.view.paging.Status
import com.example.mvvm_simple.view.paging.repository.UserGoodRepository
import com.example.mvvm_simple.viewmodel.OnSaleViewModel
import kotlinx.android.synthetic.main.activity_onsale.*

import java.util.concurrent.Executors

/**
 * @author DuLong
 * @since 2020/4/17
 * email 798382030@qq.com
 */
/**
 * 用户在售界面
 */
class OnSaleActivity: MvvmNetworkActivity<ActivityOnsaleBinding, OnSaleViewModel>(){
    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, OnSaleActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var  mAdapter: OnSaleUserGoodsAdapter
    private val executors = Executors.newFixedThreadPool(5)

    override fun getBindingVariable(): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_onsale
    }

    override fun getViewModel(): Class<out ViewModel> {
        return OnSaleViewModel::class.java
    }

    override fun initParameters() {

    }

    override fun initDataAndView() {
        img_back.setOnClickListener {
            this.finish()
        }
        StatusBarUtil.setTranslucentStatus(this)
        StatusBarUtil.setStatusBarDarkTheme(this, true)
        initPaging()
    }

    private fun initPaging() {
        val repository = UserGoodRepository(executors)
        var listing = repository.createListing(pageSize = 10, initialLoadKey = 1)
        mAdapter = OnSaleUserGoodsAdapter(this) {
            listing.retry.invoke()
        }
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = mAdapter
        listing.networkState.observe(this, Observer {
        })
        listing.pagedList.observe(this, Observer {
            mAdapter.submitList(it){
                val position: Int = LinearLayoutManager(this).findFirstCompletelyVisibleItemPosition()
                if (position != RecyclerView.NO_POSITION) {
                    mViewDataBinding.recyclerview.scrollToPosition(position)
                }
            }
        })
        /**
         * 监听初始状态，设置占位图
         */
        listing.refreshState.observe(this, Observer {
            when (it.status) {
                Status.NO_DATA -> {
                    mViewDataBinding.recyclerview.visibility = View.GONE
                    mViewDataBinding.relayoutPlaceholder.visibility = View.VISIBLE
                }
                Status.FAILED -> {
                    mViewDataBinding.relayoutPlaceholder.visibility = View.VISIBLE
                    mViewDataBinding.recyclerview.visibility =View.GONE
                    mViewDataBinding.txtNote.text = "网络错误，点击重试"
                    mViewDataBinding.txtNote.setOnClickListener{
                        listing.retry.invoke()
                    }
                }
                Status.RUNNING -> {
                    mViewDataBinding.recyclerview.visibility = View.GONE
                    mViewDataBinding.relayoutPlaceholder.visibility = View.VISIBLE
                    mViewDataBinding.txtNote.text = "加载中。。。"
                }
                Status.SUCCESS -> {
                    mViewDataBinding.recyclerview.visibility =View.VISIBLE
                    mViewDataBinding.relayoutPlaceholder.visibility = View.GONE
                }
            }
        })
    }
}