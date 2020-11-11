package com.example.lib_neuqer_chat.client;

import com.example.lib_neuqer_chat.message.BaseChatMessage;
import com.example.lib_neuqer_chat.message.Packet;

/**
 * Time:2020/3/20 15:42
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: to convert or parse message you have received
 * S: Source
 * T: Target
 */
public interface NQWorkShop {

    void convert(BaseChatMessage source);
}
