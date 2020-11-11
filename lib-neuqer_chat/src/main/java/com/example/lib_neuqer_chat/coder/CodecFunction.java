package com.example.lib_neuqer_chat.coder;

import com.example.lib_neuqer_chat.message.Packet;

import io.netty.buffer.ByteBuf;

/**
 * Time:2020/3/15 14:22
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public interface CodecFunction<T extends Packet> {

    T decode(ByteBuf byteBuf, byte command);

    byte[] encode(ByteBuf byteBuf, Packet o);

}
