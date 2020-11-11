package com.example.lib_neuqer_chat.config;

/**
 * Time:2020/3/15 14:35
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class DefaultConfig {


    public enum ConnectType {

        FAILED,

        SUCCESS,

        CONNECTING

    }

    public enum SendType {

        SENDING,

        SEND_SUCCESS,

        SEND_FAILD
    }

    public enum AppStatus {

        FOREGRAOUND,

        BACKGROUND

    }

    public static final byte DEFAULT_DECODC_ALGORITHM = 0;
    //连接超时的时间
    public static final int DEFAULT_CONNECT_TIMEOUT = 10 * 1000;
    //默认重连次数
    public static final int DEFAULT_RECONNECT_COUNT = 3;
    //重新连接间隔时间
    public static final int DEFAULT_RECONNECT_DELAY_TIME = 3 * 1000;

    //消息重发间隔
    public static final int DEFAULT_RESEND_INTERVAL = 8 * 1000;

    public static final int DEFAULT_HEARTBEAT_INTERVAL_FOREGROUND = 3 * 1000;

    public static final int DEFAULT_HEARTBEAT_INTERVAL_BACKGROUND = 30 * 1000;

}
