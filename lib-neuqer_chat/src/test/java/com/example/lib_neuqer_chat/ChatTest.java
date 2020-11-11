package com.example.lib_neuqer_chat;

import com.example.lib_neuqer_chat.client.ConfigPacket;
import com.example.lib_neuqer_chat.client.IReceiver;
import com.example.lib_neuqer_chat.client.NQChatClient;
import com.example.lib_neuqer_chat.client.NQDispatcher;
import com.example.lib_neuqer_chat.coder.CodecHelper;
import com.example.lib_neuqer_chat.coder.Spliter;
import com.example.lib_neuqer_chat.config.ContentTypeMap;
import com.example.lib_neuqer_chat.config.DefaultConfig;
import com.example.lib_neuqer_chat.message.BaseChatMessage;
import com.example.lib_neuqer_chat.message.ChatMessageHeartBeat;
import com.example.lib_neuqer_chat.message.ChatMessageLogin;
import com.example.lib_neuqer_chat.message.ChatMessagePrivate;
import com.example.lib_neuqer_chat.message.Packet;

import org.junit.Test;

/**
 * Time:2020/3/17 12:13
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ChatTest implements IReceiver {

    @Test
    public void init() {
        NQChatClient.getInstance().builder().setHeader(0x12345678, (byte) 1, CodecHelper.JSON_ALGORITHM);
        NQChatClient.getInstance().init(new Config(), "127.0.0.1:8001");
    }


    @Override
    public void onReceive(BaseChatMessage packet) {

    }

    @Override
    public void onLogin(Packet packet) {
        ChatMessagePrivate<String> chatMessagePrivate = new ChatMessagePrivate<>(1, 111, 222, ContentTypeMap.TXT,"sbsbsb");
        NQChatClient.getInstance().sendMsg(chatMessagePrivate);
    }


    @Override
    public void sendResult(Packet packet, boolean isSuccess) {
        System.out.println(packet.getUuid() + "发送失败" );
    }

    class Config extends ConfigPacket {

        @Override
        public int getHeartIntervalF() {
            return 0;
        }

        @Override
        public int getHeartIntervalB() {
            return 0;
        }

        @Override
        public int getConnectTimeOut() {
            return 0;
        }

        @Override
        public int getConnectCount() {
            return 3;
        }

        @Override
        public int getReconnectInterval() {
            return 0;
        }

        @Override
        public int getResendInterval() {
            return 0;
        }

        @Override
        public ChatMessageLogin buildLoginMessage() {
            return new ChatMessageLogin(111);
        }

        @Override
        public ChatMessageHeartBeat buildHeartBeatMessage() {
            return new ChatMessageHeartBeat(111);
        }

        @Override
        public boolean isNetAvailable() {
            return true;
        }
    }
}
