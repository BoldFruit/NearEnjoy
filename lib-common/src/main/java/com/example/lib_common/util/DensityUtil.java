package com.example.lib_common.util;


import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * @author DuLong
 * @since 2020/3/18
 * email 798382030@qq.com
 */
public class DensityUtil {

    public static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * density) + 0.5f);
    }

    public static int getScreenWidth(Context mContext) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context mContext) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        return dm.heightPixels;}

        /**
         * dp转px
         * @param dip       dp
         * @param context   上下文
         * @return
         */
        public static int dip2px(float dip, Context context) {
            float density = context.getResources().getDisplayMetrics().density;
            int px = (int) (dip * density + 0.5f);// 4.9->4, 4.1->4, 四舍五入
            return px;
        }

        /**
         * px转dp
         * @param px        px
         * @param context   上下文
         * @return
         */
        public static float px2dip(int px, Context context) {
            float density = context.getResources().getDisplayMetrics().density;
            float dp = px / density;
            return dp;
        }

        public static float getStatusBarHeight(Context mContext) {
            Resources resources = mContext.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
            return resources.getDimensionPixelSize(resourceId);
        }
    }


