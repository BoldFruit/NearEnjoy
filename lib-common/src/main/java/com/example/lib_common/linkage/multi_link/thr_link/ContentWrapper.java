package com.example.lib_common.linkage.multi_link.thr_link;

/**
 * Time:2020/5/18 9:06
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ContentWrapper {
    public static final int PRO_SER = 0;//省级数
    public static final int CITY_SER = 1;//城市级数
    public static final int SCHOOL_SER = 2;//学校级数
    private int id;
    private String name;
    private int ser;

    public ContentWrapper(int id, String name, int ser) {
        this.id = id;
        this.name = name;
        this.ser = ser;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSer() {
        return ser;
    }
}
