package com.example.lib_neuqer_chat_ui.controller;

import android.graphics.drawable.Drawable;

import com.example.lib_neuqer_chat_ui.emoji.EmojiBean;

import java.util.List;

/**
 * Time:2020/3/20 14:02
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public interface BasicConfig {

    /**
     * 是否是第一次，因为这涉及
     * @return
     */
    boolean isFirst();

    boolean isEmojiDataBase();

    int getPerPageEmojiCount();

    int getPerPageEmojiColumn();

    Drawable getSenderBubble();

    Drawable getReceiverBubble();

    List<EmojiBean> getEmojiList();

    int getDeleteRes();

    int getEmojiBtnRes();
}
