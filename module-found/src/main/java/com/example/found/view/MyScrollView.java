package com.example.found.view;

/**
 * @author DuLong
 * @since 2020/4/3
 * email 798382030@qq.com
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 用于解决滑动冲突的控件
 */
public class MyScrollView extends NestedScrollView {
    private boolean isScrollToBottom = false;

    public MyScrollView(@NonNull Context context) {
        super(context);
    }

    public MyScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 使得当RecyclerView滑动到底部时才开始滑动ScrollView
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isScrollToBottom) {
                    intercepted = true;
                } else {
                    intercepted = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            default:
                break;
        }
        return intercepted;
    }

    /**
     * 用于同步RecyclerView是否滑动到了底部
     * @param mScrollToBottom
     */
    public void setScrollToBottom(boolean mScrollToBottom) {
        isScrollToBottom = mScrollToBottom;
    }
}
