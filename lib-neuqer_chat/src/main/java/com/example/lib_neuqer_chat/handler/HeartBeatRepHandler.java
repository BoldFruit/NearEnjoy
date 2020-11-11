package com.example.lib_neuqer_chat.handler;

import com.example.lib_neuqer_chat.message.ChatMessageHeartBeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Time:2020/3/15 19:44
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class HeartBeatRepHandler extends SimpleChannelInboundHandler<ChatMessageHeartBeat> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessageHeartBeat msg) throws Exception {

    }
}
