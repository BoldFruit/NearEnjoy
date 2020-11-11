package com.example.mvvm_simple.view.adapter

import android.content.Context
import android.media.Image
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Option
import com.bumptech.glide.request.RequestOptions
import com.example.lib_common.widget.banner.adapter.BannerAdapter
import com.example.lib_common.widget.imageview.CircleImageView
import com.example.mvvm_simple.R
import com.example.mvvm_simple.model.bean.RotationChartsBean
import com.example.mvvm_simple.view.WebViewActivity

/**
 * @author DuLong
 * @since 2020/4/6
 * email 798382030@qq.com
 */

/**
 * 用于创建banner的Adapter
 */
class MyBannerAdapter(private val datas: List<RotationChartsBean>?, private val context: Context): BannerAdapter<RotationChartsBean, MyBannerAdapter.ViewHolder>(datas) {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val imgView = CircleImageView(context)
        imgView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        imgView.scaleType = ImageView.ScaleType.CENTER_CROP
        return ViewHolder(imgView)
    }

    override fun onBindView(holder: ViewHolder?, data: RotationChartsBean?, position: Int, size: Int) {
        val options = RequestOptions()
                .error(R.drawable.ic_market_banner)
                .placeholder(R.drawable.ic_market_banner)
        val itemView = holder?.itemView
        Glide.with(itemView!!)
                .load(data?.image)
                .apply(options)
                .into(itemView as ImageView)
        //设置点击进入WebActicity
        if (!data?.link.isNullOrEmpty()) {
            itemView.setOnClickListener(View.OnClickListener { WebViewActivity.startAction(context, data!!.link) })
        }
    }
}