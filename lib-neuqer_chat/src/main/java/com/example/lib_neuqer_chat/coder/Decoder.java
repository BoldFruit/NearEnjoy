package com.example.lib_neuqer_chat.coder;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * Time:2020/3/15 8:51
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class Decoder extends MessageToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        out.add(CodecHelper.decode((ByteBuf)msg));
    }
//    @Override
//    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        out.add(CodecHelper.decode(in));
//    }

}
