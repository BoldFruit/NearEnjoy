package com.example.lib_common.linkage_kt.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.lib_common.R
import com.example.lib_common.databinding.CommonLinkdataItemMainContentBinding
import com.example.lib_common.linkage_kt.data.SecondLinkData
import org.w3c.dom.Text

/**
 * Time:2020/4/14 9:20
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class ContentGridAdapter(var list: ArrayList<SecondLinkData>, var context: Context,  var itemClickListener: ContentAdapter.OnItemClickListener): BaseAdapter() {


    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var viewHolder: ViewHolder = ViewHolder()
        val view: View

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.common_linkdata_item_main_content, parent, false)
//            viewHolder.image = view.findViewById<ImageView>(R.id.link_data_main_item_img)
//            viewHolder.title = view.findViewById<TextView>(R.id.link_data_main_item_txt)
            var binding: CommonLinkdataItemMainContentBinding? = DataBindingUtil.bind(view)
            view.setOnClickListener {
                itemClickListener.onCtgItemClick(list[position], position, view)
            }
            view.tag = viewHolder
            view?.context?.let {
                Glide.with(it)
                        .load(list[position].image)
                        .into(binding!!.linkDataMainItemImg)
            }
            binding!!.linkDataMainItemTxt.text = list[position].name
            return view
        } else {
            viewHolder = convertView.tag as ViewHolder
            viewHolder.image = convertView.findViewById(R.id.link_data_main_item_img)
            viewHolder.title = convertView.findViewById(R.id.link_data_main_item_txt)
            convertView?.context?.let {
                Glide.with(it)
                        .load(list[position].image)
                        .into(viewHolder.image)
            }
            viewHolder.title.text = list[position].name
            return convertView
        }
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
       return list.size
    }

    inner class ViewHolder {
        lateinit var title: TextView
        lateinit var image: ImageView
    }
}