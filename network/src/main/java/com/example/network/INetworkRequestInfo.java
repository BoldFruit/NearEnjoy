package com.example.network;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author YangZhaoxin.
 * @since 2020/2/7 19:18.
 * email yangzhaoxin@hrsoft.net.
 * Function 暴露给外边去自定义实现的接口
 */

public interface INetworkRequestInfo {

    HashMap<String, String> getRequestHeadersMap();
    boolean isDebug();
    String getBaseUrl();
    ArrayList<Integer> getNetCorrectCode();
}
