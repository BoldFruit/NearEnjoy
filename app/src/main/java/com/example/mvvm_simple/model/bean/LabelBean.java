package com.example.mvvm_simple.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author DuLong
 * @since 2020/3/14
 * email 798382030@qq.com
 */
public class LabelBean implements Parcelable {
    /**
     * id : 2
     * name : 可小刀
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

    public LabelBean() {
    }

    protected LabelBean(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<LabelBean> CREATOR =
            new Parcelable.Creator<LabelBean>() {
        @Override
        public LabelBean createFromParcel(Parcel source) {
            return new LabelBean(source);
        }

        @Override
        public LabelBean[] newArray(int size) {
            return new LabelBean[size];
        }
    };
}
