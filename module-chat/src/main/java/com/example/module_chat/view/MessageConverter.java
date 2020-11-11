package com.example.module_chat.view;

import com.alibaba.fastjson.JSON;
import com.example.lib_neuqer_chat.coder.JsonUtil;
import com.example.lib_neuqer_chat.config.ContentTypeMap;
import com.example.lib_neuqer_chat.message.BaseChatMessage;
import com.example.lib_neuqer_chat.message.ChatMessagePrivate;
import com.example.lib_neuqer_chat.message.ChatMessagePrivatePlus;
import com.example.lib_neuqer_chat.message.Packet;
import com.example.lib_neuqer_chat_ui.message.BaseMessage;
import com.example.lib_neuqer_chat_ui.message.TxtMessage;
import com.google.gson.Gson;

/**
 * Time:2020/3/27 16:41
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class MessageConverter {

    public static ChatMessagePrivatePlus<TxtMessage> txtMsg2chatMsg(TxtMessage message, long sessionId, long goodsId, long toId) {
        ChatMessagePrivatePlus<TxtMessage> txtMessageChatMessage = new ChatMessagePrivatePlus<>(sessionId, 111, goodsId, toId, ContentTypeMap.TXT, message);
        txtMessageChatMessage.setUuid(message.getUuid());
        return txtMessageChatMessage;

    }

    public static TxtMessage chatMsg2txtMsg(BaseChatMessage messagePrivatePlus) {
        String tempStr = (String) messagePrivatePlus.getContent();
        Gson gson = new Gson();
        TxtMessage message = gson.fromJson(tempStr, TxtMessage.class);
        message.setMyself(false);
        return message;
    }

}
