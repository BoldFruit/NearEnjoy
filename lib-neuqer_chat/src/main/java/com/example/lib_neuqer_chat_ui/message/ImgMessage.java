package com.example.lib_neuqer_chat_ui.message;

/**
 * Time:2020/3/20 17:52
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ImgMessage extends BaseMessage {

    //缩略图地址
    public String thumbImgPath = "";
    public String remoteImgPath = "";

    public ImgMessage(int type) {
        super(type);
    }

    public ImgMessage(int type, String thumbImgPath, String remoteImgPath) {
        super(type);
        this.thumbImgPath = thumbImgPath;
        this.remoteImgPath = remoteImgPath;
    }

    public void setThumbImgPath(String path) {
        this.thumbImgPath = path;
    }

    public String getRemoteImgPath() {
        return remoteImgPath;
    }

    public String getThumbImgPath() {
        return thumbImgPath;
    }
}
