package com.example.mvvm_simple.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DuLong
 * @since 2020/4/14
 * email 798382030@qq.com
 */
public class UserGoodsBean implements Parcelable {

    /**
     * searchList : [{"id":50,"categoryId":2,"labelIds":"[\"3\"]","name":"高等数学",
     * "description":"高等数学","images":"[\"https://blog.csxjh.vip/usr/uploads/2020/03/3694667439
     * .jpg\", \"https://blog.csxjh.vip/usr/uploads/2020/03/2424861305.jpg\"]","price":12.88,
     * "nums":0,"wants":0},{"id":49,"categoryId":2,"labelIds":"[\"2\"]","name":"高等数学",
     * "description":"高等数学","images":"[\"https://blog.csxjh.vip/usr/uploads/2020/03/3694667439
     * .jpg\", \"https://blog.csxjh.vip/usr/uploads/2020/03/2424861305.jpg\"]","price":15,
     * "nums":2,"wants":0}]
     * prevPage : 1
     * nextPage : 3
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
         * id : 50
         * categoryId : 2
         * labelIds : ["3"]
         * name : 高等数学
         * description : 高等数学
         * images : ["https://blog.csxjh.vip/usr/uploads/2020/03/3694667439.jpg", "https://blog
         * .csxjh.vip/usr/uploads/2020/03/2424861305.jpg"]
         * price : 12.88
         * nums : 0
         * wants : 0
         */

        private int id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.prevPage);
        dest.writeInt(this.nextPage);
        dest.writeList(this.searchList);
    }

    public UserGoodsBean() {
    }

    protected UserGoodsBean(Parcel in) {
        this.prevPage = in.readInt();
        this.nextPage = in.readInt();
        this.searchList = new ArrayList<SearchListBean>();
        in.readList(this.searchList, SearchListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserGoodsBean> CREATOR =
            new Parcelable.Creator<UserGoodsBean>() {
        @Override
        public UserGoodsBean createFromParcel(Parcel source) {
            return new UserGoodsBean(source);
        }

        @Override
        public UserGoodsBean[] newArray(int size) {
            return new UserGoodsBean[size];
        }
    };
}
