package com.example.mvvm_simple.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_OUTSIDE
import android.view.View

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.base.utils.ToastUtil
import com.example.lib_common.util.DensityUtil
import com.example.lib_common.util.SPUtils
import com.example.mvvm_simple.R
import com.example.mvvm_simple.categories.mvvm.view.DetailCategoryFragment
import com.example.mvvm_simple.databinding.ItemMarketGlayoutGoodsBinding
import com.example.mvvm_simple.goods_detail.view.GoodsDetailActivity
import com.example.mvvm_simple.model.bean.FirstClassificationBean
import com.example.mvvm_simple.view.adapter.viewholder.BottomViewHolder
import com.example.mvvm_simple.view.paging.NetworkState
import com.luck.picture.lib.tools.ToastUtils
import org.json.JSONArray
import java.lang.Math.abs


/**
 * @author DuLong
 * @since 2020/3/14
 * email 798382030@qq.com
 */

/**
 * 用于首页商品显示的adapter
 */
class FirstClassificationAdapter(private val context: Context, private val retryCallback: () -> Unit): PagedListAdapter<FirstClassificationBean.SearchListBean, RecyclerView.ViewHolder>(GOODS_COMPARATOR){

    private var networkState: NetworkState? = null
    private var initNetworkState: NetworkState? = null
    private var isFirstLoaded = true
    //这里用来记录上一个长按弹出举报界面的item
    private var mLastItemPosition = -1
    private var mViewMap = HashMap<Int, View>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.main_item_bottom -> BottomViewHolder.create(parent, retryCallback)
            else -> {
                val mViewDataBinding = DataBindingUtil.inflate<ItemMarketGlayoutGoodsBinding>(LayoutInflater.from(parent.context), R.layout.item_market_glayout_goods, parent, false)

                MarketGirdGoodsViewHolder(getItem(viewType), viewType, mViewDataBinding) }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.main_item_bottom -> (holder as BottomViewHolder).bindTo(networkState, initNetworkState)
            else -> (holder as MarketGirdGoodsViewHolder).bindTo(getItem(position), position)
        }
    }

    //todo:用于一些item的局部刷刷新
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
        } else {
            onBindViewHolder(holder, position)
        }
    }

    companion object {
        private val PAYLOAD = Any()

        val GOODS_COMPARATOR = object : DiffUtil.ItemCallback<FirstClassificationBean.SearchListBean>() {
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: FirstClassificationBean.SearchListBean, newItem: FirstClassificationBean.SearchListBean): Boolean =

                    oldItem == newItem


            override fun areItemsTheSame(oldItem: FirstClassificationBean.SearchListBean, newItem: FirstClassificationBean.SearchListBean): Boolean =
                    oldItem.id == newItem.id

            //todo 在特定情况下放回payload,使对应的item实现局部刷新
            override fun getChangePayload(oldItem: FirstClassificationBean.SearchListBean, newItem: FirstClassificationBean.SearchListBean): Any? {
            return null}

        }
    }


    /**
     * 这里再onCreateViewHolder中需要position,所以直接把position当作传过去
     */
    override fun getItemViewType(position: Int): Int {
        return if (hasExtra() && position == itemCount - 1) {
            R.layout.main_item_bottom
        } else {
            position
        }
    }


    /**
     * 使得在StaggeredGirdLayoutManager时，最后一行独占一行
     */
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        if (holder.layoutPosition == itemCount - 1) {
            val lp = holder.itemView.layoutParams;
            if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
                lp.isFullSpan = true
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtra()) 1 else 0
    }

    private fun hasExtra(): Boolean {
        return (initNetworkState == NetworkState.LOADED)
    }

    /**
     * 外界设置网络状态的接口
     */
    fun setNetWorkState(newNetworkState: NetworkState?) {
            if (networkState != null && networkState?.status != newNetworkState?.status) {
                networkState = newNetworkState
                notifyItemChanged(itemCount - 1)
            }

    }

    fun setInitNetWorkState(newNetworkState: NetworkState?) {
        if (newNetworkState != initNetworkState) {
            initNetworkState = newNetworkState
            if (initNetworkState == NetworkState.LOADED) {
                notifyItemChanged(itemCount - 1)
            }
        }
    }

    private inner class MarketGirdGoodsViewHolder(data: FirstClassificationBean.SearchListBean?, position: Int, private val mDataBinding: ItemMarketGlayoutGoodsBinding)
        : RecyclerView.ViewHolder(mDataBinding.root) {

        init {
            val img = mDataBinding.imgGoods
            val glide = Glide.with(mDataBinding.root)
            //获取图片,并根据长宽设置合理的图片大小来显示。
            glide.asBitmap().load(JSONArray(data?.images).getString(0))
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            adjustImageSize(resource.width, resource.height, img, mDataBinding.root, resource)
                        }
                    })
            //记录下每一个item
            //设置举报页面弹出的监听界面
            mDataBinding.root.setOnLongClickListener(View.OnLongClickListener {
                mDataBinding.relayoutReport.visibility = View.VISIBLE
                return@OnLongClickListener true
            })
            //点击item的其它地方，使举报页面消失
            mDataBinding.relayoutReport.setOnClickListener(View.OnClickListener {
                mDataBinding.relayoutReport.visibility = View.GONE
                ToastUtil.show(context, "举报成功我们将尽快的处理。")
            })
            mDataBinding.llayoutMarketShield.setOnClickListener(View.OnClickListener {
                mDataBinding.relayoutReport.visibility = View.GONE

            })
            mDataBinding.llayoutMarketUninterested.setOnClickListener(View.OnClickListener {
                mDataBinding.relayoutReport.visibility = View.GONE
                ToastUtil.show(context, "举报成功我们将尽快的处理。")
            })
            mDataBinding.llayoutMarketUnlike.setOnClickListener (View.OnClickListener {
                mDataBinding.relayoutReport.visibility = View.GONE
                ToastUtil.show(context, "举报成功我们将尽快的处理。")
            })
            mDataBinding.root.setOnClickListener {
                if (data != null) {
                    GoodsDetailActivity.actionStart(context, data.id)
//                    val intent = Intent(context, GoodsDetailActivity::class.java)
//                    intent.putExtra(DetailCategoryFragment.START_GOODS_DETAIL, data.id)
//                    context.startActivity(intent)
                }
            }
            //点击外面的时候隐藏举报页面
            mDataBinding.root.setOnTouchListener { v, event ->
                if (event.action == ACTION_OUTSIDE) {
                    mDataBinding.relayoutReport.visibility = View.GONE
                    return@setOnTouchListener true
                }
                return@setOnTouchListener false
            }

        }


        @SuppressLint("SetTextI18n")
        fun bindTo(data: FirstClassificationBean.SearchListBean?, position: Int) {
            val options = RequestOptions()
                    .error(R.drawable.ic_placehodler)
                    .placeholder(R.drawable.ic_placehodler)
            val glide = Glide.with(itemView)

            mDataBinding.txtGoodsName.text = data?.name
            mDataBinding.txtPrice.text = "￥ ${data?.price}"
            encodeLabels(data?.labelIds)
            //再每次onBindView时，隐藏举报界面
            if (mDataBinding.relayoutReport.visibility == View.VISIBLE) {
                mDataBinding.relayoutReport.visibility == View.GONE
            }
        }


        /**
         * 解析网络请求返回的标签，并且设置好TagLayout中的数据
         */
        private fun encodeLabels(labelIds: String?) {
            //这里有一个坑，虽然存的是Int,String型，但取出来的是个String类型。所以要重新遍历一下。
            val labels : HashMap<String, String> = SPUtils.get("labels", java.util.HashMap())
            val trueLabels = HashMap<Int, String>()
            for (entry in labels.entries) {
                trueLabels[entry.key.toInt()] = entry.value
            }
            var labelName = ArrayList<String>()
            val jsonArray = JSONArray(labelIds)
            //获取标签名字
            for (i in 0 until  jsonArray.length()) {
                val index : Int = jsonArray.getString(i).toInt()
                labelName.add(trueLabels[index].toString())
            }
            mDataBinding.tlayoutTag.adapter = MyTagAdapter(labelName, itemView.context, MyTagAdapter.YELLOW_TAG)
        }

            /**
             * 根据返回的图片大小，调整 ImageView的高度
             */
            private fun adjustImageSize(width: Int, height: Int, img: ImageView, itemView: View, resource: Bitmap){
                val ratio: Double = width.toDouble() / height.toDouble()
                val arg1: Double = kotlin.math.abs(ratio - 1)
                val arg2: Double = kotlin.math.abs(ratio - 4.0 / 3.0)
                val arg3: Double = kotlin.math.abs(ratio - 3.0 / 4.0)
                val lp = img.layoutParams
                val screenWidth = DensityUtil.getScreenWidth(itemView.context)
                val itemWidth = screenWidth / 2 - DensityUtil.dpToPx(itemView.context, 16)
                when {
                    arg1 < Math.min(arg2, arg3) -> {
                        lp.height = itemWidth
                    }
                    arg2 < Math.min(arg1, arg3) -> {
                        lp.height = (itemWidth * 3.0/ 4.0).toInt()
                    }
                    else -> {
                        lp.height = (itemWidth * 4.0/ 3.0).toInt()
                    }
                }
                img.layoutParams = lp
                img.setImageBitmap(resource)
                itemView.requestLayout()
            }
    }

}