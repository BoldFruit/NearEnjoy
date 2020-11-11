package com.example.lib_neuqer_chat_ui.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * Time:2020/3/20 11:02
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: 根据内容的最大高度进行改变高度
 */
public class WrapContentViewpager extends ViewPager {

    public WrapContentViewpager(@NonNull Context context) {
        super(context);
    }

    public WrapContentViewpager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
//    makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED)中的一个参数为什么是0，什么意思？
//
//    第一个参数本应该是系统测量该View后得到的规格值（MeasureSpec），本来这个measure是由系统测量完宽高后自动调用，
//    我们这里只是做了系统即将要做的事情而已，那么这个参数为什么是0了，既然我们要通过这个方法测量View的宽高，不就是
//    怕系统还没有自动调用这个方法前调用getMeasureWidth/Height方法而没法获得导致取值为0 ，也就是我们默认调用这个
//    方法就是系统没有对该View绘制，就直接调用了measure方法，所以也就是宽高为0咯，其实这makeMeasureSpec的第一个参
//    数设置什么都无所谓啦，因为最后取得值也不是第一个参数设置的值
            /**
             * https://blog.csdn.net/qijingwang/article/details/90713043?depth_1-utm_source=distribute.pc_
             * relevant.none-task&utm_source=distribute.pc_relevant.none-task
             */
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) {
                height = h;
            }
        }
        int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
