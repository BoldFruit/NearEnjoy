package com.example.mvvm_simple.model.bean;

import java.util.List;

/**
 * @author DuLong
 * @since 2020/4/6
 * email 798382030@qq.com
 */
public class SecondClassificationBean {
    /**
     * searchList : [{"id":49,"userId":6,
     * "userName":"images/2020/1/28/7c3f852baca13c5e95775643355e724d.jpg","userAvatar":null,
     * "categoryId":2,"labelIds":"[\"2\"]","name":"高等数学","description":"高等数学",
     * "images":"[\"images/2020/1/28/7c3f852baca13c5e95775643355e724d.jpg\",
     * \"images/2020/1/28/7c3f852baca13c5e95775643355e724d.jpg\"]","price":15,"nums":2,"wants":0}]
     * prevPage : 0
     * nextPage : 2
     */

    private int prevPage;
    private int nextPage;
    private List<SearchListBean> searchList;

    public int getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public List<SearchListBean> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<SearchListBean> searchList) {
        this.searchList = searchList;
    }

    public static class SearchListBean {
        /**
         * id : 49
         * userId : 6
         * userName : images/2020/1/28/7c3f852baca13c5e95775643355e724d.jpg
         * userAvatar : null
         * categoryId : 2
         * labelIds : ["2"]
         * name : 高等数学
         * description : 高等数学
         * images : ["images/2020/1/28/7c3f852baca13c5e95775643355e724d.jpg",
         * "images/2020/1/28/7c3f852baca13c5e95775643355e724d.jpg"]
         * price : 15.0
         * nums : 2
         * wants : 0
         */

        private int id;
        private int userId;
        private String userName;
        private Object userAvatar;
        private int categoryId;
        private String labelIds;
        private String name;
        private String description;
        private String images;
        private double price;
        private int nums;
        private int wants;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Object getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(Object userAvatar) {
            this.userAvatar = userAvatar;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getLabelIds() {
            return labelIds;
        }

        public void setLabelIds(String labelIds) {
            this.labelIds = labelIds;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public int getWants() {
            return wants;
        }

        public void setWants(int wants) {
            this.wants = wants;
        }
    }
}
