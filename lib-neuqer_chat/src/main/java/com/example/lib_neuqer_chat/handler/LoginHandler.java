package com.example.lib_neuqer_chat.handler;

import com.example.lib_neuqer_chat.client.NQChatClient;
import com.example.lib_neuqer_chat.client.NQDispatcher;
import com.example.lib_neuqer_chat.message.ChatMessageLogin;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Time:2020/3/15 19:43
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class LoginHandler extends SimpleChannelInboundHandler<ChatMessageLogin> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessageLogin msg) throws Exception {
        NQDispatcher.getInstance().receiveMsgReact(msg);
        NQChatClient.getInstance().addHeartBeatHandler();
    }
}
