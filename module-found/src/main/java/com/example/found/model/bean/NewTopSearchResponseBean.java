package com.example.found.model.bean;

import java.util.List;

/**
 * @author DuLong
 * @since 2020/3/3
 * email 798382030@qq.com
 */
public class NewTopSearchResponseBean {
    private List<String> goodsTopSearchList;
    private List<BgTopSearchListBean> bgTopSearchList;

    public List<String> getGoodsTopSearchList() {
        return goodsTopSearchList;
    }

    public void setGoodsTopSearchList(List<String> goodsTopSearchList) {
        this.goodsTopSearchList = goodsTopSearchList;
    }

    public List<BgTopSearchListBean> getBgTopSearchList() {
        return bgTopSearchList;
    }

    public void setBgTopSearchList(List<BgTopSearchListBean> bgTopSearchList) {
        this.bgTopSearchList = bgTopSearchList;
    }

    public static class BgTopSearchListBean {
        /**
         * type : 活动
         * name : test
         * link : http://xxx/yyy
         */

        private String type;
        private String name;
        private String link;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
