package com.example.mvvm_simple.goods_detail.view

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowId
import android.view.WindowManager
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.disklrucache.DiskLruCache
import com.bumptech.glide.util.LruCache
import com.example.arouter.ARouter
import com.example.arouter.Constants
import com.example.base.utils.ToastUtil
import com.example.base.view.activity.MvvmNetworkActivity
import com.example.fw_annotations.BindPath
import com.example.lib_common.dialog.bottom_dialog.SimpleAdapter
import com.example.lib_common.dialog.bottom_dialog.SimpleShareBottomDialog
import com.example.lib_common.imageview.gallery.model.GalleryPhotoModel
import com.example.mvvm_simple.R
import com.example.mvvm_simple.categories.mvvm.view.CategoriesFragment
import com.example.mvvm_simple.categories.mvvm.view.DetailCategoryFragment
import com.example.mvvm_simple.databinding.ActivityGoodsDetailBinding
import com.example.mvvm_simple.goods_detail.adapter.DetailImagesAdapter
import com.example.mvvm_simple.goods_detail.adapter.DetailWannaAdapter
import com.example.mvvm_simple.goods_detail.adapter.FlowTagAdapter
import com.example.mvvm_simple.goods_detail.viewmodel.GoodsDetailViewModel
import com.luck.picture.lib.PictureSelector
//import com.neuqer.module_share.MainActivity
import com.zhihu.matisse.engine.ImageEngine
import com.zhihu.matisse.engine.impl.GlideEngine
import kotlinx.android.synthetic.main.item_mine_onsale.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * Time:2020/4/18 16:45
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
@BindPath(value = "app/GoodsDetailActivity")
class GoodsDetailActivity: MvvmNetworkActivity<ActivityGoodsDetailBinding, GoodsDetailViewModel>() {
    companion object {
        fun actionStart(context: Context, goodsId: Int) {
            val intent = Intent(context, GoodsDetailActivity::class.java)
            intent.putExtra(DetailCategoryFragment.START_GOODS_DETAIL, goodsId)
            context.startActivity(intent)
        }
    }

    private var isOtherWannaShow: Boolean = false
    private var goodsId: Int = -1
    private var imagesList = ArrayList<String>()

    private val instance by lazy {
        this
    }

    private val dialog by lazy {
        SimpleShareBottomDialog(instance, null, SimpleAdapter.SimpleItemClickListener {
            ToastUtil.show(this, it)
            val bundle = Bundle()
            bundle.putString(Constants.A_KEY_DETAIL_TO_SHARE_IMAGE, imagesList[0])
            bundle.putString(Constants.A_KEY_DETAIL_SHARE, it)
            bundle.putString(Constants.A_KEY_DETAIL_TO_SHARE_URL, "https://han1254.github.io/HTML_TEST/")
            ARouter.getInstance().startActivity(this, Constants.ACTIVITY_SHARE, bundle)
        })
    }

