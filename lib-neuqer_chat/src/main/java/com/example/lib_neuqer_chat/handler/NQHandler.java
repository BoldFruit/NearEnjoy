package com.example.lib_neuqer_chat.handler;

import com.example.lib_neuqer_chat.client.NQChatClient;
import com.example.lib_neuqer_chat.message.Packet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Time:2020/3/17 10:53
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public abstract class NQHandler<T> extends SimpleChannelInboundHandler<T> {

    protected NQChatClient chatClient;

    public NQHandler(NQChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        reConnect(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        reConnect(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception {
        onGetMessage(ctx, msg);
    }

    abstract void onGetMessage(ChannelHandlerContext ctx, T msg);

    private void reConnect(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        if (channel != null) {
            channel.close();
            ctx.close();
        }

        chatClient.reConnect(false);
    }
}
