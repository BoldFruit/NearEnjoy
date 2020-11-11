package com.example.lib_neuqer_chat.message;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * Time:2020/3/14 16:35
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 *
 *
 *
 *                                                 i###
 *                                               #########
 *                                              ###########
 *                                             #############
 *                                            ####  ##t  ###,
 *                                            ###K #f#   ####
 *                                           ####K #t#j  ####
 *                                           #####  ##   ####E
 *                                           #################
 *                                           ####;;;;;;;;;####
 *                                           ###D;;;;;;;;;####
 *                                           ######;i;;L######
 *                                          tGD#############GG
 *                                          GGGGGGGGGGGGGGGGGGj
 *                                          GGGGGGGGGGGGGGGGGGG
 *                                         #WLGGGGGGGGGGGGGGGL#
 *                                         #### GGGGDDDG,   ####
 *                                         #### GGGG        ####
 *                                        ##### GGGG        ####
 *                                        ##### GGGG        #####
 *                                        #####             #####
 *                                        ##,##             ## ##
 *                                        #  ##            :##  #
 *                                           ##i           ##
 *                                            ##          i##
 *                                             ##        ,##
 *                                           :;;###     ##K;;
 *                                           ;;;;;######j;;;;;
 *
 *     QQ保佑，代码平安！！！！！！！！！！！！！！！！！！！！！！！！！！！
 *
 */
public abstract class Packet implements Serializable {

    public static final byte HEART_BEAT_COMMAND = 0;
    public static final byte LOGIN_COMMAND = 1;
    //normal chat
    public static final byte SINGLE_COMMAND = 2;
    public static final byte GROUP_COMMAND = 3;
    //the private chat which is from group chat
    public static final byte PRIVATE_COMMAND = 4;
    public static final byte SUPER_COMMAND = 5;
    private Byte version = 0;
    private Byte algorithm = -1;
    //用于特殊标记每一条消息，主要用于消息的重发
    private String uuid;
    private String dateTime;
    //表明是否是自己发送的消息，默认为true，接收时改变
    private boolean isMyself;

    public Packet() {
        uuid = UUID.randomUUID().toString();
        dateTime = getFormatTime();
        isMyself = true;
    }

    public abstract Byte getMsgCommand();
    public Byte getVersion() {
        return version;
    }

    public String getUuid() {
        return uuid;
    }

    public void setVersion(Byte version) {
        this.version = version;
    }

    public Byte getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Byte algorithm) {
        this.algorithm = algorithm;
    }

    private String getFormatTime() {

        return SimpleDateFormat.getDateTimeInstance().format(System.currentTimeMillis());
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setMyself(boolean myself) {
        isMyself = myself;
    }

    public boolean isMyself() {
        return isMyself;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
