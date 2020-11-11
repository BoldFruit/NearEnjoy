package com.example.lib_neuqer_chat.coder;

/**
 * Time:2020/3/15 17:10
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class MsgHeader {
    private int magicNumber = Spliter.magicNumber;
    private byte version = 1;
    private byte algorithm = CodecHelper.JSON_ALGORITHM;

    public void setMagicNumber(int magicNumber) {
        this.magicNumber = magicNumber;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public void setAlgorithm(byte algorithm) {
        this.algorithm = algorithm;
    }

    public int getMagicNumber() {
        return magicNumber;
    }

    public byte getVersion() {
        return version;
    }

    public byte getAlgorithm() {
        return algorithm;
    }

}