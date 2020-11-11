package com.example.mvvm_simple.view.fragment

import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.example.base.utils.ToastUtil
import com.example.mvvm_simple.R
import com.example.mvvm_simple.databinding.FragmentMarketItemBinding
import com.example.mvvm_simple.model.bean.ClassificationBean
import com.example.mvvm_simple.model.bean.FirstClassificationBean
import com.example.mvvm_simple.view.adapter.FirstClassificationAdapter
import com.example.mvvm_simple.view.paging.Listing
import com.example.mvvm_simple.view.paging.Status
import com.example.mvvm_simple.view.paging.repository.FirstClassificationRepository
import okhttp3.internal.notifyAll
import java.util.concurrent.Executors

/**
 * @author DuLong
 * @since 2020/4/8
 * email 798382030@qq.com
 */
/**
 * 实现了懒加载的fragment
 * 只有在view创建好了并且可见的时候才加载数据
 */
class MarketItemFragment(private val categoryId: String, private val onRecyclerScroll: (t: Float) -> Unit): Fragment() {

    private lateinit var mContext: Context
    private lateinit var mLayoutManager: StaggeredGridLayoutManager
    //用于网络请求的线程池
    private val executor = Executors.newFixedThreadPool(5)
    private lateinit var mAdapter: FirstClassificationAdapter
    private lateinit var mViewDataBinding: FragmentMarketItemBinding
    //记录是否布局加载完毕，并且布局可见
    private var isViewCreated: MutableLiveData<Boolean> = MutableLiveData(false)
    //记录是否可见
    private var isVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    private lateinit var listing: Listing<FirstClassificationBean.SearchListBean>
    //判断是否布局加载成功了
    var hasLoaded = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //监听，只有当view可见，且view创建完毕后才能初始化，即懒加载
        isViewCreated.observe(viewLifecycleOwner, Observer {
            if (it && isVisible.value == true) {
                init()
            }
        })
        isVisible.observe(viewLifecycleOwner, Observer {
            if (it && isViewCreated.value == true) {
                init()
            }
        })
        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_market_item, container, false)
        return mViewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isViewCreated.value = true
        super.onViewCreated(view, savedInstanceState)
        mContext = requireActivity()
    }

    /**
     * 初始化Paging
     */
    private fun init() {
        //初始化recyclerView
        mLayoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        mViewDataBinding.recyclerview.layoutManager = mLayoutManager
        //监听Listing中的数据变化
        val repository = FirstClassificationRepository(executor)
        listing = repository.createListing(categoryId, 10)
        mAdapter = FirstClassificationAdapter(requireContext()) { listing.retry.invoke()}
        mViewDataBinding.recyclerview.adapter = mAdapter
        listing.pagedList.observe(viewLifecycleOwner, Observer<PagedList<FirstClassificationBean.SearchListBean>> { t ->
            mAdapter.submitList(t){
                val firstItems = IntArray(2)
                (mViewDataBinding.recyclerview.layoutManager as StaggeredGridLayoutManager).findFirstCompletelyVisibleItemPositions(firstItems)
                val position: Int = getMinPosition(firstItems)
                if (position != RecyclerView.NO_POSITION) {
                    mViewDataBinding.recyclerview.scrollToPosition(position)
                }
            }
        })
        listing.networkState.observe(viewLifecycleOwner, Observer {
            mAdapter.setNetWorkState(it)
        })
        //设置当网络请求失败，或者数据为空时的页面
        listing.refreshState.observe(viewLifecycleOwner, Observer {
            mViewDataBinding.container.visibility = toVisible(it.status != Status.SUCCESS)
            mViewDataBinding.btnRetry.visibility = toVisible(it.status == Status.FAILED)
            mViewDataBinding.img.visibility = toVisible(it.status == Status.NO_DATA || it.status == Status.FAILED)
            mViewDataBinding.txtProblem.visibility = toVisible(it.status == Status.NO_DATA || it.status == Status.FAILED)
            mViewDataBinding.txtProblem.text = if (it.status == Status.NO_DATA) "没有相关商品" else "网络请求失败了"
            mViewDataBinding.progressBar.visibility = toVisible(it.status == Status.RUNNING)
            mAdapter.setInitNetWorkState(it)
        })

        mViewDataBinding.btnRetry.setOnClickListener {
            listing.retry.invoke()
        }

        /**
         * 对RecyclerView的滑动进行监听，以此决定fab是否显示，上滑时候显示，下滑的时候隐藏
         */
        mViewDataBinding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var firstPositions = IntArray(2)
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                mLayoutManager.findFirstCompletelyVisibleItemPositions(firstPositions)
                if (getMinPosition(firstPositions) <= 0) {
                    onRecyclerScroll(1f)
                } else {
                    onRecyclerScroll(dy.toFloat())
                }
            }
        })
        //重置数据，防止重复加载
        isViewCreated.value = false
        isViewCreated.value = false
        hasLoaded = true
    }

    /**
     * 刷新paging
     */
    fun refresh(data: ClassificationBean.SecondListBean)  {
        val ids = categoryId.split(",")
        for (item in ids) {
            if (item.toInt() == data.id) {
                //清空数据
                mAdapter.submitList(null)
                listing.refresh.invoke()
                break;
            }
        }
    }

    private fun toVisible(constraint: Boolean): Int {
        return if (constraint) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun getMinPosition(positions: IntArray): Int {
        var min = positions[0]
        for (i in 1 until positions.size) {
            if (positions[i] < min) {
                min = positions[i]
            }
        }
        return min
    }

    /**
     *使recyclerView滑动到最顶部
     */
    fun toTop() {
        mViewDataBinding.recyclerview.scrollToPosition(0)
    }

    /**
     * 这个方法可以获得Fragment的可见状况
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isVisible.value = isVisibleToUser
    }
}