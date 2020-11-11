package com.example.lib_neuqer_chat_ui.widegt;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.example.lib_neuqer_chat.R;

import java.lang.ref.WeakReference;
import java.util.logging.LogRecord;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Time:2020/3/21 15:46
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: 能随ViewPager变化的小点点
 */
public class IndicatorView extends LinearLayout {

    private static final int INDICATOR_HANDLER_FLAG = 0x12;

    private IndicatorHandler indicatorHandler;
    private int indicatorColorNormal = Color.rgb(0, 0, 0);
    private int indicatorColorSelected = Color.rgb(0, 0, 0);
    private int indicatorWidth = 0;
    private int gravity = 0;

    private int indicatorCount = 0;
    private int currentPosition = 0;

    private Paint indicatorPaint;
    private RectF rectF;

    /**
     * 注意使用Handler推荐的方式，静态内部类
     */
    private static final class IndicatorHandler extends Handler {
        /**
         * 弱引用防止内存泄漏
         */
        private WeakReference<IndicatorView> indicatorView;

        IndicatorHandler(IndicatorView view) {
            indicatorView = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
          if (msg.what == INDICATOR_HANDLER_FLAG) {
              IndicatorView iv = indicatorView.get();
              if (iv != null) {
                  iv.invalidate();
              }
          }
        }
    }

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        indicatorHandler = new IndicatorHandler(this);
        initView(attrs);
    }

    private void initView(AttributeSet attributeSet) {
        indicatorPaint = new Paint();
        rectF = new RectF();
        if (!(attributeSet == null)) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.IndicatorView);
            indicatorColorNormal = typedArray.getColor(R.styleable.IndicatorView_indicatorColor, Color.rgb(0,0,0));
            indicatorColorSelected = typedArray.getColor(R.styleable.IndicatorView_indicatorWidth, Color.rgb(0,0,0));
            indicatorWidth = dip2Px(typedArray.getInt(R.styleable.IndicatorView_indicatorWidth, 0));
            gravity = typedArray.getInt(R.styleable.IndicatorView_gravity, 0);
            typedArray.recycle();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {

        int viewWidth = getWidth();
        int viewHeight = getHeight();
        int totalWidth = indicatorWidth * (2 * indicatorCount - 1);
        indicatorPaint.setAntiAlias(true);
        if (indicatorCount > 0) {
            for (int i = 0; i < indicatorCount; i++) {
                if (i == currentPosition) {
                    indicatorPaint.setColor(indicatorColorSelected);
                } else {
                    indicatorPaint.setColor(indicatorColorNormal);
                }
                // inW == indicatorWidth
                // |<inW>|
                //    O
                //
                //      |2*inW|
                // _____________________________
                // ||   |  O  |  O  |  O  |   ||
                // —————————————————————————————
                //      |<-  totalWidth ->|
                // |<-        viewWidth      ->|
                // |left|
                int left = (viewWidth - totalWidth) / 2 + (i * 2 * indicatorWidth);

                switch (gravity) {
                    //中间
                    case 0:
                        left = (viewWidth - totalWidth) / 2 + (i * 2 * indicatorWidth);
                        break;
                    //靠左
                    case 1:
                        left = i * 2 * indicatorWidth;
                        break;
                    //靠右
                    case 2:
                        left = viewWidth  - totalWidth + i * 2 * indicatorWidth;
                        break;
                }

                int top = (viewHeight - indicatorWidth) / 2;
                int right = left + indicatorWidth;
                int bottom = top + indicatorWidth;
                rectF.set(left, top, right, bottom);
                canvas.drawOval(rectF, indicatorPaint);
            }
        }
    }

    public void setIndicatorCount(int count) {
        this.indicatorCount = count;
    }

    public void setPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        indicatorHandler.sendEmptyMessage(INDICATOR_HANDLER_FLAG);
    }

    public int dip2Px(int dip) {
        float density = getContext().getApplicationContext().getResources().getDisplayMetrics().density;
        return (int)(dip * density + 0.5f);
    }
}
