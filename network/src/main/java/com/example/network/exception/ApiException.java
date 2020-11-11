package com.example.network.exception;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;

/**
 * @author YangZhaoxin.
 * @since 2020/2/13 11:27.
 * email yangzhaoxin@hrsoft.net.
 * Function 异常响应体
 */

public class ApiException extends IOException {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String msg;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public ApiException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
