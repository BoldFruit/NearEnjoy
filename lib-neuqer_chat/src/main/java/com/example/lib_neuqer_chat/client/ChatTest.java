package com.example.lib_neuqer_chat.client;

import com.example.lib_neuqer_chat.coder.CodecHelper;
import com.example.lib_neuqer_chat.config.ContentTypeMap;
import com.example.lib_neuqer_chat.config.DefaultConfig;
import com.example.lib_neuqer_chat.message.BaseChatMessage;
import com.example.lib_neuqer_chat.message.ChatMessageHeartBeat;
import com.example.lib_neuqer_chat.message.ChatMessageLogin;
import com.example.lib_neuqer_chat.message.ChatMessagePrivate;
import com.example.lib_neuqer_chat.message.Packet;

/**
 * Time:2020/3/17 12:13
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ChatTest  {

    public static void main(String args[]) {
        NQDispatcher.getInstance().register("test", new IReceiver() {
            @Override
            public void onReceive(BaseChatMessage packet) {
                System.out.println(packet);
            }

            @Override
            public void onLogin(Packet packet) {
                ChatMessagePrivate<String> chatMessagePrivate = new ChatMessagePrivate<>(1, 111, 222, ContentTypeMap.TXT,"sbsbsb");
                NQChatClient.getInstance().sendMsg(chatMessagePrivate);
            }

            @Override
            public void sendResult(BaseChatMessage packet, boolean isSuccess) {
                if (isSuccess) {
                    System.out.println("消息发送成功");
                    ChatMessagePrivate<String> chatMessagePrivate = new ChatMessagePrivate<>(1, 111, 222, ContentTypeMap.TXT,"sbsbsb");
                    NQChatClient.getInstance().sendMsg(chatMessagePrivate);
                }
            }

            @Override
            public void sendResult(BaseChatMessage packet, DefaultConfig.SendType type) {

            }
        });
        NQChatClient.getInstance().builder().setHeader(0x12345678, (byte) 1, CodecHelper.JSON_ALGORITHM);
        NQChatClient.getInstance().init(new Config(), "127.0.0.1:8001");
    }


    static class Config extends ConfigPacket {

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
