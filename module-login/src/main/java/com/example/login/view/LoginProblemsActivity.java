package com.example.login.view;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fw_annotations.BindPath;
import com.example.lib_common.statusbar.StatusBarUtil;
import com.example.login.R;

/**
 * 1.切换fragment的时候记得使用FrameLayout作为根布局
 */
public class LoginProblemsActivity extends AppCompatActivity implements OnTitleChange{

    private TextView mTitle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this, R.color.login_white));
        setContentView(R.layout.login_activity_problems);
//        mTransaction = getSupportFragmentManager().beginTransaction();
//        dataBinding = DataBindingUtil.setContentView(this, R.layout.login_activity_problems);
//        dataBinding.loginProblemsFindPwd.setOnClickListener(v -> {
//            mFindPasswordFg = (LoginFindPwdFragment) getSupportFragmentManager().findFragmentByTag(LoginFindPwdFragment.FIND_PASSWORD_TAG);
//            if (mFindPasswordFg == null) {
//                mFindPasswordFg = new LoginFindPwdFragment();
//                mTransaction.add(R.id.login_problems_container, mFindPasswordFg, LoginFindPwdFragment.FIND_PASSWORD_TAG);
//                mTransaction.commit();
//            }
//        });

        mTitle = findViewById(R.id.login_fragment_problems_title);
        toolbar = findViewById(R.id.login_problems_toolbar);

        toolbar.setNavigationOnClickListener(v -> {
            if (!Navigation.findNavController(this, R.id.login_problems_container).navigateUp()) {
                finish();
            }
        });



    }

    /**
     * 拦截返回键的点击，使得其能够作用到fragment上
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.login_problems_container).navigateUp();
    }

    @Override
    public void changeTitle(String title) {
        mTitle.setText(title);
    }

}
