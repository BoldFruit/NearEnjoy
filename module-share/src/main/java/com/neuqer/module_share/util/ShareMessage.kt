package com.neuqer.module_share.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import com.example.base.utils.ToastUtil

/**
 * Time:2020/4/27 15:44
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: 综合多方代码，方便用来分享图片和文字，前提是要有读写
 * 内存的权限
 */
class ShareMessage(var context: Context) {

    companion object {
        const val TENCENT_QQ_PACKAGE_NAME = "com.tencent.mobileqq"
        const val TENCENT_WECHAT_PACKGE_NAME = "com.tencent.mm"
        const val TECENT_WECHAT_FRIEND_ACTIVITY = "com.tencent.mm.ui.tools.ShareImgUI"
        const val TECENT_WECHAT_FRIEND_CIRCLE_ACTIVITY = "com.tencent.mm.ui.tools.ShareToTimeLineUI"
        const val TYPE_TXT = "txt/plain"
        const val TYPE_IMG = "image/*"
        const val WECHAT_TYPE_FRIEND = "wechat_friend"
        const val WECHAT_TYPE_CIRCLE = "wechat_circle"
    }

    /**
     * 分享图片到微信
     *
     * @param title String
     * @param extraText String
     * @param target String 朋友圈或者好友
     * @param imageView View 要发送的截图
     */
    fun shareImageToWechat(title: String, extraText: String, target: String, imageView: View) {
        shareImageToWechat(title, extraText, target, viewToBitmap(imageView))
    }

    /**
     * 分享图片到微信
     *
     * @param title String
     * @param extraText String
     * @param target String 朋友圈或者好友
     * @param bitmap Bitmap? 要发送的bitmap
     */
    fun shareImageToWechat(title: String, extraText: String, target: String, bitmap: Bitmap?) {
        bitmap2Uri(bitmap)?.let { shareImageToWechat(title, extraText, target, it) }
    }

    /**
     * 分享图片到微信
     *
     * @param title String
     * @param extraText String
     * @param target String 朋友圈或者好友
     * @param bitmap Bitmap? 要发送的uri
     */
    fun shareImageToWechat(title: String, extraText: String, target: String, uri: Uri) {
        shareImage(packageName = TENCENT_WECHAT_PACKGE_NAME,
                   activityName = if (target == WECHAT_TYPE_CIRCLE) TECENT_WECHAT_FRIEND_CIRCLE_ACTIVITY else TECENT_WECHAT_FRIEND_ACTIVITY,
                   title = title,
                   extraText = extraText,
                   imageUri = uri)
    }


    fun shareImageToQQ(title: String, extraText: String, imageUri: Uri) {
        if (isAvailable(TENCENT_QQ_PACKAGE_NAME)) {
            shareImage(TENCENT_QQ_PACKAGE_NAME, title, extraText, imageUri)
        } else {
            ToastUtil.show(context, "请安装QQ")
        }
    }

    fun shareImageToQQ(title: String, extraText: String, viewBitmap: Bitmap?) {
        bitmap2Uri(viewBitmap)?.let { shareImageToQQ(title, extraText, it) }
    }

    fun shareImageToQQ(title: String, extraText: String, imageView: View) {
        shareImageToQQ(title, extraText, viewToBitmap(imageView))
    }

    /**
     * 判断相对应的APP是否存在
     * @param packageName
     * @return
     */
    fun isAvailable(packageName: String?): Boolean {
        val packageManager = context.packageManager
        val pinfo = packageManager.getInstalledPackages(0)
        for (i in pinfo.indices) {
            if ((pinfo[i] as PackageInfo).packageName
                            .equals(packageName, ignoreCase = true)) return true
        }
        return false
    }


    private fun shareImage(packageName: String, title: String, extraText: String, imageUri: Uri) {
        val intent = Intent()
        prepareIntent(intent, packageName, title, extraText, imageUri)
        context.startActivity(intent)
    }

    private fun shareImage(packageName: String, activityName: String, title: String, extraText: String, imageUri: Uri) {
        val intent = Intent()
        prepareIntent(intent, packageName, title, extraText, imageUri)
        intent.component = ComponentName(packageName, activityName)
        context.startActivity(intent)
    }

    private fun prepareIntent(intent: Intent, packageName: String, title: String, extraText: String, imageUri: Uri) {
        intent.action = Intent.ACTION_SEND
        intent.type = TYPE_IMG
        intent.setPackage(packageName)
        intent.putExtra(Intent.EXTRA_STREAM, imageUri)
        if (title.isNotEmpty()) {
            intent.putExtra(Intent.EXTRA_TITLE, title)
        }
        if (extraText.isNotEmpty()) {
            intent.putExtra(Intent.EXTRA_TEXT, extraText)
        }
    }



    fun bitmap2Uri(bitmap: Bitmap?): Uri? {
        return Uri.parse(MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, null,null))
    }

    private fun viewToBitmap(v: View): Bitmap? {
        val screenshot: Bitmap = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(screenshot)
        v.draw(canvas)
        return screenshot
    }

}