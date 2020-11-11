package com.example.lib_neuqer_chat.handler;

import com.example.lib_neuqer_chat.client.IClientInterface;
import com.example.lib_neuqer_chat.client.NQChatClient;
import com.example.lib_neuqer_chat.message.ChatMessageHeartBeat;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Time:2020/3/15 19:43
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: 用于监测用户的读写时间超时的handler
 * 详情参见 https://blog.csdn.net/u013967175/article/details/78591810
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    private NQChatClient chatClient;
    private HeartBeatTask task;

    public HeartBeatHandler(NQChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            switch (state){
                case READER_IDLE:
                    //读取超时
                    //TODO:如果服务端长时间不发消息，则重新连接
//                    chatClient.reConnect(false);
                    break;
                case WRITER_IDLE:
                    //写超时，即规定时间没有往服务端发送心跳包
                    if (task == null) {
                        task = new HeartBeatTask(ctx);
                    }
                    chatClient.getLoopFactory().execHeatBeatTask(task);
                    break;
            }
        }
    }

    private class HeartBeatTask implements Runnable {

        private ChannelHandlerContext ctx;

        private HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            if (ctx.channel().isActive()) {
                ChatMessageHeartBeat chatMessageHeartBeat = chatClient.getConfigPacket().buildHeartBeatMessage();
                if (chatMessageHeartBeat == null) {
                    throw new NullPointerException("didn't provide the heartbeat msg");
                }

                chatClient.sendMsg(chatMessageHeartBeat, false);
            }
        }
    }
}
