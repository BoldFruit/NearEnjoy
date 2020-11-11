package com.example.found.paging.adapter.viewHolder

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Index
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.arouter.ARouter
import com.example.arouter.Constants
import com.example.found.R
import com.example.found.model.bean.SearchGoodsResponse
import com.example.found.view.FoundActivity
import com.example.found.view.adapter.MyTagAdapter
import com.example.lib_common.util.SPUtils
import com.example.lib_common.widget.flowlayout.TagFlowLayout
import org.json.JSONArray

import kotlin.collections.HashMap

/**
 * @author DuLong
 * @since 2020/3/14
 * email 798382030@qq.com
 */

/**
 * 用于展示LinearLayout排列的recyclerView的商品数据的ViewHolder
 */
class LinearGoodsViewHolder(view: View, parent: ViewGroup)
    : RecyclerView.ViewHolder(view){

    private val imgGoods: ImageView = view.findViewById(R.id.img_goods)
    private val imgAvatar: ImageView = view.findViewById(R.id.img_avatar)
    private val txtGoodsName: TextView = view.findViewById(R.id.txt_goods_name)
    private val txtPrice: TextView = view.findViewById(R.id.txt_price)
    private val txtName: TextView = view.findViewById(R.id.txt_name)
    private val txtNumberLove: TextView = view.findViewById(R.id.txt_number_love)
    private val tlayoutTag: TagFlowLayout = view.findViewById(R.id.tlayout_tag)



    fun bindTo(context: Context, data: SearchGoodsResponse.SearchListBean?) {
        val options = RequestOptions()
                .error(R.drawable.pic_placehodler)
                .placeholder(R.drawable.pic_placehodler)
        val glide = Glide.with(itemView)
        //获取第一张图片
        glide.load(JSONArray(data?.images).getString(0))
                .apply(options)
                .into(imgGoods)
        glide.load(data?.userAvatar)
                .apply(options)
                .into(imgAvatar)
        txtGoodsName.setText(data?.name)
        txtPrice.text = String.format(context.resources.getText(R.string.found_txt_price_number).toString(), data?.price)
        encodeLabels(data?.labelIds)
        txtName.text = data?.userName
        txtNumberLove.text = String.format(context.resources.getText(R.string.found_txt_want_number).toString(), data?.wants)
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
        fun create(parent: ViewGroup): LinearGoodsViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_llayout_goods, parent, false)
            return LinearGoodsViewHolder(itemView, parent)
        }
    }
}