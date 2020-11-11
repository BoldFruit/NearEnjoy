package com.neuqer.module_share.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore

/**
 * Time:2020/4/27 12:11
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class ConverterImage {

    companion object {
        fun bitmap2Uri(context: Context, bitmap: Bitmap?): Uri? {
           return Uri.parse(MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, null,null))
        }
    }




}