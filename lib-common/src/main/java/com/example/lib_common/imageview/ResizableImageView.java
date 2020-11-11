package com.example.lib_common.imageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.lib_common.R;
import com.example.lib_common.util.DensityUtil;

/**
 * function:能够适配图片的高度，完美解决图片有时候宽度铺满但是高度占据了
 * 整个屏幕的问题
 *
 * 2020/3/29 添加MaxWidth属性，原因是在聊天功能中设置图片的最大宽度失败
 */
@SuppressLint("AppCompatCustomView")
public class ResizableImageView extends ImageView {

    private float maxWidth;
    private int maxPxWidth;

    public ResizableImageView(Context context) {
        super(context);
    }
 
    public ResizableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ResizableImageView);
        maxWidth = typedArray.getDimension(R.styleable.ResizableImageView_max_resize_width, 0f);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        Drawable d = getDrawable();
        if(d!=null) {
            // ceil not round - avoid thin vertical gaps along the left/right edges
            int width = MeasureSpec.getSize(widthMeasureSpec);
            //高度根据使得图片的宽度充满屏幕计算而得
            int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
            setMeasuredDimension(width > maxWidth && maxWidth != 0 ? (int) maxWidth : width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setMaxWidth(float maxWidth) {
        this.maxWidth = maxWidth;
        maxPxWidth = DensityUtil.dip2px(maxWidth, getContext());
        invalidate();
    }
 
}