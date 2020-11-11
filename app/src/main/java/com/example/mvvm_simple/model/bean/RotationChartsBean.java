package com.example.mvvm_simple.model.bean;

/**
 * @author DuLong
 * @since 2020/4/6
 * email 798382030@qq.com
 */
public class RotationChartsBean {
    /**
     * type : 图片
     * image : /images/xxx/yyy.jpg
     * link : null
     */

    private String type;
    private String image;
    private String link;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
