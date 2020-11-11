package com.example.base.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @author YangZhaoxin.
 * @since 2020/1/28 11:01.
 * email yangzhaoxin@hrsoft.net.
 */

public class ToastUtil {

    public static void show(Context context, String msg) {
        Toast mToast;
        if (context != null && !TextUtils.isEmpty(msg)) {
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
            mToast.setText(msg);
            mToast.show();
        }
    }
}
