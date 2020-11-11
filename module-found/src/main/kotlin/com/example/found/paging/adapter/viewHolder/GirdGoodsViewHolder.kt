package com.example.found.paging.adapter.viewHolder

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.arouter.ARouter
import com.example.arouter.Constants
import com.example.found.R
import com.example.found.databinding.ItemGlayoutGoodsBinding
import com.example.found.databinding.ItemLlayoutGoodsBinding
import com.example.found.model.bean.SearchGoodsResponse
import com.example.found.view.FoundActivity
import com.example.found.view.adapter.MyTagAdapter
import com.example.lib_common.util.DensityUtil
import com.example.lib_common.util.SPUtils
import com.example.lib_common.widget.flowlayout.TagFlowLayout
import com.google.gson.JsonArray
import org.json.JSONArray
import java.net.URL
import kotlin.math.abs

/**
 * @author DuLong
 * @since 2020/3/14
 * email 798382030@qq.com
 */
class GirdGoodsViewHolder(view: View)
    : RecyclerView.ViewHolder(view) {

    private val imgGoods: ImageView = view.findViewById(R.id.img_goods)
    private val imgAvatar: ImageView = view.findViewById(R.id.img_avatar)
    private val txtGoodsName: TextView = view.findViewById(R.id.txt_goods_name)
    private val txtPrice: TextView = view.findViewById(R.id.txt_price)
    private val txtName: TextView = view.findViewById(R.id.txt_name)
    private val txtNumberLove: TextView = view.findViewById(R.id.txt_number_love)
    private val tlayoutTag : TagFlowLayout = view.findViewById(R.id.tlayout_tag)

    @SuppressLint("SetTextI18n")
    fun bindTo(context: Context, data: SearchGoodsResponse.SearchListBean?, position: Int) {
        val options = RequestOptions()
                .error(R.drawable.pic_placehodler)
                .placeholder(R.drawable.pic_placehodler)!!
        val glide = Glide.with(itemView)
        glide.load(data?.userAvatar)
                .apply(options)
                .into(imgAvatar)
        txtGoodsName.text = data?.name
        txtPrice.text = "￥ ${data?.price}"
        encodeLabels(data?.labelIds)
        txtName.text = data?.userName
        txtNumberLove.text = "${data?.wants} 想要"
        itemView.setOnClickListener {
            val bundle: Bundle = Bundle()
            data?.let {
                bundle.putInt("start_goods_detail", data.id)
                ARouter.getInstance().startActivity(context as FoundActivity, Constants.ACTIVITY_GOODS_DETAIL, bundle)
            }
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
            trueLabels.put(entry.key.toInt(), entry.value)
        }
        var labelName = ArrayList<String>()
        val jsonArray = JSONArray(labelIds)
        //获取标签名字
        for (i in 0 until  jsonArray.length()) {
            val index : Int = jsonArray.getString(i).toInt()
            labelName.add(trueLabels.get(index).toString())
        }
        tlayoutTag.adapter = MyTagAdapter(labelName, itemView.context, MyTagAdapter.SMALL_YELLOW_TAG)
    }


    companion object {

        fun create(data: SearchGoodsResponse.SearchListBean?, parent: ViewGroup): GirdGoodsViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_glayout_goods, parent, false)
            val img = itemView.findViewById<ImageView>(R.id.img_goods)
            val glide = Glide.with(itemView)
            //获取图片,并根据长宽设置合理的图片大小来显示。
            glide.asBitmap().load(JSONArray(data?.images).getString(0))
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            if (resource != null) {
                                adjustImageSize(resource.width, resource.height, img, itemView, resource);
                            } else {
                                img.setImageDrawable(itemView.context.getDrawable(R.drawable.pic_placehodler))
                            }
                        }

                    })
            return GirdGoodsViewHolder(itemView)
        }

        /**
         * 根据返回的图片大小，调整 ImageView的高度
         */
        private fun adjustImageSize(width: Int, height: Int, img: ImageView, itemView: View, resource: Bitmap){
            val ratio: Double = width.toDouble() / height.toDouble()
            val arg1: Double = abs(ratio -1)
            val arg2: Double = abs(ratio - 4.0 / 3.0)
            val arg3: Double = abs(ratio - 3.0 / 4.0)
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