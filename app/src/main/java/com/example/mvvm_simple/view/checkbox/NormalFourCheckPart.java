package com.example.mvvm_simple.view.checkbox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.mvvm_simple.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import androidx.arch.core.util.Function;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/**
 * Time:2020/3/6 10:58
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class NormalFourCheckPart extends RelativeLayout implements OnChecked{

    private CheckItem checkItem1;
    private CheckItem checkItem2;
    private CheckItem checkItem3;
    private CheckItem checkItem4;
    private MutableLiveData<CheckStateWrapper> transCheck1 = new MutableLiveData<>();
    private MutableLiveData<CheckStateWrapper> transCheck2 = new MutableLiveData<>();
    private MutableLiveData<CheckStateWrapper> transCheck3 = new MutableLiveData<>();
    private MutableLiveData<CheckStateWrapper> transCheck4 = new MutableLiveData<>();
    List<CheckItem> checkItems = new ArrayList<>();
    private int lastCheckedStack = 0;

    public NormalFourCheckPart(Context context) {
        super(context);
        init();
    }

    public NormalFourCheckPart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NormalFourCheckPart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_answer_questions, this);
        checkItem1 = view.findViewById(R.id.check_1);
        checkItem2 = view.findViewById(R.id.check_2);
        checkItem3 = view.findViewById(R.id.check_3);
        checkItem4 = view.findViewById(R.id.check_4);

        checkItems.add(checkItem1);
        checkItems.add(checkItem2);
        checkItems.add(checkItem3);
        checkItems.add(checkItem4);

        checkItem1.setPosition(0);
        checkItem2.setPosition(1);
        checkItem3.setPosition(2);
        checkItem4.setPosition(3);

        for (CheckItem i :
                checkItems) {
            i.setOnCheckedHost(this);
        }



        transCheck1 = (MutableLiveData<CheckStateWrapper>) Transformations.map(checkItem1.getIsChecked(),
                input -> new CheckStateWrapper(0, input));
        transCheck2 = (MutableLiveData<CheckStateWrapper>) Transformations.map(checkItem2.getIsChecked(),
                input -> new CheckStateWrapper(0, input));
        transCheck3 = (MutableLiveData<CheckStateWrapper>) Transformations.map(checkItem3.getIsChecked(),
                input -> new CheckStateWrapper(0, input));
        transCheck4 = (MutableLiveData<CheckStateWrapper>) Transformations.map(checkItem4.getIsChecked(),
                input -> new CheckStateWrapper(0, input));
    }

    @Override
    public void onChecked(int position) {
        for (int i = 0; i < 4; i++) {
            if (position != i) {
                checkItems.get(i).disableCheckBox();
            }
        }
    }

    class CheckStateWrapper {

        private int position;
        private boolean isChecked;

        public CheckStateWrapper(int position, boolean isChecked) {
            this.position = position;
            this.isChecked = isChecked;
        }
    }


}
