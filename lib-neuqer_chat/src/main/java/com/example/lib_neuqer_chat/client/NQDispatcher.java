package com.example.lib_neuqer_chat.client;

import com.example.lib_neuqer_chat.config.DefaultConfig;
import com.example.lib_neuqer_chat.message.BaseChatMessage;
import com.example.lib_neuqer_chat.message.Packet;

import java.util.HashMap;
import java.util.Map;

/**
 * Time:2020/3/16 9:48
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class NQDispatcher {

    public static final String CONNECT_STATUS_KEY = "connect_status";
    public static final int RECEIVE_TYPE = 0;
    public static final int SEND_TYPE = 1;
    Map<String, IReceiver> receiverMap;

    private NQDispatcher() {
        receiverMap = new HashMap<>();
    }

    private static final class NQDispatcherHolder {
        private static final NQDispatcher INSTANCE = new NQDispatcher();
    }

    public static NQDispatcher getInstance() {
        return NQDispatcherHolder.INSTANCE;
    }

    /**
     * 对连接状态进行回调
     * @param type
     */
    public void connectStatusReact(DefaultConfig.ConnectType type) {
        //TODO:进行全局广播

        LiveDataDispatcherBus.getInstance().with(CONNECT_STATUS_KEY).postValue(type);
    }

    /**
     * 回调发送消息的结果
     * @param packet
     * @param isSuccess
     */
    public void sendMsgReact(Packet packet, boolean isSuccess) {
       if (receiverMap.size() == 0) {
           throw new NullPointerException("map is null");
       } else {
           if (packet instanceof BaseChatMessage) {
               for (Map.Entry<String, IReceiver> entry:
                       receiverMap.entrySet()) {
                   entry.getValue().sendResult((BaseChatMessage) packet, isSuccess);
               }
           }
       }
    }


    public void sendMsgReact(Packet packet, DefaultConfig.SendType type) {
        if (receiverMap.size() == 0) {
            throw new NullPointerException("map is null");
        } else {
            if (packet instanceof BaseChatMessage) {
                for (Map.Entry<String, IReceiver> entry:
                        receiverMap.entrySet()) {
                    entry.getValue().sendResult((BaseChatMessage) packet, type);
                }
            }
        }
    }


    /**
     * 接收消息回调
     * @param packet
     */
    public void receiveMsgReact(Packet packet) {

        receiveMsgReact(packet, null);
    }

    public void receiveMsgReact(Packet packet, String key) {
        if (packet == null)  {
            return;
        }

        /**
         * 信息是接收到的，设置isMySelf为false
         */
        packet.setMyself(false);

        if (key == null) {
            if (NQChatClient.getInstance().isInfoGlobal()) {
                //TODO:LiveDataBus广播
            } else {
                if (receiverMap == null || receiverMap.size() == 0) {
                    throw new NullPointerException("map is null");
                } else {
                    for (Map.Entry<String, IReceiver> entry:
                            receiverMap.entrySet()) {
                        if (packet.getMsgCommand() == Packet.LOGIN_COMMAND) {
                            //login successfully
                            entry.getValue().onLogin(packet);
                        } else {
                            //heartbeat message should not be reply to UI
                            if (!(packet.getMsgCommand() == Packet.HEART_BEAT_COMMAND)) {
                                entry.getValue().onReceive((BaseChatMessage) packet);
                            }
                        }
                    }
                }
            }
        } else {
            if (NQChatClient.getInstance().isInfoGlobal()) {
                //TODO:LiveDataBus广播
            } else {
                if (receiverMap == null || receiverMap.size() == 0) {
                    throw new NullPointerException("map is null");
                } else if (!receiverMap.containsKey(key)) {
                    throw new IllegalArgumentException("not register such key");
                } else {
                    if (receiverMap.get(key) != null) {
                        receiverMap.get(key).onReceive((BaseChatMessage) packet);
                    } else {
                        throw new NullPointerException("the value of " + key + "is null");
                    }
                }
            }
        }
    }

    public void register(String key, IReceiver receiver) {
        if (key != null && receiver != null) {
            receiverMap.put(key, receiver);
        }
    }

    public void unRegister(String key) {
        if (receiverMap != null) {
            receiverMap.remove(key);
        }
    }

    public void unRegister() {
        if (receiverMap != null) {
            receiverMap.clear();
        }
    }
}
