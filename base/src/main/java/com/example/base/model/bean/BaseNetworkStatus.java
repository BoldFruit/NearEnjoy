package com.example.base.model.bean;


import com.example.base.network.NetType;

import com.example.base.network.NetWorkStatus;

/**
 * @author YangZhaoxin.
 * @since 2020/2/5 11:47.
 * email yangzhaoxin@hrsoft.net.
 */

public class BaseNetworkStatus {

    NetWorkStatus status;
    String message;

    NetType netType;


    public BaseNetworkStatus() {
        status = NetWorkStatus.INIT;

    }

    public BaseNetworkStatus(NetWorkStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public NetWorkStatus getStatus() {
        return status;
    }

    public void setStatus(NetWorkStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NetType getNetType() {
        return netType;
    }

    public void setNetType(NetType netType) {
        this.netType = netType;
    }
}
