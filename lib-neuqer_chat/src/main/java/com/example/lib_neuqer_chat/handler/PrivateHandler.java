package com.example.lib_neuqer_chat.handler;

import com.example.lib_neuqer_chat.client.NQChatClient;
import com.example.lib_neuqer_chat.message.ChatMessagePrivate;

import io.netty.channel.ChannelHandlerContext;

/**
 * Time:2020/3/17 11:01
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class PrivateHandler extends NQHandler<ChatMessagePrivate> {

    public PrivateHandler(NQChatClient chatClient) {
        super(chatClient);
    }

    @Override
    void onGetMessage(ChannelHandlerContext ctx, ChatMessagePrivate msg) {

    }
}
