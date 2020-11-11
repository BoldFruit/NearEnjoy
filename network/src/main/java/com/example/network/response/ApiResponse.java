package com.example.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author YangZhaoxin.
 * @since 2020/2/13 11:15.
 * email yangzhaoxin@hrsoft.net.
 * Function 正确响应体
 */

public class ApiResponse<T> {

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("message")
    @Expose
    private String msg;

    @SerializedName("data")
    @Expose
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
