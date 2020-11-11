package com.example.lib_neuqer_chat.handler;

import com.example.lib_neuqer_chat.client.NQChatClient;
import com.example.lib_neuqer_chat.coder.Decoder;
import com.example.lib_neuqer_chat.coder.Encoder;
import com.example.lib_neuqer_chat.coder.Spliter;
import com.example.lib_neuqer_chat.message.Packet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * Time:2020/3/17 10:02
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class NQInitializer extends ChannelInitializer<Channel> {

    private NQChatClient chatClient;

    public NQInitializer(NQChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new Spliter(chatClient.getMsgHeader().getMagicNumber()))
                .addLast(new Encoder())
                .addLast(new Decoder())
                .addLast(LoginHandler.class.getSimpleName(), new LoginHandler())
                .addLast(NQEventHandler.class.getSimpleName(), new NQEventHandler(chatClient));


    }
}
