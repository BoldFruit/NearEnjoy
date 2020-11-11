package com.example.lib_neuqer_chat.coder;

import com.example.lib_neuqer_chat.message.Packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Time:2020/3/15 18:25
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class Encoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        CodecHelper.encode(out, msg);
    }
}
