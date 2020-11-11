package com.example.module_chat.view;

import android.graphics.drawable.Drawable;

import com.example.lib_neuqer_chat_ui.controller.BasicConfig;
import com.example.lib_neuqer_chat_ui.emoji.EmojiBean;

import java.util.List;

/**
 * Time:2020/3/25 18:54
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: 配置聊天界面所需要的部分
 */
public class ChatUIPacket implements BasicConfig {
    @Override
    public boolean isFirst() {
        return false;
    }

    @Override
    public boolean isEmojiDataBase() {
        return true;
    }

    @Override
    public int getPerPageEmojiCount() {
        return 0;
    }

    @Override
    public int getPerPageEmojiColumn() {
        return 0;
    }

    @Override
    public Drawable getSenderBubble() {
        return null;
    }

    @Override
    public Drawable getReceiverBubble() {
        return null;
    }

    @Override
    public List<EmojiBean> getEmojiList() {
        return null;
    }

    @Override
    public int getDeleteRes() {
        return 0;
    }

    @Override
    public int getEmojiBtnRes() {
        return 0;
    }
}
