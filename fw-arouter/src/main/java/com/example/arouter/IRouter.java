package com.example.arouter;

/**
 * @author YangZhaoxin.
 * @since 2020/1/18 16:31.
 * email yangzhaoxin@hrsoft.net.
 */

public interface IRouter {

    /**
     * 顶层接口，用于将所有模块中的类对象加入到容器中
     */
    void putActivity();
}
