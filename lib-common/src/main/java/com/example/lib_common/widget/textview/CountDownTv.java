package com.example.lib_common.widget.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * Time:2020/2/20 14:23
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: 自定义的一个倒计时工具，可自定义倒计时时间，倒计时间隔，倒计时的单位。
 * 同时支持添加监听回调，关闭倒计时等功能
 */
@SuppressLint("AppCompatCustomView")
public class CountDownTv extends TextView {

    public interface CountDownListener {
        void onFinish(TextView textView);

        void onTick(Long time);

        void beforeStart();
    }

    private CountDownListener countDownListener;
    private CountDownTimer countDownTimer;
    private Long totalTime;
    private Long perTime;
    private String unit;
    private StringBuilder builder;

    public CountDownTv(Context context) {
        super(context);
        init();
    }


    public CountDownTv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountDownTv(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        totalTime = (long) (60 * 1000);
        perTime = (long)1000;
        unit = "s";
        builder = new StringBuilder();
    }

    public void beginCountDown(long totalTime, long perTime, final String unit) {

        this.totalTime = totalTime;
        this.perTime = perTime;
        this.unit = unit;

        countDownListener.beforeStart();

        countDownTimer = new CountDownTimer(totalTime, perTime) {
            @Override
            public void onTick(long millisUntilFinished) {
                builder.delete(0, builder.length());
                String value = String.valueOf((int)millisUntilFinished / 1000);
                builder.append(value);
                builder.append(unit);
                CountDownTv.this.setText(builder.toString());
                countDownListener.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                if (countDownListener != null) {
                    countDownListener.onFinish(CountDownTv.this);
                }
            }
        }.start();
    }

    public void stopCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            this.setText("");
        } else {
            throw new NullPointerException("CountDownTimer shouldn't be null");
        }

    }

    public CountDownTv setOnFinishListener(CountDownListener listener) {
        this.countDownListener = listener;
        return this;
    }

    public void retry() {
        if (countDownTimer != null) {
            countDownTimer.start();
        } else {
            beginCountDown(totalTime, perTime, unit);
        }
    }

}
