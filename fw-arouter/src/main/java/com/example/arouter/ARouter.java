package com.example.arouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

/**
 * @author YangZhaoxin.
 * @since 2020/1/15 19:00.
 * email yangzhaoxin@hrsoft.net.
 */

public class ARouter {

    // 装载所有Activity的类对象
    private Map<String, Class<? extends Activity>> map;

    private Context context;

    private ARouter() {
        map = new HashMap<>();
    }

    private static class InstanceHolder {
        private static final ARouter INSTANCE = new ARouter();
    }

    public static ARouter getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void putActivity(String key, Class<? extends Activity> clazz) {
        if (key != null && !key.isEmpty() && clazz != null) {
            map.put(key, clazz);
        }
    }

    public void init(Context context) {
        this.context = context;
        List<String> classNames = getClassName(Constants.PACKAGE_NAME);
        for (String className : classNames) {
            try {
                Class<?> aClass = Class.forName(className);
                // 判断该类是否是IRouter的子类
                if (IRouter.class.isAssignableFrom(aClass)) {
                    // 这里用反射的话，会很大程度上影响效率。
                    // 故采用接口的引用指向子类对象
                    IRouter iRouter = (IRouter) aClass.newInstance();
                    iRouter.putActivity();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据报名获取到包下面所有的类
     * @param packageName
     * @return
     */
    private List<String> getClassName(String packageName) {
        List<String> classNames = new ArrayList<>();
        try {
            // 获取到当前应用的信息类
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            // apk的最终存储路径
            String sourceDir = applicationInfo.sourceDir;
            // 获取到apk的目录对象，其中包含了apk中的所有类名
            DexFile dexFile = new DexFile(sourceDir);
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                // 每个目录或者文件的名字（带包名）
                String fileName = entries.nextElement();
                // 判断该包名是否包含所需要的包名
                if (fileName.contains(packageName)) {
                    classNames.add(fileName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classNames;
    }

    public void startActivity(Activity activity, String key) {
        startActivity(activity, key, null);
    }

    /**
     * 不同模块间跳转窗体的共用方法
     * TODO: 若要求启动模式，可在之后需要时封装
     * @param key
     * @param bundle
     */
    public void startActivity(Activity activity, String key, Bundle bundle) {
        Class<? extends Activity> aClass = map.get(key);
        if (aClass != null) {
            Intent intent = new Intent(context, aClass);
            if (bundle != null && key != null && !key.isEmpty()) {
                intent.putExtras(bundle);
            }
            activity.startActivity(intent);
        } else {
            throw new IllegalArgumentException("该类未被加入ARouter容器，经检查该类是否被 BindPath 修饰");
        }
    }

    // TODO: 补充fragment
}
