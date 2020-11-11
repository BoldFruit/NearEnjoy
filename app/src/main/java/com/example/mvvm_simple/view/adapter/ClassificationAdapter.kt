package com.example.mvvm_simple.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_simple.R
import com.example.mvvm_simple.model.bean.ClassificationBean
import com.example.mvvm_simple.view.FirstClassificationActivity
import com.example.mvvm_simple.view.SecondClassificationActivity

/**
 * @author DuLong
 * @since 2020/4/24
 * email 798382030@qq.com
 */
/**
 * 用于展示所有一级分类的adapter
 */
class ClassificationAdapter(val context: Context, val data: List<ClassificationBean>) : RecyclerView.Adapter<ClassificationAdapter.MyViewHolder>(){
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameTxt: TextView = itemView.findViewById(R.id.txt_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_classification, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data[position]
        holder.nameTxt.text = data.name
        //跳转到二级选择页面
        holder.itemView.setOnClickListener(View.OnClickListener {
            SecondClassificationActivity.actionStart(context, data.secondList as ArrayList)
        })
    }
}