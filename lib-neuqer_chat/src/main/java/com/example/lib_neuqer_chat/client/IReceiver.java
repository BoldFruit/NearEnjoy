package com.example.lib_neuqer_chat.client;

import com.example.lib_neuqer_chat.config.DefaultConfig;
import com.example.lib_neuqer_chat.message.BaseChatMessage;
import com.example.lib_neuqer_chat.message.Packet;

/**
 * Time:2020/3/16 10:03
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public interface IReceiver {

    void onReceive(BaseChatMessage packet);

    void onLogin(Packet packet);

    void sendResult(BaseChatMessage packet, boolean isSuccess);

    void sendResult(BaseChatMessage packet, DefaultConfig.SendType type);

}
