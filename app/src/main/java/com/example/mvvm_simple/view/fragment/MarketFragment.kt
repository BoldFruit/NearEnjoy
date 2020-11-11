package com.example.mvvm_simple.view.fragment

import android.content.Context
import android.content.Intent
import android.media.session.MediaSession
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.arouter.ARouter
import com.example.arouter.Constants
import com.example.base.model.bean.BaseNetworkStatus
import com.example.base.view.fragment.LazyMvvmNetworkFragment
import com.example.base.view.fragment.MvvmNetworkFragment
import com.example.lib_common.statusbar.StatusBarUtil
import com.example.lib_common.util.DensityUtil
import com.example.lib_common.widget.banner.indicator.SlideIndicator
import com.example.lib_common.widget.banner.transformer.MultiplePagerScaleInTransformer
import com.example.lib_common.widget.banner.util.BannerUtils
import com.example.lib_data.data_user.UserInfoIO
import com.example.lib_data.data_user.token.TokenManager
import com.example.mvvm_simple.R
import com.example.mvvm_simple.categories.mvvm.view.CategoriesFragment
import com.example.mvvm_simple.categories.mvvm.view.CategoriesHostActivity
import com.example.mvvm_simple.databinding.FragmentMarketBinding
import com.example.mvvm_simple.model.SpData
import com.example.mvvm_simple.model.bean.ClassificationBean
import com.example.mvvm_simple.model.bean.LabelCategoriesBean
import com.example.mvvm_simple.model.bean.MainCategoryBean
import com.example.mvvm_simple.model.bean.RotationChartsBean
import com.example.mvvm_simple.model.models.GetAllLabelModel
import com.example.mvvm_simple.model.models.GetClassificationsModel
import com.example.mvvm_simple.model.models.GetLabelCategoriesModel
import com.example.mvvm_simple.view.WebViewActivity
import com.example.mvvm_simple.view.adapter.MyBannerAdapter
import com.example.mvvm_simple.view.adapter.MyFragmentPagerAdapter
import com.example.mvvm_simple.viewmodel.MarketViewModel

