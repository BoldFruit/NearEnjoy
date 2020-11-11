package com.example.lib_neuqer_chat.message;

/**
 * Time:2020/3/15 16:35
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ChatMessageGroup<T> extends BaseChatMessage<T> {

    public ChatMessageGroup(long sessionId, long fromId, long toId, String type, T content) {
        super(sessionId, fromId, toId, type, content);
    }

    @Override
    public Byte getMsgCommand() {
        return Packet.GROUP_COMMAND;
    }
}
