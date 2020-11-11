package com.example.lib_common.checkpoint;

/**
 * Time:2020/5/6 8:31
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public interface ICheckPoint {
    void setPointTag(int tag);
    void onChecked();
    void onCanceled();
}
