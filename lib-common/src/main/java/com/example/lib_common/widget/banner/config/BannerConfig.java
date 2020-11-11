package com.example.lib_common.widget.banner.config;


import com.example.lib_common.R;
import com.example.lib_common.widget.banner.util.BannerUtils;

import androidx.annotation.DrawableRes;

/**
 * banner的一些默认属性
 * 由于自定义的banner中
 */
public class BannerConfig {
    //是否自动跳转
    public static final boolean IS_AUTO_LOOP = true;
    //跳转的间隔时间
    public static final int DELAY_TIME = 3000;
    public static final int DURATION = 800;
    //指示器的颜色
    public static final int INDICATOR_NORMAL_COLOR = 0x88D8D8D8;
    public static final int INDICATOR_SELECTED_COLOR = 0x88D8D8D8;
    //指示器的大小
    public static final float INDICATOR_NORMAL_WIDTH = BannerUtils.dp2px(8);
    public static final float INDICATOR_SELECTED_WIDTH = BannerUtils.dp2px(12);
    //指示器的间隔
    public static final float INDICATOR_SPACE = BannerUtils.dp2px(6);
    public static final int INDICATOR_MARGIN = (int) BannerUtils.dp2px(5);
    //SlideIndicator中滑块的样式
    @DrawableRes
    public static final int INDICATOR_TYPE = R.drawable.ic_rectangle;

}
