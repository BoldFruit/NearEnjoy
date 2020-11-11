package com.example.found.model.bean;

/**
 * @author DuLong
 * @since 2020/3/3
 * email 798382030@qq.com
 */
public class HotSearchBean {
    //是否是管理员配置的
    private boolean isManagerSet;

    private String content;


    public boolean isManagerSet() {
        return isManagerSet;
    }

    public void setManagerSet(boolean mManagerSet) {
        isManagerSet = mManagerSet;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String mContent) {
        content = mContent;
    }
}
