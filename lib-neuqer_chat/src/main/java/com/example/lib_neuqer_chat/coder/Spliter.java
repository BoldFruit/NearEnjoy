package com.example.lib_neuqer_chat.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Time:2020/3/14 22:50
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: This class is used for unpacking data pack;
 *
 * DataBody:
 * +--------------+---------+----------+---------+--------------+------+
 * | magic_number | version | algorithm| command | data_length | data |
 * | 4byte        | 1byte   | 1byte    | 1byte   | 4byte       |      |
 * +--------------+---------+---------+----------+-------------+------+
 *
 */
public class Spliter extends LengthFieldBasedFrameDecoder {

    public static int magicNumber = 0x12345678;
    public static final int MAX_LENGTH = Integer.MAX_VALUE;
    public static final int MAGIC_NUMBER_LENGTH = 4;
    public static final int VERSION_LENGTH = 1;
    public static final int ALGORITHM_LENGTH = 1;
    public static final int COMMAND_LENGTH = 1;
    public static final int LENGTH_FIELD_LENGTH = 4;
    public static int dataLength = 0;
    public static final int LENGTH_FILED_OFFSET = MAGIC_NUMBER_LENGTH +
            VERSION_LENGTH + ALGORITHM_LENGTH + COMMAND_LENGTH;


    public Spliter(int magic) {
        //because the LENGTH_FIELD_LENGTH is show the length of real data, so the lengthAdjustment is 0, then we
        //just need to call the
        // LengthFieldBasedFrameDecoder(
        //            int maxFrameLength,
        //            int lengthFieldOffset, int lengthFieldLength)
        super(MAX_LENGTH, LENGTH_FILED_OFFSET, LENGTH_FIELD_LENGTH);
        magicNumber = magic;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        int magic = in.getInt(in.readerIndex());
        if (magic != magicNumber) {
            ctx.channel().close();
            throw new IllegalArgumentException("unknown magic number, maybe you should have a good talk with your back-end engineer!");
        }
        return super.decode(ctx, in);
    }


}
