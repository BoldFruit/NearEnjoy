package com.example.module_chat.adapter;

import android.view.View;

import com.example.lib_neuqer_chat.config.DefaultConfig;
import com.example.lib_neuqer_chat_ui.message.TxtMessage;
import com.example.module_chat.databinding.ItemSendTxtBinding;

import java.util.Observer;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

/**
 * Time:2020/3/25 14:11
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ChatSendTextViewHolder extends BaseChatViewHolder<ItemSendTxtBinding, TxtMessage> {

    public ObservableField<TxtMessage> observableField = new ObservableField<>();
    public ObservableField<String> url = new ObservableField<>("https://i.ibb.co/bbDf9cN/download.jpg");

    public ChatSendTextViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void bind(TxtMessage msg) {
        observableField.set(msg);
        getDataBinding().setSendTxtItem(this);
        getDataBinding().executePendingBindings();
    }
}
