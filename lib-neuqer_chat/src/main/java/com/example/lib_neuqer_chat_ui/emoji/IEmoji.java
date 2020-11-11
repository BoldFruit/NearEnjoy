package com.example.lib_neuqer_chat_ui.emoji;

/**
 * Time:2020/3/20 13:44
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public interface IEmoji {

    String getUnicodeString();

    int getUnicodeInt();

    int getId();

    EmojiType getType();

    public enum EmojiType {

        EMOJI,

        DELETE_KEY

    }
}
