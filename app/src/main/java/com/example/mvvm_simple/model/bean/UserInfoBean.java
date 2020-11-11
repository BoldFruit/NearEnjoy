package com.example.mvvm_simple.model.bean;

/**
 * @author DuLong
 * @since 2020/4/14
 * email 798382030@qq.com
 */
public class UserInfoBean {
    /**
     * id : 5
     * schoolId : 1
     * phone : 13081859497
     * name : 小橡果
     * avatar :
     * signature : null
     */

    private int id;
    private int schoolId;
    private String phone;
    private String name;
    private String avatar;
    private String signature;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
