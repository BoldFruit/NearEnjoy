package com.example.lib_neuqer_chat.coder;

import android.os.Build;

import com.example.lib_neuqer_chat.message.Packet;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Time:2020/3/15 16:29
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class JsonUtil {

    static String bytes2json(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    static byte[] object2bytes(Packet packet) {
        Gson gson = new Gson();
        String jsonPacket = gson.toJson(packet);
        return jsonPacket.getBytes();
    }

}
