package com.example.lib_neuqer_chat.message;

import com.google.gson.annotations.SerializedName;

/**
 * Time:2020/3/15 16:07
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ChatMessagePrivatePlus<T> extends ChatMessagePrivate<T> {

    @SerializedName("goodsId")
    private long extId;
    public ChatMessagePrivatePlus(long sessionId, long fromId, long extId, long toId, String type, T content) {
        super(sessionId, fromId, toId, type, content);
        this.extId = extId;
    }

    @Override
    public Byte getMsgCommand() {
        return Packet.SINGLE_COMMAND;
    }

}
