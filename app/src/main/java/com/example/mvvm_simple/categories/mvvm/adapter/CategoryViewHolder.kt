package com.example.mvvm_simple.categories.mvvm.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.lib_common.util.DensityUtil
import com.example.lib_common.util.SPUtils
import com.example.lib_common.widget.flowlayout.TagFlowLayout
import com.example.mvvm_simple.R
import com.example.mvvm_simple.model.bean.FirstClassificationBean
import com.example.mvvm_simple.view.adapter.MyTagAdapter
import org.json.JSONArray
import kotlin.math.abs

/**
 * Time:2020/4/13 21:14
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val imgGoods: ImageView = itemView.findViewById(R.id.img_goods)
    private val imgAvatar: ImageView = itemView.findViewById(R.id.img_avatar)
    private val txtGoodsName: TextView = itemView.findViewById(R.id.txt_goods_name)
    private val txtPrice: TextView = itemView.findViewById(R.id.txt_price)
    private val txtName: TextView = itemView.findViewById(R.id.txt_name)
    private val txtNumberLove: TextView = itemView.findViewById(R.id.txt_number_love)
    private val tlayoutTag : TagFlowLayout = itemView.findViewById(R.id.tlayout_tag)

    @SuppressLint("SetTextI18n")
    fun bindTo(data: FirstClassificationBean.SearchListBean?, position: Int) {
        val options = RequestOptions()
                .error(R.drawable.ic_placehodler)
                .placeholder(R.drawable.ic_placehodler)
        val glide = Glide.with(itemView)
        glide.load(data?.userAvatar)
                .apply(options)
                .into(imgAvatar)
        txtGoodsName.text = data?.name
        txtPrice.text = "￥ ${data?.price}"
        encodeLabels(data?.labelIds)
        txtName.text = data?.userName
        txtNumberLove.text = "${data?.wants} 想要"
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
        tlayoutTag.adapter = MyTagAdapter(labelName, itemView.context, MyTagAdapter.SMALL_YELLOW_TAG)
    }


    companion object {

        fun create(data: FirstClassificationBean.SearchListBean?, parent: ViewGroup, listener: CategoryWithFooterAdapter.OnGoodsItemClick): CategoryViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.main_item_glayout_goods, parent, false)
            itemView.setOnClickListener {
                listener.onClick(data!!.id)
            }
            val img = itemView.findViewById<ImageView>(R.id.img_goods)
            val glide = Glide.with(itemView)
            //获取图片,并根据长宽设置合理的图片大小来显示。
            glide.asBitmap().load(JSONArray(data?.images).getString(0))
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            adjustImageSize(resource.width, resource.height, img, itemView, resource)
                        }
                    })
            return CategoryViewHolder(itemView)
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