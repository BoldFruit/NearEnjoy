package com.example.module_chat.adapter;

import android.view.View;

import com.example.lib_neuqer_chat_ui.message.TxtMessage;
import com.example.module_chat.databinding.ItemReceiveTxtBinding;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

/**
 * Time:2020/3/25 15:33
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ChatReceiveTextViewHolder extends BaseChatViewHolder<ItemReceiveTxtBinding, TxtMessage> {

    public ObservableField<TxtMessage> observableField = new ObservableField<>();

    public ChatReceiveTextViewHolder(@NonNull View itemView) {
        super(itemView);
        getDataBinding().setReceiveTxtItem(this);
    }

    @Override
    public void bind(TxtMessage msg) {
        observableField.set(msg);
    }
}
