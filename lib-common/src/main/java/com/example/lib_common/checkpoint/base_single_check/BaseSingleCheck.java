package com.example.lib_common.checkpoint.base_single_check;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

/**
 * Time:2020/5/6 11:00
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: 封装单选题的基类，具备复选功能
 */
public abstract class BaseSingleCheck extends RelativeLayout {
    protected boolean isChecked = false;
    protected int checkTag = 0;
    protected BaseSingleCheckController checkController;

    public BaseSingleCheck(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BaseSingleCheck(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setCheckController(BaseSingleCheckController checkController, int checkTag) {
        this.checkController = checkController;
        this.checkTag = checkTag;
    }

    protected abstract void init(Context context, AttributeSet attrs);

    public void onSelected() {
        isChecked = true;
        selected();
    }

    public void onCanceled() {
        isChecked = false;
        canceled();
    }

    protected abstract void canceled();

    protected abstract void selected();


    public int getCheckTag() {
        return checkTag;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
