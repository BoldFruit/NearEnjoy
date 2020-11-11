package com.example.lib_data;

import com.example.lib_data.data_user.NearEnjoyUser;

/**
 * Time:2020/2/21 14:00
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:app内存级别，
 * 1.存储一些在运行时需要的数据，不需要持久保存
 * 2.比如像用户信息这种需要经常IO操作的数据，每
 * 次从数据库查询比较耗时，可以登陆后将用户信息
 * 存到这里
 */
public class AppCurrentUser {

    private NearEnjoyUser currentUser;
    private String loginPhone;

    private AppCurrentUser() {
        currentUser = null;
    }

    private static final class AppCurrentUserHandler {
        private static final AppCurrentUser INSTANCE = new AppCurrentUser();
    }

    public static AppCurrentUser getInstance() {
        return AppCurrentUserHandler.INSTANCE;
    }

    public void setCurrentUser(NearEnjoyUser user) {
        this.currentUser = user;
    }

    public void setLoginPhone(String phone) {
        this.loginPhone = phone;
    }

    public String getLoginPhone() {
        return loginPhone;
    }
}