/**
 * Time:2020/3/2 11:23
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class MarketFragment :LazyMvvmNetworkFragment<FragmentMarketBinding, MarketViewModel>() {
    companion object {
        const val MARKET_FRAGMENT_IDS = "market_fragment_ids"
        const val MARKET_FRAGMENT_NAME = "market_fragment_name"
    }
    private lateinit var mContext: Context
    private lateinit var mFirstClassifications: HashMap<Int, String>
    private lateinit var mAllLabels: HashMap<Int, String>
    private lateinit var mLabelCategories: MediatorLiveData<List<LabelCategoriesBean>>
    private var mClassifications: List<ClassificationBean>? = ArrayList()
    private var mTabSize: Int = 0
    //判断sp中是否缓存了标签
    private var mHasLabels = MutableLiveData<Boolean>(false)
    //判断sp中是否缓存了分类
    private var mHasClassification = MutableLiveData<Boolean>(false)
    private var mHasLabelCategories = MutableLiveData<Boolean>(false)
    private lateinit var mPagerAdapter: MyFragmentPagerAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_market
    }

    override fun getViewModel(): Class<out ViewModel?> {
        return MarketViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return 0
    }

    override fun getFragmentTag(): String {
        return "MarketFragment"
    }

    override fun initParameters() {}

    override fun initDataAndView() {
        mContext = this.requireActivity()
        mViewDataBinding?.eventPresenter = EventPresenter()
//        //动态设置顶部margin,为statusBar腾出空间
//        val lp = mViewDataBinding.appbar.layoutParams as ViewGroup.MarginLayoutParams
//        lp.padi = (DensityUtil.dpToPx(context, 7) + DensityUtil.getStatusBarHeight(context)).toInt()
//        mViewDataBinding.appbar.layoutParams = lp
//        mViewDataBinding.appbar.set
        initBanner()
        addObserver()
        networkRequest()
    }

    private fun addListenerToOpenCategory(list: List<MainCategoryBean>) {
        val categoryIdsList = getCategoryIdsList(list)
        mViewDataBinding.imgMarketOne.setOnClickListener {
            startCategoryFragment(categoryIdsList[0], list[0].name)
        }

        mViewDataBinding.imgMarketTwo.setOnClickListener {
            startCategoryFragment(categoryIdsList[1], list[1].name)
        }

        mViewDataBinding.imgMarketThree.setOnClickListener {
            startCategoryFragment(categoryIdsList[2], list[2].name)
        }

        mViewDataBinding.imgMarketFour.setOnClickListener {
            startCategoryFragment(categoryIdsList[3], list[3].name)
        }

        mViewDataBinding.imgMarketFive.setOnClickListener {
            startCategoryFragment(categoryIdsList[4], list[4].name)
        }
    }

    private fun getCategoryIdsList(list: List<MainCategoryBean>): ArrayList<ArrayList<Int>> {
        val tempList = ArrayList<ArrayList<Int>>()

        for (i in list) {
            val temp = ArrayList<Int>()
            for (j in i.categoryList) {
                temp.add(j.id)
            }
            tempList.add(temp)
        }

        return tempList
    }

    private fun startCategoryFragment(ids: ArrayList<Int>, name: String) {
        val intent = Intent(activity, CategoriesHostActivity::class.java)
        intent.putExtra(MARKET_FRAGMENT_IDS, ids)
        intent.putExtra(MARKET_FRAGMENT_NAME, name)
        startActivity(intent)
    }

    /**
     * 进行网络请求
     */
    private fun networkRequest() {
        mViewModel?.getMainCategories()
        mViewModel?.getLabelCategories()
    }


    /**
     * 当有新的数据加入时
     */
    fun notifyDataChanged(data: ClassificationBean.SecondListBean?) {
        if (data != null) {
            val pagerAdapter = mViewDataBinding.pager.adapter
            for (i in 0 until mTabSize) {
                if (pagerAdapter != null) {
                    val fragment = pagerAdapter.instantiateItem(mViewDataBinding.pager, i) as MarketItemFragment
                    if (fragment.hasLoaded) {
                        fragment.refresh(data)
                    }
                }
            }
        }
    }

    /**
     * 设置观察事件
     */
    private fun addObserver() {
        mClassifications = SpData.getCategory()
        mAllLabels = TokenManager.getLabels()
        //看sp中是否有数据，如果没有的话就请求
        if (mClassifications.isNullOrEmpty()) {
            mViewModel?.getClassification()
        } else {
            mHasClassification.value = true
        }
        if (mAllLabels.isEmpty()) {
            mViewModel?.getAllLabels()
        } else {
            mHasLabels.value = true
        }
        /**
         * 只有网络加载完毕时，才初始化viewPager和TabLayout
         */
        mHasLabels.observe(viewLifecycleOwner, Observer {
            if (it && mHasLabelCategories.value!!) {
                initViewPagerAndTabLayout()
            }
        })
        mHasLabelCategories.observe(viewLifecycleOwner, Observer {
            if (it && mHasLabels.value!!) {
                initViewPagerAndTabLayout()
            }
        })



        /**
         * 当网络请求成功时配置五个页面
         * 懒得写RecyclerView了就这样吧。。。
         * todo 等涵哥写完加上点击事件
         */
        mViewModel!!.mainCategories.observe(viewLifecycleOwner, Observer<List<MainCategoryBean>> {
            if (!it.isNullOrEmpty()) {
                addListenerToOpenCategory(it)
                val glide = Glide.with(this)
                var option: RequestOptions
                for (i in 0..4) {
                    when (i) {
                        0 -> {
                            mViewDataBinding!!.txtMarketOne.text = it[i].name
                            option = RequestOptions().placeholder(R.drawable.ic_market_book)
                                    .error(R.drawable.ic_market_book)
                            glide.load(it[i].icon)
                                    .apply(option)
                                    .into(mViewDataBinding!!.imgMarketOne)
                        }
                        1 -> {
                            mViewDataBinding!!.txtMarketTwo.text = it[i].name
                            option = RequestOptions().placeholder(R.drawable.ic_market_play)
                                    .error(R.drawable.ic_market_play)
                            glide.load(it[i].icon)
                                    .apply(option)
                                    .into(mViewDataBinding!!.imgMarketTwo)
                        }
                        2 -> {
                            mViewDataBinding!!.txtMarketThree.text = it[i].name
                            option = RequestOptions().placeholder(R.drawable.ic_market_duck)
                                    .error(R.drawable.ic_market_duck)
                            glide.load(it[i].icon)
                                    .apply(option)
                                    .into(mViewDataBinding!!.imgMarketThree)
                        }
                        3 -> {
                            mViewDataBinding!!.txtMarketFour.text = it[i].name
                            option = RequestOptions().placeholder(R.drawable.ic_market_skill)
                                    .error(R.drawable.ic_market_skill)
                            glide.load(it[i].icon)
                                    .apply(option)
                                    .into(mViewDataBinding!!.imgMarketFour)
                        }
                        4 -> {
                            mViewDataBinding!!.txtMarketFive.text = it[i].name
                            option = RequestOptions().placeholder(R.drawable.ic_market_other)
                                    .error(R.drawable.ic_market_other)
                            glide.load(it[i].icon)
                                    .apply(option)
                                    .into(mViewDataBinding!!.imgMarketFive)
                        }
                    }
                }
            }
        })
    }

    /**
     * 初始化ViewPager和TabLayout的使用
     */
    private fun initViewPagerAndTabLayout() {
        val list = ArrayList<Fragment>()
        val names = ArrayList<String>()
        val data = mViewModel.labelCategories.value
        if (data != null) {
            mTabSize = data.size
            data.forEach {
                val string = StringBuilder()
                //一级分类需要找出它下面所有的二级分类
                if (it.level == 1) {
                    for (firItem in mClassifications!!) {
                        //查找出是哪个一级分类
                        if (firItem.id == it.id) {
                            //将一级分类下的所有二级分类都组合成“1，2，3”形式的字符串
                            firItem.secondList.forEachIndexed { index, secondListBean ->
                                if (firItem.secondList.size == 1) {
                                    string.append(secondListBean.id)
                                } else {
                                    if (index != firItem.secondList.size - 1) {
                                        string.append(secondListBean.id)
                                        string.append(",")
                                    } else {
                                        string.append(secondListBean.id)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    string.append(it.id)
                }
                list.add(MarketItemFragment(string.toString()) { it1 ->
                    onScroll(it1)
                })
                names.add(it.name)
            }
            //预加载全部并且配合懒加载的fragment
            mViewDataBinding?.pager?.offscreenPageLimit = data.size
        }
        //建立TabLayout和ViewPager的连接
        mPagerAdapter = MyFragmentPagerAdapter(requireActivity().supportFragmentManager, list, names)
        mViewDataBinding?.pager?.adapter = mPagerAdapter
        mViewDataBinding?.tlayout?.setupWithViewPager(mViewDataBinding?.pager)
    }


    /**
     * 当recyclerView滑动时fab的状态
     */
    private fun onScroll(distance: Float) {
        if (distance <= 0) {
            //上划时显示fba
            mViewDataBinding!!.fab.animate().translationY(0f).alpha(1f).setInterpolator(DecelerateInterpolator(1.0f)).start()
            mViewDataBinding!!.fab.isClickable = true
        } else {
            //下滑时隐藏fba
            val lp = mViewDataBinding!!.fab.layoutParams as ViewGroup.MarginLayoutParams
            mViewDataBinding!!.fab.animate().translationY((lp.height + lp.bottomMargin).toFloat()).alpha(0f).setInterpolator(AccelerateDecelerateInterpolator()).start()
            mViewDataBinding!!.fab.isClickable = false
        }
    }

    override fun onLoadMoreFailure(message: String) {}

    /**
     * 初始化banner
     */
    private fun initBanner() {
        //监听网络
        mViewModel?.rotationCharts?.observe(this, Observer<List<RotationChartsBean>> { t ->
            if (t != null && t.isNotEmpty()) {
                mViewDataBinding?.banner?.adapter = MyBannerAdapter(t, mContext)
                //设置参数,indicator必须在adapter设置好后设置
                mViewDataBinding?.banner?.setIndicator(SlideIndicator(mContext))
                        ?.setIndicatorSelectedColor(mContext.resources?.getColor(android.R.color.darker_gray)!!)
                        ?.setIndicatorNormalWidth(BannerUtils.dp2px(8f).toInt())
                        ?.setIndicatorSelectedWidth(BannerUtils.dp2px(12f).toInt())
                        ?.setIndicatorNormalColor(mContext.resources?.getColor(android.R.color.darker_gray)!!)
                        ?.setIndicatorType(R.drawable.shape_slide_indicator)
                        ?.setPageTransformer(MultiplePagerScaleInTransformer(10, 0.5f))
                mViewDataBinding?.banner?.isAutoLoop(true)
                //开始自动换图
                mViewDataBinding?.banner?.start()
            } else {
                mViewDataBinding!!.banner.adapter = MyBannerAdapter(null, mContext)
            }
        })
        //进行网络请求,获取轮播图
        val userData = UserInfoIO.getLastUser(mContext)
        var schoolId = 1
        if (userData != null) {
            schoolId = userData.schoolId
        }
        mViewModel?.getRotationCharts(schoolId = schoolId.toString())
    }

    override fun onLoadMoreEmpty() {}

    override fun onNetDone(key: String, status: BaseNetworkStatus) {
        when(key) {
            GetClassificationsModel.TAG -> {
                mFirstClassifications = TokenManager.getFirstClassification()
                mHasClassification.value = true
                }
            GetAllLabelModel.TAG -> {
                mAllLabels = TokenManager.getLabels()
                mHasLabels.value = true
            }
            GetLabelCategoriesModel.TAG -> {
                mHasLabelCategories.value = true
            }
        }
    }
    inner class EventPresenter {
        fun onClick(v: View) {
            when (v.id) {
                R.id.llayout_search  ->                     //跳转到搜索模块
                    ARouter.getInstance().startActivity(activity, Constants.ACTIVITY_SEARCH)
                R.id.fab  -> {
                    //使当前page页面中的recyclerView跳转到顶部
                    val curPage = mViewDataBinding!!.pager.currentItem
                    val curFragment = mPagerAdapter.getItem(curPage) as MarketItemFragment
                    curFragment.toTop()
                }
                R.id.img_market_desire -> {
                    WebViewActivity.startAction(mContext, "https://www.baidu.com/")
                }
                R.id.llayout_classification -> {
                    startActivity(Intent(activity, CategoriesHostActivity::class.java))
                }
            }
        }
    }
}




