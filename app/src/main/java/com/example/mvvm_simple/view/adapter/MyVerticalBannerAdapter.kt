package com.example.mvvm_simple.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lib_common.util.SPUtils
import com.example.lib_common.widget.banner.adapter.BannerAdapter
import com.example.mvvm_simple.R
import com.example.mvvm_simple.databinding.MainItemMySalingBinding
import com.example.mvvm_simple.goods_detail.view.GoodsDetailActivity
import com.example.mvvm_simple.model.bean.UserGoodsBean
import org.json.JSONArray

/**
 * @author DuLong
 * @since 2020/4/14
 * email 798382030@qq.com
 */
class MyVerticalBannerAdapter(var data: MutableList<UserGoodsBean.SearchListBean>): BannerAdapter<UserGoodsBean.SearchListBean, MyVerticalBannerAdapter.MyViewHolder>(data) {
    private lateinit var mDataBinding: MainItemMySalingBinding
    private lateinit var mContext: Context

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        mContext = parent!!.context
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.main_item_my_saling, parent, false)
        return MyViewHolder(mDataBinding.root)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindView(holder: MyViewHolder?, data: UserGoodsBean.SearchListBean?, position: Int, size: Int) {
        val option = RequestOptions().error(R.drawable.ic_mine_goods_placeholder)
                .placeholder(R.drawable.ic_placehodler)
        Glide.with(mContext)
                .load(JSONArray(data?.images).getString(0))
                .apply(option)
                .into(mDataBinding.imgMineGoods)
        mDataBinding.txtMinePrice.text = "￥ ${data?.price.toString()}"
        mDataBinding.txtMineWants.text = "${data?.wants} 想要"
        mDataBinding.txtMineGoodsName.text = data?.name
        encodeLabels(data?.labelIds)
        holder?.let{
            holder.itemView.setOnClickListener {
                data?.let {
                    GoodsDetailActivity.actionStart(mContext, data.id)
                }
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
            trueLabels[entry.key.toInt()] = entry.value
        }
        var labelName = ArrayList<String>()
        val jsonArray = JSONArray(labelIds)
        //获取标签名字
        for (i in 0 until  jsonArray.length()) {
            val index : Int = jsonArray.getString(i).toInt()
            labelName.add(trueLabels.get(index).toString())
        }
        mDataBinding.tagLayout.adapter = MyTagAdapter(labelName, mDataBinding.root.context, MyTagAdapter.SMALL_YELLOW_TAG)
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }
}