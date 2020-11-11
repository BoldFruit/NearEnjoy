package com.example.mvvm_simple.view.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_simple.R
import com.example.mvvm_simple.model.bean.ClassificationBean
import com.example.mvvm_simple.model.bean.SecondClassificationBean
import com.example.mvvm_simple.view.IdleInformationActivity
import com.example.mvvm_simple.view.SecondClassificationActivity

/**
 * @author DuLong
 * @since 2020/4/24
 * email 798382030@qq.com
 */
class SecondClassificationAdapter(val data: List<ClassificationBean.SecondListBean>, val context: Context) : RecyclerView.Adapter<SecondClassificationAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameTxt: TextView = itemView.findViewById(R.id.txt_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_classification, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.nameTxt.text = data.name
        //跳转到闲置信息的界面
        holder.itemView.setOnClickListener(View.OnClickListener {
            IdleInformationActivity.actionStart(context, data)
        })
    }

}