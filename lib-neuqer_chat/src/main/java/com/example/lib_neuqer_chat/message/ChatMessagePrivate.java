package com.example.lib_neuqer_chat.message;

/**
 * Time:2020/3/14 17:05
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: Private chat from group chat
 */
public class ChatMessagePrivate<T> extends BaseChatMessage<T> {

    public ChatMessagePrivate(long sessionId, long fromId, long toId, String type, T content) {
        super(sessionId, fromId, toId, type, content);
    }

    @Override
    public Byte getMsgCommand() {
        return Packet.PRIVATE_COMMAND;
    }
}
