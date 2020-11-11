package com.example.lib_common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import com.example.lib_common.R;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Time:2020/3/2 14:10
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class EasyRoundView extends View {

    private int POSITION = 0;
    private int startColor;
    private int endColor;
    private int backGroundColor;
    private float centerPosition;
    private float radius;
    private Rect mRect;
    private Paint mPaint;
    private Point point;


    public EasyRoundView(Context context) {
        super(context);
        init(context);
    }

    public EasyRoundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EasyRoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mRect = new Rect();
        point = new Point();
        setLayerType(LAYER_TYPE_SOFTWARE, mPaint);
    }

    private void init(Context context, AttributeSet attrs) {
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EasyRoundView);
        point.x = (int) typedArray.getDimension(R.styleable.EasyRoundView_circle_center_x, getWidth() / 2);
        point.y = (int) typedArray.getDimension(R.styleable.EasyRoundView_circle_center_y, getHeight() / 2);
        radius =  typedArray.getDimension(R.styleable.EasyRoundView_circle_radius, getWidth() / 2);
        backGroundColor = typedArray.getColor(R.styleable.EasyRoundView_background_color,
                ContextCompat.getColor(getContext(), R.color.white));
        mPaint.setColor(backGroundColor);
        typedArray.recycle();

    }

    public void setBackGroundColor(int backGroundColor) {
        this.backGroundColor = backGroundColor;
        invalidate();
        forceLayout();
        requestLayout();
    }

    public void setBackGroundColor(String colorName) {
        this.backGroundColor = Color.parseColor(colorName);
        invalidate();
//        forceLayout();
//        requestLayout();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRect.set(0, 0, w, h);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawCircle(point.x, point.y, radius, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(mRect, mPaint);
        mPaint.setShader(new LinearGradient(0, 0, ((float)getWidth()), ((float) getHeight()), backGroundColor, backGroundColor, Shader.TileMode.MIRROR));
        mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }
}
