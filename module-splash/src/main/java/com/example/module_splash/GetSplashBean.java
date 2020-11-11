package com.example.module_splash;

/**
 * @author DuLong
 * @since 2020/5/4
 * email 798382030@qq.com
 */
public class GetSplashBean {
    /**
     * id : 1
     * upperImage : /images/xxx/yyy.jpg
     * bgColor : #123456
     * bgImage : /images/xxx/yyy.jpg
     * showed : true
     */

    private int id;
    private String upperImage;
    private String bgColor;
    private String bgImage;
    private boolean showed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpperImage() {
        return upperImage;
    }

    public void setUpperImage(String upperImage) {
        this.upperImage = upperImage;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getBgImage() {
        return bgImage;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    public boolean isShowed() {
        return showed;
    }

    public void setShowed(boolean showed) {
        this.showed = showed;
    }
}