    private  var tagAdapter: FlowTagAdapter? = null
    lateinit var recyclerViewAdapter: DetailImagesAdapter
    lateinit var otherWannaAdapter: DetailWannaAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_goods_detail
    }

    override fun getViewModel(): Class<out ViewModel> {
        return GoodsDetailViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return 0
    }

    override fun initParameters() {
    }

    override fun initDataAndView() {

        ARouter.getInstance().putActivity(Constants.ACTIVITY_DETAIL, GoodsDetailActivity::class.java)
//        ARouter.getInstance().putActivity(Constants.ACTIVITY_SHARE, com.neuqer.module_share.MainActivity::class.java)

        goodsId = intent.getIntExtra(DetailCategoryFragment.START_GOODS_DETAIL, -1)


        if (goodsId == -1) {
            mViewDataBinding.goodsDetailMainPart.visibility = View.GONE
            mViewDataBinding.goodsDetailFailedPage.visibility = View.VISIBLE
            ToastUtil.show(this, "加载失败")
            return
        }
        mViewDataBinding.goodsDetailLottieView.visibility = View.VISIBLE
        mViewDataBinding.goodsDetailRecycler.visibility = View.GONE
        mViewDataBinding.goodsDetailLottieView.playAnimation()

        if (goodsId != -1) {
            mViewModel.requestDetail(goodsId.toString())
        }

        mViewModel.responseBean.observe(this, Observer {
            Glide.with(instance)
                    .load(it.userAvatar)
                    .into( mViewDataBinding.goodsDetailAvatar)
            mViewDataBinding.goodsDetailUserName.text = it.userName
            mViewDataBinding.goodsDetailPrice.text = it.price.toString()
            mViewDataBinding.goodsDetailGoodsTitle.text = it.name
            mViewDataBinding.goodsDetailGoodsDescription.text = it.description
        })

        mViewModel.imagesList.observe(this, Observer {
            //存储imagesList
            imagesList = it
            mViewDataBinding.goodsDetailMainPart.visibility = View.VISIBLE
            mViewDataBinding.goodsDetailFailedPage.visibility = View.GONE
            mViewDataBinding.goodsDetailLottieView.visibility = View.GONE
            mViewDataBinding.goodsDetailRecycler.visibility = View.VISIBLE
            initRecyclerView(it)
        })
        mViewModel.tagsDataList.observe(this, Observer {
            initTags(it)
        })

        mViewModel.isLoading.observe(this, Observer {
               if (it == GoodsDetailViewModel.RequestType.FAILED) {
                    mViewDataBinding.goodsDetailMainPart.visibility = View.GONE
                    mViewDataBinding.goodsDetailFailedPage.visibility = View.VISIBLE
                }
            })

        setToolbarMoreListener()

        setWannaListener()

        setIWannaListener()

        setClickListenerToFailed()
    }

    private fun setClickListenerToFailed() {
        mViewDataBinding.goodsDetailFailedIcon.setOnClickListener {
            mViewModel.requestDetail(id = goodsId.toString())
        }
    }

    /**
     * 点击右上角三点
     */
    private fun setToolbarMoreListener() {
        mViewDataBinding.goodsDetailToolbarMore.setOnClickListener {
            dialog.show()
        }
    }

    /**
     * 点击我想要
     */
    private fun setIWannaListener() {
        mViewDataBinding.goodsDetailImgIWanna.setOnClickListener {
            try {
                ToastUtil.show(this, "跳转QQ......")
                //第二种方式：可以跳转到添加好友，如果qq号是好友了，直接聊天
                val url = "mqqwpa://im/chat?chat_type=wpa&uin="+mViewDataBinding.goodsDetailConnectionNumber.text.toString().trim()//uin是发送过去的qq号码
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (e: Exception) {
                e.printStackTrace()
                ToastUtil.show(this, "请检查是否安装QQ")
            }
        }
    }

    /**
     * 点击大家想要
     */
    private fun setWannaListener() {
        mViewDataBinding.goodsDetailImgOtherWanna.setOnClickListener {
           onTouchOtherWanna()
        }
    }

    /**
     * 点击大家想要
     */
    private fun onTouchOtherWanna() {
        if (isOtherWannaShow) {
            mViewDataBinding.layoutOtherWanna.visibility = View.GONE
            isOtherWannaShow = false
        } else {
            mViewDataBinding.layoutOtherWanna.visibility = View.VISIBLE
            isOtherWannaShow = true
            val images = ArrayList<String>()
            for (i in 0..9) {
                if(i % 2 == 0) {
                    images.add("https://i.ibb.co/p3mwB7v/timg.jpg")
                } else {
                    images.add("https://i.ibb.co/60JdWn8/timg1.jpg")
                }
            }
            val manager: WindowManager = this.windowManager
            val outMetrics = DisplayMetrics()
            manager.defaultDisplay.getMetrics(outMetrics)
            otherWannaAdapter = DetailWannaAdapter(images, outMetrics.widthPixels)
            val layoutManager = LinearLayoutManager(instance, LinearLayoutManager.HORIZONTAL, false)
            mViewDataBinding.goodsDetailOtherWannaRecycler.layoutManager = layoutManager
            mViewDataBinding.goodsDetailOtherWannaRecycler.adapter = otherWannaAdapter
        }
    }

    /**
     * 初始化标签
     */
    private fun initTags(list:List<String>) {
        tagAdapter = FlowTagAdapter(list, instance, FlowTagAdapter.YELLOW_TAG)
        mViewDataBinding.goodsDetailTagLayout.adapter = tagAdapter
    }

    /**
     * 初始化Recyclerview
     */
    private fun initRecyclerView(imagesList: ArrayList<String>) {
        recyclerViewAdapter = DetailImagesAdapter(imagesList)
        mViewDataBinding.goodsDetailRecycler.adapter = recyclerViewAdapter
        recyclerViewAdapter.setClickListener(object : DetailImagesAdapter.DetailImageItemClickListener {
            override fun onClick(position: Int, url: String, imageView: ImageView) {
                //显示图片预览
                val modelList = ArrayList<GalleryPhotoModel>()
                for (i in imagesList) {
                    modelList.add(GalleryPhotoModel(i))
                }
                mViewDataBinding.photoGalleryView.visibility = View.VISIBLE
                mViewDataBinding.photoGalleryView.showPhotoGallery(position, modelList, imageView)
            }

        })
    }

    override fun onDestroy() {
        tagAdapter?.unRegister()
        super.onDestroy()
    }
}