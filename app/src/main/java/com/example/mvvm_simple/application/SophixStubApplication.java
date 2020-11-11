package com.example.mvvm_simple.application;

import android.content.Context;
import android.util.Log;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

import androidx.annotation.Keep;

/**
 * @author DuLong
 * @since 2020/8/4
 * sophixSdk稳健接入
 * email 798382030@qq.com
 */
public class SophixStubApplication extends SophixApplication {
    private final String TAG = "SophixStubApplication";
    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    @Keep
    @SophixEntry(App.class)
    static class RealApplicationStub {}
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//         如果需要使用MultiDex，需要在此处调用。
//         MultiDex.install(this);
        initSophix();
    }
    private void initSophix() {
        //拉取对应控制台版本号创建的补丁
        String appVersion = "1.0.0";
        try {
            appVersion = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
        }
        final SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setAppVersion(appVersion)
                .setSecretMetaData(null, null, null)    //对应AndroidManifest中的三个，如果设置了会覆盖
                .setEnableDebug(true)   //线下测试设为true,不会对签名校监，正式发布设置为false,会对签名进行校监
                .setEnableFullLog()
                .setAesKey("abcdefghijklmnop")     //设置aes密钥，对补丁包进行对称加密。必须是16位数字或者字母的组合，与补丁工具中设置的一致。
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    //设置patch状态加载监听器

                    /**
                     *
                     * @param mode  无实际意义, 为了兼容老版本, 默认始终为0
                     * @param code  补丁加载状态码, 详情查看PatchStatus类说明
                     * @param info   补丁加载详细说明
                     * @param handlePatchVersion    当前处理的补丁版本号, 0:无 -1:本地补丁 其它:后台补丁
                     */
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            Log.i(TAG, "sophix load patch success!");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 如果需要在后台重启，建议此处用SharePreference保存状态。
                            Log.i(TAG, "sophix preload patch success. restart app to make effect.");
                            //在合适的时机杀死进程
                            instance.killProcessSafely();
                        }
                    }
                }).initialize();
    }
}
