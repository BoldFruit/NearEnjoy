package com.example.module_chat.adapter;

import android.os.Message;
import android.view.View;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.lib_neuqer_chat.config.DefaultConfig;
import com.example.lib_neuqer_chat.message.BaseChatMessage;
import com.example.lib_neuqer_chat_ui.message.BaseMessage;
import com.example.lib_neuqer_chat_ui.message.TxtMessage;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2020/3/24 23:05
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public abstract class BaseChatViewHolder<D extends ViewDataBinding, T extends BaseMessage> extends BaseViewHolder {

    private final D dataBinding;
//    //sending
//    public ObservableField<Integer> status = new ObservableField<>(BaseMessage.SEND_SENDING_TYPE);

    public ObservableField<T> holderMsgField = new ObservableField<>();
    public ObservableField<String> avatarField = new ObservableField<>();

    public BaseChatViewHolder(@NonNull View itemView) {
        super(itemView);
        this.dataBinding = DataBindingUtil.bind(itemView);
    }

    public D getDataBinding() {
        return dataBinding;
    }

    public abstract void bind(T msg);

    public void bindDetail(T msg, String avatar) {
        holderMsgField.set(msg);
        bind(msg);
        avatarField.set(avatar);
    }

//
//    public void setStatus(DefaultConfig.SendType type) {
//        if (type == DefaultConfig.SendType.SEND_SUCCESS) {
//            status.set(BaseMessage.SEND_SUCCESS_TYPE);
//        } else {
//            status.set(BaseMessage.SEND_FAILED_TYPE);
//        }
//    }

}
