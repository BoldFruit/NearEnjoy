package com.example.found.model.bean;

/**
 * @author DuLong
 * @since 2020/2/25
 * email 798382030@qq.com
 */
public class SearchUserResponse {
    /**
     * id : 5
     * schoolId : 0
     * name : 小橡果
     * avatar :
     * signature : null
     */

    private int id;
    private int schoolId;
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
