package com.example.found.paging.adapter.viewHolder

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.found.R
import com.example.found.model.bean.SearchUserResponse


/**
 * @author DuLong
 * @since 2020/3/27
 * email 798382030@qq.com
 */
class UserViewHolder(itemView: View, parent: ViewGroup)
    : RecyclerView.ViewHolder(itemView) {

    private val mImgAvatar: de.hdodenhof.circleimageview.CircleImageView = itemView.findViewById(R.id.img_avatar)
    private val mTxtName: TextView = itemView.findViewById(R.id.txt_name)
    private val mTxtIndroduction: TextView = itemView.findViewById(R.id.txt_personal_introduction)

    fun bindTo(data: SearchUserResponse?, position: Int) {
        val options = RequestOptions()
                .error(R.drawable.pic_placehodler)
                .placeholder(R.drawable.pic_placehodler)
        val glide = Glide.with(itemView)
        glide.load(data?.avatar)
                .apply(options)
                .into(mImgAvatar)
        mTxtName.setText(data?.name)
        mTxtIndroduction.setText(data?.signature)

    }

    companion object {
        fun create(parent: ViewGroup): UserViewHolder{
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
            return UserViewHolder(view, parent)
        }
    }
}