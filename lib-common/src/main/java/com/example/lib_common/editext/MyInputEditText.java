package com.example.lib_common.editext;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.example.lib_common.R;

/**
 * Time:2020/2/19 21:17
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
@SuppressLint("AppCompatCustomView")
public class MyInputEditText extends EditText {

    private String passwordType;
    private FocusChangeListener mChangedListener;

    public MyInputEditText(Context context, AttributeSet attrs) throws Exception {
        super(context, attrs);
        initEdit(context, attrs);
    }

    public MyInputEditText(Context context, AttributeSet attrs, int defStyleAttr) throws Exception {
        super(context, attrs, defStyleAttr);

        initEdit(context, attrs);
    }

    public void setChangedListener(FocusChangeListener listener) {
        this.mChangedListener = listener;
    }

    private void initEdit(Context context, AttributeSet attrs) throws Exception {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.my_edit_text);
        if (typedArray != null) {
            passwordType = typedArray.getString(R.styleable.my_edit_text_passwordType);
        }

        if (!"".equals(passwordType) && !(passwordType == null)) {
            if (passwordType.length() > 1) {
                throw new Exception("定义字符长度不能超过1");
            } else {
                setEditPasswordType(passwordType);
            }
        } else {

            if ("".equals(passwordType)) {
                throw new NullPointerException("自定义字符不能为空");
            }

        }
    }

    private void setEditPasswordType(final String passwordType) {

        this.setTransformationMethod(new TransformationMethod() {
            @Override
            public CharSequence getTransformation(CharSequence source, View view) {
                return new PasswordTypeCharSequence(source);
            }

            @Override
            public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {
                if (mChangedListener != null) {
                    mChangedListener.focusChanged(view, sourceText, focused, direction, previouslyFocusedRect);
                }
            }
        });
    }


    private class PasswordTypeCharSequence implements CharSequence {

        private CharSequence mSource;

        public PasswordTypeCharSequence(CharSequence mSource) {
            this.mSource = mSource;
        }

        @Override
        public int length() {
            return mSource.length();
        }

        @Override
        public char charAt(int index) {
            return passwordType.charAt(0);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end);
        }
    }

    interface FocusChangeListener {

        void focusChanged(View view,
                          CharSequence sourceText,
                          boolean focused,
                          int direction,
                          Rect previouslyFocusedRect);
    }


}
