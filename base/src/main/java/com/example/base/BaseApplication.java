package com.example.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;

import com.example.base.network.NetworkManager;
import com.squareup.leakcanary.LeakCanary;

import java.util.List;

/**
 * @author YangZhaoxin.
 * @since 2020/1/10 12:20.
 * email yangzhaoxin@hrsoft.net.
 */

public class BaseApplication extends Application {

    public static Application sApplication;

    public static boolean sDebug;

    public void setDebug(boolean isDebug) {
        sDebug = isDebug;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        // 初始化网络检测器
        NetworkManager.getInstance().init(this);
        //初始化内存泄漏监测器
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    /**
     * 获取进程名
     * @param context
     * @return
     */
    public static String getCurrentProcessName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = null;
        if (activityManager != null) {
            runningAppProcessInfos = activityManager.getRunningAppProcesses();
        }
        if (runningAppProcessInfos == null) {
            return null;
        }

        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfos) {
            if (processInfo.pid == Process.myPid()) {
                if (processInfo.processName != null) {
                    return processInfo.processName;
                }
            }
        }
        return null;
    }
}
