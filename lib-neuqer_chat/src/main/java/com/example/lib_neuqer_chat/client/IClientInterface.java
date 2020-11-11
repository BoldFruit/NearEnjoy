package com.example.lib_neuqer_chat.client;

import com.example.lib_neuqer_chat.config.DefaultConfig;
import com.example.lib_neuqer_chat.excutor.MsgTimeOutManager;
import com.example.lib_neuqer_chat.message.Packet;

import java.util.Vector;

/**
 * Time:2020/3/15 20:46
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public interface IClientInterface {

    void init(Vector<String> urlList, ConfigPacket configPacket);

    void reConnect();

    void reConnect(boolean isFirst);

    void sendMsg(Packet packet);

    void sendMsg(Packet packet, boolean isJoinToTimerManager);

    void close();

    void setAppStatus(DefaultConfig.AppStatus status);

    boolean isClosed();

    ConfigPacket getConfigPacket();

    int getHeartBeatInterval();

    int getReconnectInterval();

    int getReconnectCount();

    int getResentInterval();

    int getConnectTimeout();

    boolean getNetworkAvailable();

    MsgTimeOutManager getTimeOutManager();
}
