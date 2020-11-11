package com.example.lib_common.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Time:2020/2/24 15:02
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class EditCheckUtil {

    public interface OnTextChange {
        void before(CharSequence s, int start, int count, int after);
        void onChange(CharSequence s, int start, int before, int count);
        void after(Editable s);
    }

    public static void checkNull(EditText editText, final MutableLiveData<Boolean> isContentNull, final OnTextChange change) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!(change == null)) {
                    change.before(s, start, count, after);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(change == null)) {
                    change.onChange(s, start, before, count);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                isContentNull.setValue(s.toString().length() == 0);
                if (!(change == null)) {
                    change.after(s);
                }
            }
        });
    }

    public static void checkNull(EditText editText, final MutableLiveData<Boolean> isContentNull) {
        checkNull(editText, isContentNull, null);
    }

    public static boolean isNumberEnough(EditText editText, int number) {
        return editText.getText().toString().length() == number;
    }

    /**
     * 用于在每次行为之前（比如点击登陆按钮之前）检测所有的EditText的内容是否为空
     * @param editTexts
     * @return
     */
    public static boolean isContentSame(EditText...editTexts) {
        String temp = "";
        int start = 0;
        boolean isSame;
        for (EditText edt :
                editTexts) {
            if (start == 0) {
                temp = edt.getText().toString().trim();
            }
            isSame = temp.equals(edt.getText().toString().trim());
            if (!isSame) {
                return false;
            }
            start++;
        }
        return true;
    }

    /**
     * 本来想要动态监测每个EditText是否为空的情况，但是能力有限
     * 先弃坑了
     */
//    public static LiveData<Boolean> isEdtsAllComplete(EditText...editTexts) {
//        MutableLiveData<Boolean> isEdtListAllNull = new MutableLiveData<>();
//        isEdtListAllNull.setValue(false);
//        for (EditText editText:
//             editTexts) {
//
//        }
//    }

}
