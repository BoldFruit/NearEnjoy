package com.example.network.constant;

/**
 * @author YangZhaoxin.
 * @since 2020/2/13 17:47.
 * email yangzhaoxin@hrsoft.net.
 * Function
 */

public class Constants {

    // 网络请求连接超时时间，单位：s
    public static final int APP_SERVER_CONNECT_TIME_OUT = 15;

    // 读取超时，单位：s
    public static final int READ_TIME_OUT = 15;

    // 写入超时，单位：s
    public static final int WRITE_TIME_OUT = 15;

    // 同时最大请求数量
    public static final int MAX_REQUESTS_PER_HOST = 20;
}
