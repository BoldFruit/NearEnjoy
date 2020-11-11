package com.example.module_chat.view;

import com.example.lib_neuqer_chat.client.ConfigPacket;
import com.example.lib_neuqer_chat.message.ChatMessageHeartBeat;
import com.example.lib_neuqer_chat.message.ChatMessageLogin;

/**
 * Time:2020/3/29 14:42
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ChatConnectPacket extends ConfigPacket {
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
        return 0;
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
