package com.example.lib_common.widget.banner.indicator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.lib_common.widget.banner.config.IndicatorConfig;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


/**
 * @author DuLong
 * @since 2020/2/14
 * email 798382030@qq.com
 */

public class SlideIndicator extends RelativeLayout implements Indicator {
    private float mNormalRadius;
    protected IndicatorConfig config;
    protected Paint mPaint;

    //滑块的长度
    private float mSelectedWidth;
    //动画
    private ObjectAnimator mMoveAnim;
    private ObjectAnimator mScale;
    private AnimatorSet mSet;
    //上一次的索引，用于判断滑块移动的方向和距离
    private int mLastPosition;
    //滑块
    private ImageView mRectangle;

    public SlideIndicator(Context context) {
        this(context, null);
    }

    public SlideIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public SlideIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        config = new IndicatorConfig();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(config.getNormalColor());
        mNormalRadius = config.getNormalWidth() / 2;
        mSelectedWidth = config.getSelectedWidth();
        this.setBackground(getResources().getDrawable(android.R.color.transparent));
    }

    /**
     * 在这里自定义View
     * @return
     */
    @NonNull
    @Override
    public View getIndicatorView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (config.getGravity()) {
            case IndicatorConfig.Direction.LEFT:
                layoutParams.gravity = Gravity.BOTTOM | Gravity.START;
                break;
            case IndicatorConfig.Direction.CENTER:
                layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
                break;
            case IndicatorConfig.Direction.RIGHT:
                layoutParams.gravity = Gravity.BOTTOM | Gravity.END;
                break;
        }
        layoutParams.leftMargin=config.getMargins().leftMargin;
        layoutParams.rightMargin=config.getMargins().rightMargin;
        layoutParams.topMargin=config.getMargins().topMargin;
        layoutParams.bottomMargin=config.getMargins().bottomMargin;
        setLayoutParams(layoutParams);
        return this;
    }

    @Override
    public IndicatorConfig getIndicatorConfig() {
        return config;
    }

    /**
     * 这个方法只在初始化的时候调用一次
     * @param count
     * @param currentPosition
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onPageChanged(int count, int currentPosition) {
        config.setIndicatorSize(count);
        config.setCurrentPosition(currentPosition);
        requestLayout();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //在这里加入滑块
        mNormalRadius = config.getNormalWidth() / 2;
        mSelectedWidth = config.getSelectedWidth();
        mRectangle = new ImageView(getContext());
        mRectangle.setImageResource(config.getIndicatorType());
        LayoutParams mLayoutParams = new LayoutParams((int) mSelectedWidth,
                (int) (config.getNormalWidth()));
        this.addView(mRectangle, mLayoutParams);

        //初始化动画
        mScale = ObjectAnimator.ofFloat(mRectangle, "ScaleX", 1f, 2f, 1f)
                .setDuration(100);
        //需要较高的版本
        mScale.setAutoCancel(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void move(int curPosition) {
        if (curPosition == mLastPosition) {
            return;
        }
        float curTranslationX = mRectangle.getTranslationX();
        //向右移动
        mMoveAnim = ObjectAnimator.ofFloat(mRectangle, "TranslationX", (curPosition * (config.getIndicatorSpace() + mNormalRadius * 2)));
        mMoveAnim.setDuration(100);
        mMoveAnim.setAutoCancel(false);
        mSet = new AnimatorSet();
        mSet.play(mMoveAnim)
                .with(mScale);
        mSet.start();
    }

    /**
     * 设置Indicator的参数
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPaint.setColor(config.getNormalColor());
        int count = config.getIndicatorSize();
        if (count <= 1) return;
        mNormalRadius = config.getNormalWidth() / 2;
        mSelectedWidth = config.getSelectedWidth();
        //间距*（总数-1） +默认半径*（总数）+ 长条与原点宽度的差值
        int width = (int) ((count - 1) * config.getIndicatorSpace() + 2 * ( mNormalRadius * count) + mSelectedWidth - 2 * mNormalRadius);
        int height = (int) (2 * mNormalRadius);
        setMeasuredDimension(width, height);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 修改基类的方法，防止每次重新画点
     * @param position
     */
    @SuppressLint("NewApi")
    @Override
    public void onPageSelected(int position) {
        config.setCurrentPosition(position);
        move(position);
        mLastPosition = position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(config.getNormalColor());
        int count = config.getIndicatorSize();
        if (count <= 1) return;
        float off = mSelectedWidth / 2;
        //画点
        for (int i = 0; i < count; i++) {
            float x = off + i * (2 * mNormalRadius + config.getIndicatorSpace());
            canvas.drawCircle(x, mNormalRadius, mNormalRadius, mPaint);
        }
    }

}
