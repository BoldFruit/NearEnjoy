package com.example.lib_neuqer_chat_ui.message;

/**
 * Time:2020/3/20 16:17
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class TxtMessage extends BaseMessage {

    private String content;

    public TxtMessage(int type, String content) {
        super(type);
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
