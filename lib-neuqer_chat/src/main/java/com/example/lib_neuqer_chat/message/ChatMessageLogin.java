package com.example.lib_neuqer_chat.message;

/**
 * Time:2020/3/14 17:01
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ChatMessageLogin extends Packet {

    private long userId;

    public ChatMessageLogin(long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Byte getMsgCommand() {
        return Packet.LOGIN_COMMAND;
    }
}
