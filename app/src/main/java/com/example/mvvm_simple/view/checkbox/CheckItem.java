package com.example.mvvm_simple.view.checkbox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mvvm_simple.R;

import androidx.lifecycle.MutableLiveData;

/**
 * Time:2020/3/6 10:51
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class CheckItem extends RelativeLayout {

    private TextView mText;
    private CheckBox mCheckBox;
    private boolean isChecked = false;
    private OnChecked onCheckedHost;
    private int position;
    private MutableLiveData<Boolean> mIsChecked = new MutableLiveData<>();

    public CheckItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CheckItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_check_box, this);
        mText = view.findViewById(R.id.check_item_check_txt);
        mCheckBox = view.findViewById(R.id.check_item_checkbox);
        initListener();
    }

    private void initListener() {
       mCheckBox.setOnCheckedChangeListener((buttonView, is) -> {
           isChecked = is;
           mIsChecked.setValue(is);
           if (isChecked) {
               onCheckedHost.onChecked(position);
           }
       });
    }

    public void setOnCheckedHost(OnChecked onCheckedHost) {
        this.onCheckedHost = onCheckedHost;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setText(String text) {
        mText.setText(text);
    }

    public MutableLiveData<Boolean> getIsChecked() {
        return mIsChecked;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void disableCheckBox() {
        mCheckBox.setChecked(false);
    }
}
