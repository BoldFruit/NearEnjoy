package com.example.lib_common.util;

import android.graphics.Rect;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.EditText;

/**
 * Time:2020/2/20 15:15
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: 自定义的一个工具类，主要用于密码输入，自定义密码样式，使用简单。
 */
public class ChangeEditContentUtil {

    public static void changContent(EditText editText, final char replaceChar) {

        editText.setTransformationMethod(new TransformationMethod() {
            @Override
            public CharSequence getTransformation(CharSequence source, View view) {
                return new TypeCharSequence(source, replaceChar);
            }

            @Override
            public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {

            }
        });

        editText.setSelection(editText.getText().toString().length());

    }

    public static void clearEditTransformation(EditText text) {
        text.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        //每次切换后要将光标放置到末尾
        text.setSelection(text.getText().toString().length());
    }

    private static class TypeCharSequence implements CharSequence {

        private CharSequence mSource;
        private char mReplaceSequence;

        public TypeCharSequence(CharSequence mSource, char charSequence) {
            this.mSource = mSource;
            this.mReplaceSequence = charSequence;
        }

        @Override
        public int length() {
            return mSource.length();
        }

        @Override
        public char charAt(int index) {
            return mReplaceSequence;
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end);
        }
    }

}
