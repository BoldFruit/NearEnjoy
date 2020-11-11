package com.example.lib_common.checkpoint;

import android.util.Half;
import android.util.SparseArray;

/**
 * Time:2020/5/6 8:40
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class CheckPointController {
    private int pointNumber = 0;
    private int currentCheckedTag = 0;
    private SparseArray<CheckPoint> pointMap = new SparseArray<>();

    public CheckPointController addPoint(CheckPoint point) {
        point.setPointTag(pointNumber);
        point.setController(this);
        pointMap.append(pointNumber++, point);
        return this;
    }

    public void onClickPoint(CheckPoint point, boolean isChecked) {
        if (isChecked) {
            pointMap.get(currentCheckedTag).onCanceled();
            currentCheckedTag = point.getPointTag();
            point.onChecked();
        } else {
            point.onCanceled();
        }
    }

}
