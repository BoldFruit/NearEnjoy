package com.example.lib_neuqer_chat.message;

/**
 * Time:2020/3/15 16:05
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ChatMessageHeartBeat extends Packet {

    private long userId;

    public ChatMessageHeartBeat(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public Byte getMsgCommand() {
        return Packet.HEART_BEAT_COMMAND;
    }
}
