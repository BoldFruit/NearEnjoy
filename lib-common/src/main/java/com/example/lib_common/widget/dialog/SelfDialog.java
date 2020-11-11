package com.example.lib_common.widget.dialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lib_common.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * 创建自定义的dialog，主要学习其实现原理
 */
public class SelfDialog extends Dialog {

    private Button yes;//确定按钮
    private Button no;//取消按钮
    private TextView titleTv;//消息标题文本
    private TextView messageTv;//消息提示文本
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    //确定文本和取消文本的显示内容
    private String yesStr, noStr;
    private int layoutType;

    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    public static final int NORMAL_DIALOG = 1;
    public static final int NO_TITLE_DIALOG = 2;
    public static final int CONFIRM_DIALOG = 3;
    public static final int LOADING_DIALOG = 4;

    @Retention(RetentionPolicy.RUNTIME)
    @IntDef({NO_TITLE_DIALOG, NORMAL_DIALOG, CONFIRM_DIALOG, LOADING_DIALOG})
    public @interface LayoutType {
    }
    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public SelfDialog setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
        return this;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *  @param str
     * @param onYesOnclickListener
     * @return
     */
    public SelfDialog setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener;
        return this;
    }

    public SelfDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (layoutType) {
            case NO_TITLE_DIALOG:
                setContentView(R.layout.no_title_dialog);
                break;
            case NORMAL_DIALOG:
                setContentView(R.layout.normal_dialog);
                break;
            case CONFIRM_DIALOG:
                setContentView(R.layout.common_confirm_dialog);
                break;
            case LOADING_DIALOG:
                setContentView(R.layout.common_dialog_loading);
                break;
            default:
                setContentView(R.layout.normal_dialog);
                break;
        }

        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();


    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        if (yes != null)
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        if (no != null)
        //设置取消按钮被点击后，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            titleTv.setText(titleStr);
        }
        if (messageStr != null) {
            messageTv.setText(messageStr);
        }
        //如果设置按钮的文字
        if (yesStr != null) {
            yes.setText(yesStr);
        }
        if (noStr != null) {
            no.setText(noStr);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        yes =  findViewById(R.id.yes);
        no = findViewById(R.id.no);
        titleTv =  findViewById(R.id.title);
        messageTv =  findViewById(R.id.message);
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     * @return
     */
    public void setMessage(String message) {
        messageStr = message;
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick();
    }

    /**
     * 设置布局类型
     * @param type 布局类型
     * @return
     */
    public void setLayout(@LayoutType int type) {
        layoutType = type;
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }

}
