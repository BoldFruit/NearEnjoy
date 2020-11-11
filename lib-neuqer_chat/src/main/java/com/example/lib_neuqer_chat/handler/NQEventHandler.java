package com.example.lib_neuqer_chat.handler;

import com.example.lib_neuqer_chat.client.NQChatClient;
import com.example.lib_neuqer_chat.client.NQDispatcher;
import com.example.lib_neuqer_chat.message.Packet;

import io.netty.channel.ChannelHandlerContext;

/**
 * Time:2020/3/17 11:07
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class NQEventHandler extends NQHandler<Packet> {

    public NQEventHandler(NQChatClient chatClient) {
        super(chatClient);
    }

    @Override
    void onGetMessage(ChannelHandlerContext ctx, Packet msg) {
        NQDispatcher.getInstance().receiveMsgReact(msg);
    }
}
