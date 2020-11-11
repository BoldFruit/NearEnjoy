package com.example.lib_common.checkpoint.base_single_check;

import android.util.SparseArray;

/**
 * Time:2020/5/6 11:06
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: 单选题控制，快速准确地保证只有一个可以被选中
 */
public class BaseSingleCheckController {

    private SparseArray<BaseSingleCheck> singleCheckMap = new SparseArray<>();
    private int checkCount = 0;
    private int currentCheckedTag = -1;

    public BaseSingleCheckController addCheck(BaseSingleCheck singleCheck) {
        singleCheck.setCheckController(this, checkCount);
        singleCheckMap.put(checkCount++, singleCheck);
        return this;
    }

    public int getCurrentTag() {
        return currentCheckedTag;
    }

    /**
     * 这里要注意，无论tag是为-1，都要讲checkcount和checktag置零
     * 因为可能存在没有选中！！！
     * */
    public void clearQuestion() {
        if (currentCheckedTag != -1) {
            singleCheckMap.get(currentCheckedTag).onCanceled();
            currentCheckedTag = -1;
        }
        checkCount = 0;

    }

    public void onClickedItem(BaseSingleCheck check, boolean isChecked) {
        if (isChecked) {
            if (currentCheckedTag != -1) {
                singleCheckMap.get(currentCheckedTag).onCanceled();
            }
            currentCheckedTag = check.getCheckTag();
            check.onSelected();
        } else {
            check.onCanceled();
        }
    }

}
