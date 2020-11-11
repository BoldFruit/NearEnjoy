package com.example.module_chat;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import io.netty.bootstrap.Bootstrap;

/**
 * Time:2020/3/25 8:21
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ChatBindingAdapter {

//    @BindingAdapter(value = {"imgUrl", "error", "placeHolder"}, requireAll = false)
//    public static void circleUrl(ImageView view, String url, Drawable error, Drawable placeHolder, Drawable fallback) {
//        RequestOptions options = new RequestOptions()
//                .error(error)
//                .placeholder(placeHolder)
//                .fallback(fallback);
//        Glide.with(view.getContext())
//                .load(url)
//                .apply(options)
//                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                .into(view);
//    }

    @BindingAdapter(value = "isAutoScroll", requireAll = false)
    public static void rvAutoScroll(RecyclerView recyclerView, boolean isAuto) {
        if (isAuto) {
            recyclerView
                    .addOnLayoutChangeListener((v, left,
                                                top, right,
                                                bottom, oldLeft,
                                                oldTop, oldRight,
                                                oldBottom) -> {
                if (bottom < oldBottom) {
                    int itemCount;
                    if ((itemCount = recyclerView.getChildCount()) > 0) {
                        recyclerView.smoothScrollToPosition(itemCount - 1);
                    }
                }
            });
        }
    }

    @BindingAdapter(value = "android:roundImg", requireAll = false)
    public static void roundImg(ImageView imageView, String url) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher_round);
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }

    @BindingAdapter(value = {"android:imgUrl", "android:isRound"}, requireAll = false)
    public static void parseImgUrl(ImageView view, String url, boolean isRound) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher_round);
        RequestBuilder<Drawable> apply = Glide.with(view.getContext())
                .load(url)
                .apply(options);
        if (isRound) {
           apply.apply(RequestOptions.bitmapTransform(new CircleCrop()))
                   .into(view);
        } else {
            apply.into(view);
        }
    }

}
