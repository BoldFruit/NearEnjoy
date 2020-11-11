package com.example.lib_neuqer_chat_ui.message;

import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * Time:2020/3/20 15:33
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class BaseMessage {
    public static final int SEND_FAILED_TYPE = 0;
    public static final int SEND_SENDING_TYPE = 1;
    public static final int SEND_SUCCESS_TYPE = 2;
    //超级管理员，启动富文本编辑方式
    public static final int SUPER_MANNAGER = 0;
    public static final int TXT = 1;
    public static final int IMG = 2;
    public static final int AUDIO = 3;
    public static final int VIDEO = 4;
    public static final int MAX_TYPE_INDEX = 4;
    //是否为自己发出的消息
    private boolean isMyself = true;
    private String time;
    private String uuid;

    private int type = -1;

    private int sendingType;

    public BaseMessage(int type) {
        sendingType = SEND_SENDING_TYPE;
        this.type = type;
        time = SimpleDateFormat.getDateTimeInstance().format(System.currentTimeMillis());
        uuid = UUID.randomUUID().toString();
    }

    public String getUuid() {
        return uuid;
    }

    public void setMyself(boolean myself) {
        isMyself = myself;
    }

    public boolean isMyself() {
        return isMyself;
    }

    public int getType() {

        if (type < 0) {
            throw new IllegalArgumentException("type is wrong!");
        }
        return type;

    }

    public void setType(int type) {
        this.type = type;
    }


    public void setSendingType(int sendingType) {
        this.sendingType = sendingType;
    }

    public int getSendingType() {
        return sendingType;
    }

    public String getTime() {
        return time;
    }


}
