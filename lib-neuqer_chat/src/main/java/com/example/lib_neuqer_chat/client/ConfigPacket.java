package com.example.lib_neuqer_chat.client;

import com.example.lib_neuqer_chat.message.ChatMessageHeartBeat;
import com.example.lib_neuqer_chat.message.ChatMessageLogin;

/**
 * Time:2020/3/16 8:09
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: 基础配置包
 */
public abstract class ConfigPacket {

    /**
     * @return 返回前台心跳间隔
     */
    public abstract int getHeartIntervalF();

    /**
     * @return 返回后台心跳间隔
     */
    public abstract int getHeartIntervalB();

    /**
     * @return 返回重连超时限制
     */
    public abstract int getConnectTimeOut();

    /**
     * @return 返回重连次数
     */
    public abstract int getConnectCount();

    /**
     * @return 返回重连时间间隔
     */
    public abstract int getReconnectInterval();

    /**
     * @return 返回消息重发时间间隔
     */
    public abstract int getResendInterval();

    /**
     * @return 获得登陆消息
     */
    public abstract ChatMessageLogin buildLoginMessage();

    public abstract ChatMessageHeartBeat buildHeartBeatMessage();

    /**
     * @return 网络是否可用
     */
    public abstract boolean isNetAvailable();


}
