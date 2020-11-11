package com.example.lib_common.widget.banner.indicator;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.example.lib_common.widget.banner.config.IndicatorConfig;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



public class BaseIndicator extends View implements Indicator {
    protected IndicatorConfig config;
    protected Paint mPaint;

    public BaseIndicator(Context context) {
        this(context, null);
    }

    public BaseIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        config = new IndicatorConfig();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(config.getNormalColor());
    }


    /**
     * 更新view的参数
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
                layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
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
     * 设置当页面变换时indicator的变化
     * @param count
     * @param currentPosition
     */
    @Override
    public void onPageChanged(int count, int currentPosition) {
        config.setIndicatorSize(count);
        config.setCurrentPosition(currentPosition);
        requestLayout();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        config.setCurrentPosition(position);
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
