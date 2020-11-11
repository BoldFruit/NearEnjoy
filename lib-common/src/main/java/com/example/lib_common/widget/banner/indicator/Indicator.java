package com.example.lib_common.widget.banner.indicator;

import android.view.View;

import com.example.lib_common.widget.banner.config.IndicatorConfig;
import com.example.lib_common.widget.banner.listener.OnPageChangeListener;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;



import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface Indicator extends OnPageChangeListener {
    @NonNull
    View getIndicatorView();

    IndicatorConfig getIndicatorConfig();

    /**
     * 在初始化Indicator或者重置Indicator时调用
     * @param count
     * @param currentPosition
     */
    void onPageChanged(int count, int currentPosition);

}
