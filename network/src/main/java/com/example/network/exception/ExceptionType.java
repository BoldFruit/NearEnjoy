package com.example.network.exception;

/**
 * 约定异常
 * @author YangZhaoxin.
 * @since 2020/2/13 11:33.
 * email yangzhaoxin@hrsoft.net.
 * Function
 */

public class ExceptionType {

    //未知错误
    public static final int UNKNOWN = 1000;
    // 解析(服务器)数据错误
    public static final int PARSE_SERVER_DATA_ERROR = 1001;
    // 解析(客户端)数据错误
    public static final int PARSE_CLIENT_DATA_ERROR = 1002;
    // 网络错误
    public static final int NETWORK_ERROR = 1003;
    // 协议出错
    public static final int HTTP_ERROR = 1004;
    // 证书出错
    public static final int SSL_ERROR = 1005;
    // 连接超时
    public static final int TIMEOUT_ERROR = 1006;
}
