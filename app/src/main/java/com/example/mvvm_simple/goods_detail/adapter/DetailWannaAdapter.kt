package com.example.mvvm_simple.goods_detail.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mvvm_simple.R

/**
 * Time:2020/4/19 21:17
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class DetailWannaAdapter(var imageList: ArrayList<String>, var windowWidth: Int): RecyclerView.Adapter<DetailWannaAdapter.WannaImageViewHolder>() {

    private var firstLineImages = ArrayList<String>()
    private var isMore = false

    companion object {
        const val ONE_LINE_NUMBER = 8
    }

    init {
        //大于8，只截取前7个头像
        if (imageList.size > ONE_LINE_NUMBER) {
            isMore = true
            for (i in 0..6) {
                firstLineImages.add(imageList[i])
            }
            firstLineImages.add("")
        } else {
            firstLineImages.addAll(imageList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WannaImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(getLayout(viewType), parent, false)
        return WannaImageViewHolder(view, windowWidth = windowWidth)
    }

    private fun getLayout(type: Int): Int {
        return when(type) {
            7 -> {
                R.layout.detail_item_wanna_more
            }
            else -> {
                R.layout.detail_item_wanna_avatar
            }
        }
    }

    override fun getItemCount() = firstLineImages.size

    override fun onBindViewHolder(holder: WannaImageViewHolder, position: Int) {
        if (imageList.size > ONE_LINE_NUMBER) {
            holder.bindView(position, imageList[position], true, imageList.size)
        } else {
            holder.bindView(position, imageList[position], false, imageList.size)
        }

    }

    override fun getItemViewType(position: Int) = position

    class WannaImageViewHolder(view: View, var windowWidth: Int): RecyclerView.ViewHolder(view) {

        lateinit var imageView: ImageView
        private lateinit var moreNumberTxt: TextView

        fun bindView(position: Int, imgUrl: String, isMore: Boolean, totalCount: Int) {

            if (isMore && position == ONE_LINE_NUMBER - 1) {
                moreNumberTxt = itemView.findViewById(R.id.detail_item_wanna_more_txt_number)
                moreNumberTxt.text = totalCount.toString()
                val layoutParams = itemView.layoutParams
                layoutParams.width = windowWidth / ONE_LINE_NUMBER
                itemView.layoutParams = layoutParams
            } else {
                imageView = itemView.findViewById(R.id.detail_wanna_item_img_avatar)
                Glide.with(itemView.context)
                        .load(imgUrl)
                        .listener(object : RequestListener<Drawable> {
                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                val layoutParams = itemView.layoutParams
                                layoutParams.width = windowWidth / ONE_LINE_NUMBER
                                itemView.layoutParams = layoutParams
                                return false
                            }

                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                return false
                            }

                        })
                        .into(imageView)
            }

        }

    }


}