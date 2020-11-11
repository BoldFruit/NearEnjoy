package com.example.lib_common.dialog.bottom_dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.RelativeLayout;

import com.example.lib_common.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2020/4/20 12:03
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class SimpleShareBottomDialog extends BottomDialogBase {
    interface OnCancelListener {
        void onCancel();
    }
    private SimpleAdapter adapter;
    private OnCancelListener listener;
    private SimpleAdapter.SimpleItemClickListener onItemClickListener;
    private ArrayList<Integer> imageList = new ArrayList<>();
    private ArrayList<String> titleList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;

    public SimpleShareBottomDialog(Context context, OnCancelListener listener, SimpleAdapter.SimpleItemClickListener itemClickListener) {
        super(context);
        this.listener = listener;
        this.onItemClickListener = itemClickListener;
    }

    @Override
    protected void onCreate() {
        setContentView(R.layout.common_simple_share_bottom_dialog);
        adapter = new SimpleAdapter();
        //由于构造函数的特殊性，导致listener无法在onCreate之前被初始化，所以只能采取
        //二次监听的方式，当点击事件被监听到时，外部设置的onItemClickListener也已经
        //初始化完毕，就可以被回调了
        adapter.setListener(title -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(title);
            }
        });
        adapter.addImage(R.drawable.common_ic_qq, "QQ")
                .addImage(R.drawable.common_ic_qq_space, "QQ空间")
                .addImage(R.drawable.common_ic_webo, "微博")
                .addImage(R.drawable.common_ic_wechat, "微信")
                .addImage(R.drawable.common_ic_wechat_moments, "朋友圈")
                .addImage(R.drawable.common_ic_talk, "聊天")
                .addImage(R.drawable.common_ic_report, "举报")
                .addImage(R.drawable.common_ic_black_list, "拉黑");
        recyclerView = findViewById(R.id.common_simple_share_bottom_recycler);
        relativeLayout = findViewById(R.id.common_simple_share_bottom_cancel);
        recyclerView.setAdapter(adapter);
        relativeLayout.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancel();
            } else {
                dismiss();
            }
        });
    }
}
