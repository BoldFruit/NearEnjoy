package com.example.lib_common.widget.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.example.lib_common.R;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * @author DuLong
 * @since 2020/2/4
 * email 798382030@qq.com
 */

/**
 * 圆角的自定义View,可以配置四个圆角
 */
public class CircleImageView extends AppCompatImageView {

    private float mWith, mHeight;
    private int defaultRadius = 0;
    private int radius;
    private int leftTopRadius;
    private int rightTopRadius;
    private int rightBottomRadius;
    private int leftBottomRadius;

    public CircleImageView(Context context) {
        super(context);
        init(context, null);
    }


    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context mContext, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        //读取配置
        TypedArray marray = mContext.obtainStyledAttributes(attrs, R.styleable.Custom_Round_Image_View);
        radius = marray.getDimensionPixelOffset(R.styleable.Custom_Round_Image_View_radius, defaultRadius);
        leftTopRadius = marray.getDimensionPixelOffset(R.styleable.Custom_Round_Image_View_left_top_radius, defaultRadius);
        rightTopRadius = marray.getDimensionPixelOffset(R.styleable.Custom_Round_Image_View_right_top_radius, defaultRadius);
        leftBottomRadius = marray.getDimensionPixelOffset(R.styleable.Custom_Round_Image_View_left_bottom_radius, defaultRadius);
        rightBottomRadius = marray.getDimensionPixelOffset(R.styleable.Custom_Round_Image_View_right_bottom_radius, defaultRadius);
        if (radius == defaultRadius) {
            radius = 50;
        }
        //四个角都没有设置，就用radius
        if (defaultRadius == leftBottomRadius) {
            leftBottomRadius = radius;
        }
        if (defaultRadius == leftTopRadius) {
            leftTopRadius = radius;
        }
        if (defaultRadius == rightBottomRadius) {
            rightBottomRadius = radius;
        }
        if (defaultRadius == rightTopRadius) {
            rightTopRadius = radius;
        }
        marray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWith = getWidth();
        mHeight = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //做判断，只有图片的宽高大于设置的圆角距离的时候才进行裁剪
        int minWidth = Math.max(leftTopRadius, leftBottomRadius) + Math.max(rightBottomRadius, rightTopRadius);
        int minHeight = Math.min(leftTopRadius, rightTopRadius) + Math.max(leftBottomRadius, rightBottomRadius);
        if (mWith >= minWidth && mHeight >= minHeight) {
            //画四个角
            Path mPath = new Path();
            mPath.moveTo(leftTopRadius, 0);
            mPath.lineTo(mWith - rightTopRadius, 0);
            mPath.quadTo(mWith, 0, mWith, rightTopRadius);

            mPath.lineTo(mWith, mHeight - rightBottomRadius);
            mPath.quadTo(mWith, mHeight, mWith - rightBottomRadius, mHeight);

            mPath.lineTo(leftBottomRadius, mHeight);
            mPath.quadTo(0, mHeight, 0, mHeight - leftBottomRadius);

            mPath.lineTo(0, leftBottomRadius);
            mPath.quadTo(0, 0, leftBottomRadius, 0);

            canvas.clipPath(mPath);
        }
        super.onDraw(canvas);
    }


}
