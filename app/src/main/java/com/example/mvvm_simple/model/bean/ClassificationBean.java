package com.example.mvvm_simple.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author DuLong
 * @since 2020/4/6
 * email 798382030@qq.com
 */
public class ClassificationBean {
    /**
     * id : 1
     * name : 书籍111
     * secondList : [{"id":2,"name":"教材111"},{"id":5,"name":"教辅"},{"id":13,"name":"大苏打"},{"id
     * ":12,"name":"纪家新"}]
     */

    private int id;
    private String name;
    private List<SecondListBean> secondList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SecondListBean> getSecondList() {
        return secondList;
    }

    public void setSecondList(List<SecondListBean> secondList) {
        this.secondList = secondList;
    }

    public static class SecondListBean implements Parcelable {
        /**
         * id : 2
         * name : 教材111
         */

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
        }

        public SecondListBean() {
        }

        protected SecondListBean(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
        }

        public static final Parcelable.Creator<SecondListBean> CREATOR =
                new Parcelable.Creator<SecondListBean>() {
            @Override
            public SecondListBean createFromParcel(Parcel source) {
                return new SecondListBean(source);
            }

            @Override
            public SecondListBean[] newArray(int size) {
                return new SecondListBean[size];
            }
        };
    }
}
