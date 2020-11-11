package com.example.module_chat.adapter;

import android.view.View;

import com.example.lib_neuqer_chat_ui.message.ImgMessage;
import com.example.module_chat.databinding.ItemSendImgBinding;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

/**
 * Time:2020/3/28 21:39
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ChatSendImgViewHolder extends BaseChatViewHolder<ItemSendImgBinding, ImgMessage> {

    public ObservableField<ImgMessage> imgField = new ObservableField<>();
    public ChatSendImgViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void bind(ImgMessage msg) {
        holderMsgField.set(msg);
        imgField.set(msg);
        avatarField.set("https://i.ibb.co/bbDf9cN/download.jpg");
        getDataBinding().setImgItemHolder(this);
    }

}
