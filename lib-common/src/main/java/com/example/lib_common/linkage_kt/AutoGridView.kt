package com.example.lib_common.linkage_kt

import android.content.Context
import android.util.AttributeSet
import android.widget.GridView


/**
 * Time:2020/4/14 11:42
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class AutoGridView constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : GridView(context, attrs, defStyleAttr) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = MeasureSpec.makeMeasureSpec(Int.MAX_VALUE shr 2,
                MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}