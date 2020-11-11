package com.example.lib_neuqer_chat.excutor;

import android.util.Log;

import com.example.lib_neuqer_chat.client.IClientInterface;
import com.example.lib_neuqer_chat.message.Packet;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.netty.util.internal.StringUtil;

/**
 * Time:2020/3/16 21:30
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class MsgTimeOutManager {

    private IClientInterface iClientInterface;
    //线程安全的HashMap
    private ConcurrentMap<String, MsgTimeoutTimer> msgTimeOutMap = new ConcurrentHashMap<>();

    public MsgTimeOutManager(IClientInterface iClientInterface) {
        this.iClientInterface = iClientInterface;
    }

    public void add(Packet packet) {
       if (packet != null) {
           if (!(packet.getMsgCommand() == Packet.HEART_BEAT_COMMAND
                   || packet.getMsgCommand() == Packet.LOGIN_COMMAND)) {
               if (!msgTimeOutMap.containsKey(packet.getUuid())) {
                   msgTimeOutMap.put(packet.getUuid(), new MsgTimeoutTimer(iClientInterface, packet));
               }
           }
       } else {
           throw new NullPointerException("message content shouldn't be null");
       }
    }

    /**
     * 开启重发任务
     * @param key
     */
    public void startTimerTask(String key) {
        if (msgTimeOutMap.containsKey(key)) {
            MsgTimeoutTimer timeoutTimer = msgTimeOutMap.get(key);
            if (timeoutTimer != null) {
                timeoutTimer.startTimeTask();
            }
        }
    }

    /**
     * 将消息从消息队列钟移除
     * @param key
     */
    public void removeTimeoutMsg(String key) {
        if (StringUtil.isNullOrEmpty(key)) {
            throw new NullPointerException("null key");
        }

        MsgTimeoutTimer timeoutTimer = msgTimeOutMap.remove(key);
        if (timeoutTimer != null) {
            timeoutTimer.cancel();
            timeoutTimer = null;
        }
    }

    /**
     * 当网络重新连接时，需要将存储的未发出的消息再发出去
     */
    public void onReconnect() {
        for (Iterator<Map.Entry<String, MsgTimeoutTimer>> iterator = msgTimeOutMap.entrySet().iterator();
        iterator.hasNext();) {
            iterator.next().getValue().sendMessage();
        }
    }

}
