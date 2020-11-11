package com.example.found.paging.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.net.Uri
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.found.R
import com.example.found.model.bean.SearchGoodsResponse
import com.example.found.paging.NetworkState
import com.example.found.paging.adapter.viewHolder.BottomViewHolder
import com.example.found.paging.adapter.viewHolder.GirdGoodsViewHolder
import com.example.found.paging.adapter.viewHolder.LinearGoodsViewHolder
import org.json.JSONArray
import retrofit2.http.Url
import java.net.URL
import kotlin.math.abs

/**
 * @author DuLong
 * @since 2020/3/14
 * email 798382030@qq.com
 */
class GoodsDetailAdapter(private val context: Context, isLinearLayout: Boolean, private val retryCallback: () -> Unit): PagedListAdapter<SearchGoodsResponse.SearchListBean, RecyclerView.ViewHolder>(GOODS_COMPARATOR){

    private var mLayout = isLinearLayout
    private var networkState: NetworkState? = null
    /**
     * 转换布局排列方式
     */
    fun switchLayout(layoutType : Boolean) {
        mLayout = layoutType
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_bottom -> BottomViewHolder.create(parent, retryCallback)
            R.layout.item_llayout_goods -> LinearGoodsViewHolder.create(parent)
            else -> GirdGoodsViewHolder.create(getItem(viewType), parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_bottom -> (holder as BottomViewHolder).bindTo(networkState)
            R.layout.item_llayout_goods -> (holder as LinearGoodsViewHolder).bindTo(context, getItem(position))
            else -> (holder as GirdGoodsViewHolder).bindTo(context, getItem(position), position)
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
        //图片比例为1：1
        private val TYPE_ONE = 1;
        //图片比例为3：4
        private val TYPE_TWO = 2;
        //图片比例为4：3
        private val TYPE_THREE = 3;
        val GOODS_COMPARATOR = object : DiffUtil.ItemCallback<SearchGoodsResponse.SearchListBean>() {
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: SearchGoodsResponse.SearchListBean, newItem: SearchGoodsResponse.SearchListBean): Boolean =
                    oldItem == newItem


            override fun areItemsTheSame(oldItem: SearchGoodsResponse.SearchListBean, newItem: SearchGoodsResponse.SearchListBean): Boolean =
                    oldItem.id == newItem.id

            //todo 在特定情况下放回payload,使对应的item实现局部刷新
            override fun getChangePayload(oldItem: SearchGoodsResponse.SearchListBean, newItem: SearchGoodsResponse.SearchListBean): Any? {
            return null}

        }
    }


    override fun getItemViewType(position: Int): Int {

        return when {
            position == itemCount - 1 -> {
                R.layout.item_bottom
            }
            mLayout -> {
                R.layout.item_llayout_goods
            }
            else -> {
                position
            }
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
        return super.getItemCount() + 1
    }

    /**
     * 外界设置网络状态的接口
     */
    fun setNetWorkState(newNetworkState: NetworkState?) {
            if (networkState != newNetworkState) {
                networkState = newNetworkState
                notifyItemChanged(itemCount - 1)
            }
    }


}