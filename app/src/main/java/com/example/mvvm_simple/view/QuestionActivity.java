package com.example.mvvm_simple.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import android.os.Bundle;
import android.view.View;

import com.example.base.view.activity.MvvmNetworkActivity;
import com.example.lib_common.button.RelativeButton;
import com.example.mvvm_simple.R;
import com.example.mvvm_simple.databinding.ActivityQuestionBinding;
import com.example.mvvm_simple.viewmodel.QuestionViewModel;

public class QuestionActivity extends MvvmNetworkActivity<ActivityQuestionBinding, QuestionViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_question;
    }

    @Override
    public Class<? extends ViewModel> getViewModel() {
        return QuestionViewModel.class;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    protected void initParameters() {

    }

    @Override
    protected void initDataAndView() {

        mViewDataBinding.questionBtn.setType(RelativeButton.BtnType.ENABLE);

        mViewDataBinding.questionBtn.setOnClickListener(v -> {
            mViewDataBinding.questionBtn.setType(RelativeButton.BtnType.PROGRESSING);
        });

        mViewDataBinding.appQuestionSelector.setPullListener(new SchoolSelector.OnSelectorPullListener() {
            @Override
            public void onPullDown() {

            }

            @Override
            public void onPullUp(SchoolSelector schoolSelector) {

            }
        });
    }

}
