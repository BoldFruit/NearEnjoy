package com.example.lib_neuqer_chat.coder;

import com.alibaba.fastjson.JSON;
import com.example.lib_neuqer_chat.message.ChatMessageGroup;
import com.example.lib_neuqer_chat.message.ChatMessageHeartBeat;
import com.example.lib_neuqer_chat.message.ChatMessageLogin;
import com.example.lib_neuqer_chat.message.ChatMessagePrivate;
import com.example.lib_neuqer_chat.message.ChatMessagePrivatePlus;
import com.example.lib_neuqer_chat.client.NQChatClient;
import com.example.lib_neuqer_chat.message.Packet;
import com.google.gson.Gson;

import io.netty.buffer.ByteBuf;

/**
 * Time:2020/3/15 8:49
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class CodecHelper {

    public static int magicNumber = Spliter.magicNumber;
    public static byte JSON_ALGORITHM = 1;

    public static Packet decode(ByteBuf byteBuf) {
        byteBuf.skipBytes(Spliter.MAGIC_NUMBER_LENGTH)//magic number length
                .skipBytes(Spliter.VERSION_LENGTH);//version number
        byte algorithm = byteBuf.readByte();//algorithm number
        byte command = byteBuf.readByte();
        int size = byteBuf.readInt();
        if (algorithm == JSON_ALGORITHM) {
            return jsonDecode(byteBuf, size, command);
        } else {
            return customDecode(byteBuf, algorithm, command);
        }

    }

    public static void encode(ByteBuf byteBuf, Packet packet) {
        byte algorithm;
        if (packet.getAlgorithm() < 0) {
            algorithm = NQChatClient.getInstance().getMsgHeader().getAlgorithm();
        } else {
            algorithm = packet.getAlgorithm();
        }

        if (algorithm == JSON_ALGORITHM) {
            jsonEncode(byteBuf, packet, algorithm);
        } else {
            customEncode(byteBuf, packet, algorithm);
        }

    }

    private static void customEncode(ByteBuf byteBuf, Packet packet, byte algorithm) {
        NQChatClient.getInstance().getCodecFunction(algorithm).encode(byteBuf, packet);
    }

    private static void jsonEncode(ByteBuf byteBuf, Packet packet, byte algorithm) {
        byteBuf.writeInt(magicNumber)
                .writeByte(packet.getVersion() < 0 ?
                        NQChatClient.getInstance().getMsgHeader().getVersion()
                        : packet.getVersion())//If you customized the version when sending data, use the version when sending the message
                .writeByte(algorithm)
                .writeByte(packet.getMsgCommand())
                .writeInt(JsonUtil.object2bytes(packet).length)
//                .writeBytes(JSON.toJSONBytes(packet));
                .writeBytes(JsonUtil.object2bytes(packet));
    }


    /**
     * 解析二进制json数组
     * @param byteBuf
     * @param size
     * @param command
     * @return
     */
    private static Packet jsonDecode(ByteBuf byteBuf, int size, byte command) {
        byte[] bytes = new byte[size];
        byteBuf.readBytes(bytes);
        Class<? extends Packet> clazz = getClazz(command);
        Gson gson = new Gson();
        return gson.fromJson(JsonUtil.bytes2json(bytes), clazz);
    }

    /**
     * 自定义解析方式
     * @param byteBuf
     * @param algorithm
     * @param command
     * @return
     */
    private static Packet customDecode(ByteBuf byteBuf, byte algorithm, byte command) {
        CodecFunction function = NQChatClient.getInstance().getCodecFunction(algorithm);
        return function.decode(byteBuf, command);
    }

    private static Class<? extends Packet> getClazz(byte command) {
        switch (command) {
            case Packet.HEART_BEAT_COMMAND:
                return ChatMessageHeartBeat.class;
            case Packet.LOGIN_COMMAND:
                return ChatMessageLogin.class;
            case Packet.SINGLE_COMMAND:
                return ChatMessagePrivatePlus.class;
            case Packet.GROUP_COMMAND:
                return ChatMessageGroup.class;
            case Packet.PRIVATE_COMMAND:
                return ChatMessagePrivate.class;
            case Packet.SUPER_COMMAND:
                //TODO:Manager Class
            default:
                return NQChatClient.getInstance().get7PlusCommandClass(command);

        }
    }



}
