package com.example.mvvm_simple.view.adapter.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lib_common.util.SPUtils
import com.example.lib_common.widget.flowlayout.TagFlowLayout
import com.example.mvvm_simple.R
import com.example.mvvm_simple.goods_detail.view.GoodsDetailActivity
import com.example.mvvm_simple.model.bean.UserGoodsBean
import com.example.mvvm_simple.view.adapter.MyTagAdapter
import org.json.JSONArray

/**
 * @author DuLong
 * @since 2020/5/1
 * email 798382030@qq.com
 */
class OnSaleGoodsViewHolder(view: View, parent: ViewGroup, private val context: Context): RecyclerView.ViewHolder(view) {

    private val imgGoods: ImageView = view.findViewById(R.id.img_goods)
    private val txtGoodsName: TextView = view.findViewById(R.id.txt_goods_name)
    private val txtPrice: TextView = view.findViewById(R.id.txt_price)
    private val txtNumberLove: TextView = view.findViewById(R.id.txt_number_love)
    private val tlayoutTag: TagFlowLayout = view.findViewById(R.id.tlayout_tag)
    private val imgEdit: ImageView = view.findViewById(R.id.img_edit)



    fun bindTo(data: UserGoodsBean.SearchListBean?) {
        val options = RequestOptions()
                .error(R.drawable.ic_placehodler)
                .placeholder(R.drawable.ic_placehodler)
        val glide = Glide.with(itemView)
        //获取第一张图片
        glide.load(JSONArray(data?.images).getString(0))
                .apply(options)
                .into(imgGoods)
        txtGoodsName.setText(data?.name)
        txtPrice.text = "￥ ${data?.price.toString()}"
        encodeLabels(data?.labelIds)
        txtNumberLove.text = "${data?.wants} 想要"
        imgEdit.setOnClickListener {
            //todo
        }
        itemView.setOnClickListener {
            data?.let {
                GoodsDetailActivity.actionStart(context, data.id)
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
        fun create(parent: ViewGroup, context: Context): OnSaleGoodsViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_mina_user_goods, parent, false)
            return OnSaleGoodsViewHolder(itemView, parent, context)
        }
    }
}