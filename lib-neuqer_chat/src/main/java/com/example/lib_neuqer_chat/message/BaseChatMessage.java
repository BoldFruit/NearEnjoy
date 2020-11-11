package com.example.lib_neuqer_chat.message;

import com.example.lib_neuqer_chat.config.ContentTypeMap;
import com.example.lib_neuqer_chat.config.DefaultConfig;

import androidx.lifecycle.MutableLiveData;

/**
 * Time:2020/3/14 16:35
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public abstract class BaseChatMessage<T> extends Packet {
    //the id of every single conversation
    private long sessionId = 0L;
    private long fromId;
    private long toId;
    private String type = ContentTypeMap.TXT;
    private MutableLiveData<DefaultConfig.SendType> sendTypeLiveData;
    private T content;

    public BaseChatMessage(long sessionId, long fromId, long toId, String type, T content) {
        this.sessionId = sessionId;
        this.fromId = fromId;
        this.toId = toId;
        this.type = type;
        this.content = content;
//        sendTypeLiveData = new MutableLiveData<>();
//        sendTypeLiveData.setValue(DefaultConfig.SendType.SENDING);
    }


    public long getSessionId() {
        return sessionId;
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public String getType() {
        return type;
    }

    public T getContent() {
        return content;
    }

    public MutableLiveData<DefaultConfig.SendType> getSendTypeLiveData() {
        return sendTypeLiveData;
    }
}
