package com.example.lib_neuqer_chat.config;

import java.util.HashMap;

/**
 * Time:2020/3/14 16:35
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ContentTypeMap {

        private static final String TXT_TYPE_KEY = "txt_key";
        private static final String IMG_TYPE_KEY = "img_key";
        private static final String FILE_TYPE_KEY = "file_key";
        private static final String EMOJI_TYPE_KEY = "emoji_key";
        public static final String TXT = "txt";
        public static final String IMG = "img";
        public static final String FILE = "file";
        public static final String EMOJI = "emoji";
        static HashMap<String, String> typeMap = new HashMap<>();
        static {
            typeMap.put(TXT_TYPE_KEY, TXT);
            typeMap.put(IMG_TYPE_KEY, IMG);
            typeMap.put(EMOJI_TYPE_KEY, EMOJI);
            typeMap.put(FILE_TYPE_KEY, FILE);
        }
        public static void expandType(String key, String typeName) throws Exception {
           if (typeMap.containsKey(key)) {
               throw new Exception("ContentTypeMap-expandType(): the key you provide makes conflict with the origin keys");
           }
            typeMap.put(key, typeName);
        }
        
    }