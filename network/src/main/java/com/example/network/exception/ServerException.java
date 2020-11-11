package com.example.network.exception;

/**
 * @author YangZhaoxin.
 * @since 2020/2/13 11:30.
 * email yangzhaoxin@hrsoft.net.
 * Function
 */

public class ServerException extends RuntimeException {

    private int code;
    private String msg;

    public ServerException(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
