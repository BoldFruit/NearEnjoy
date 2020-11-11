package com.example.module_chat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.lib_neuqer_chat_ui.message.BaseMessage;
import com.example.module_chat.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.databinding.ViewDataBinding;

/**
 * Time:2020/3/24 22:58
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class BaseTestChatAdapter extends BaseQuickAdapter<BaseMessage, BaseViewHolder> {

    private static final int TYPE_SEND_TXT = 1;
    private static final int TYPE_RECEIVE_TXT = 2;
    private static final int TYPE_SEND_IMG = 3;
    private static final int TYPE_RECEIVE_IMG = 4;

    private static final int LAYOUT_SEND_TXT = R.layout.item_send_txt;
    private static final int LAYOUT_SEND_IMG = R.layout.item_send_img;
    private static final int LAYOUT_RECEIVE_TXT = R.layout.item_receive_txt;
    private static final int LAYOUT_RECEIVE_IMG = R.layout.item_receive_img;

    public BaseTestChatAdapter(List<BaseMessage> chatList) {
        super(0, chatList);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, BaseMessage baseMessage) {

    }
}
