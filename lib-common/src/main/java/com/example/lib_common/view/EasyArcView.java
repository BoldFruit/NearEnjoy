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
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.example.lib_common.R;
import com.example.lib_common.bottomnavi.easynavigation.utils.NavigationUtil;

import androidx.annotation.RequiresApi;

/**
 * Time:2019/9/11 22:05
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: to draw arcs
 */
public class EasyArcView extends View {

    private Paint paint;
    private int height;
    private int width;
    private RectF rect = new RectF();
    private Point circleCenter;
    private float radius;
    private float radius_real;
    private int startColor;
    private int endColor;
    private LinearGradient mLinearGradient;
    private int position;
    private int number_radius;
    public static int POSITION_UP = 0;
    public static int POSITION_DOWN = 1;

    public EasyArcView(Context context) {
        this(context,null);
        startInit();
    }



    public EasyArcView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        startInit();
    }

    public EasyArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EasyArcView);
        height = getHeight();
        width = getWidth();
        number_radius = typedArray.getInteger(R.styleable.EasyArcView_arc_radius,3);
        radius_real = NavigationUtil.dip2px(this.getContext(), typedArray.getDimension(R.styleable.EasyArcView_arc_radius_real, 0));
        position = typedArray.getInteger(R.styleable.EasyArcView_arc_position,POSITION_DOWN);
        startColor = typedArray.getColor(R.styleable.EasyArcView_start_color, Color.parseColor("#FFFFFF"));
        endColor = typedArray.getColor(R.styleable.EasyArcView_end_color,Color.parseColor("#FFFFFF"));
        startInit();
        typedArray.recycle();
    }



    private void startInit() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        circleCenter = new Point();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
        radius = (radius_real == 0 ? width * number_radius : radius_real);
        rect.top = 0;
        rect.bottom = getHeight();
        rect.left = 0;
        rect.right = getWidth();
        int width = getWidth();
        circleCenter.x = width/2;
        if (position == POSITION_UP) {
            circleCenter.y = (int)(height - radius);
        } else {
            circleCenter.y = (int)radius;
        }

        mLinearGradient = new LinearGradient((float)(width / 2), 0, (float)(width / 2),height, startColor, endColor, Shader.TileMode.MIRROR);
    }


    @SuppressLint("DrawAllocation")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int layerId = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawCircle(circleCenter.x, circleCenter.y,radius,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setShader(mLinearGradient);
        canvas.drawRect(rect,paint);
        paint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }
}
