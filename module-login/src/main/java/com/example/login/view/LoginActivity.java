package com.example.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.arouter.ARouter;
import com.example.arouter.Constants;
import com.example.base.model.bean.BaseNetworkStatus;
import com.example.base.network.NetType;
import com.example.base.utils.ToastUtil;
import com.example.base.view.activity.MvvmNetworkActivity;
import com.example.fw_annotations.BindPath;
import com.example.lib_common.dialog.ListDialog;
import com.example.lib_common.util.ChangeEditContentUtil;
import com.example.lib_common.statusbar.StatusBarUtil;
import com.example.lib_common.textview.CountDownTv;
import com.example.lib_common.util.DensityUtil;
import com.example.lib_data.AppCurrentUser;
import com.example.lib_data.AppDataBase;
import com.example.lib_data.bean.LoginResponseBean;
import com.example.login.databinding.LoginActivityLoginBinding;
import com.example.login.model.VerifyModel;
import com.example.login.new_part.QuestionActivity;
import com.example.login.view.fragment.LoginChangePwdFragment;
import com.example.login.view.fragment.LoginNewUserFragment;
import com.example.login.viewmodel.LoginViewModel;
import com.example.login.R;
import com.example.login.model.LoginModel;
import java.util.Stack;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import static com.example.login.view.fragment.LoginFindPwdFragment.FIND_PASSWORD_TAG;

@BindPath(Constants.ACTIVITY_APP_LOGIN)
/**
 * 切换Fragment的时候记得以FrameLayout作为根布局
 */
public class LoginActivity extends MvvmNetworkActivity<LoginActivityLoginBinding, LoginViewModel> {

    private static final int RETRY_PWD_LOGIN_TIMES = 3;

    private MutableLiveData<LoginViewModel.LoginType> mType;
    private MutableLiveData<Boolean> isCountDownContinue;
    //观察手机号是否填写完整
    private MutableLiveData<Boolean> isPhoneComplete;
    //观察验证码或者密码是否填写
    private MutableLiveData<Boolean> isCodeComplete;
    //同时观察两个LiveData
    private MediatorLiveData<Boolean> isLoginBtnClickable;

    private LoginViewModel.LoginType mTypeWhenLogin;

    private MutableLiveData<Boolean> isPasswordVisible;

    private MutableLiveData<Boolean> isNewUser;

    private LoginNewUserFragment newUserFragment;

    private ListDialog mDialog;

    private int passwordLoginFailTime = 0;

    private Stack<Fragment> fragmentStack = new Stack<Fragment>();



    @Override
    public int getLayoutId() {
        return R.layout.login_activity_login;
    }

    @Override
    public Class<? extends ViewModel> getViewModel() {
        return LoginViewModel.class;
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

        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.login_colorPrimary));
        isCountDownContinue = new MutableLiveData<>();
        isPhoneComplete = new MutableLiveData<>();
        isLoginBtnClickable = new MediatorLiveData<>();
        isCodeComplete = new MutableLiveData<>();
        isPasswordVisible = new MutableLiveData<>();
        isPasswordVisible.setValue(false);
        isCountDownContinue.setValue(false);
        isPhoneComplete.setValue(false);
        isLoginBtnClickable.setValue(false);
        isCodeComplete.setValue(false);

        isNewUser = new MutableLiveData<>(false);

        //mType是ViewModel中的引用
        mType = mViewModel.getLoginType();
        addListener();
        observeTypeChange();
        mViewDataBinding.loginImgLoginQq.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuestionActivity.class);
            startActivity(intent);
        });
    }

    private void addListener() {

        addListenerToEdit();

        addListenerToChangeType();

        addListenerToEdtPhone();

        addListenerToEdtGetVerify();

        addListenerToCutDown();

        addListenerToBtnGetVerify();

        addListenerToBtnLogin();

        addListenerToEye();

        addListenerToToolBar();

        isLoginBtnClickable.observe(this, this::setLoginBtnEnable);

        observeChangeOfLoginData();

        mViewDataBinding.loginTxtProblem.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, LoginProblemsActivity.class));
        });

    }

    /**
     * 对toolbar的返回键进行监听
     */
    private void addListenerToToolBar() {
        mViewDataBinding.loginMainToolbar.setNavigationOnClickListener(v -> {
            if (isNewUser.getValue()) {
                hideNewUser();
            } else {
                //TODO:打开主页面
                finish();
            }
        });
    }

    /**
     * 拦截返回键的点击，使得其能够作用到fragment上
     * @return
     */

    @Override
    public void onBackPressed() {
       if (isNewUser.getValue()) {
           hideNewUser();
       } else {
           //TODO:打开近享界面
           finish();
       }
    }



    private void addListenerToEye() {
        mViewDataBinding.loginEyePart.setOnClickListener(v -> {
            isPasswordVisible.setValue(!isPasswordVisible.getValue());
        });

        isPasswordVisible.observe(this, aBoolean -> {

            if (mType.getValue() == LoginViewModel.LoginType.PASSWORD) {

                mViewDataBinding.loginImgVisibleEye.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                mViewDataBinding.loginImgVisibleBlindEye.setVisibility(aBoolean ? View.GONE : View.VISIBLE);

                if (aBoolean) {
                    ChangeEditContentUtil.clearEditTransformation(mViewDataBinding.loginEdtVerifyCode);
                } else {
                    ChangeEditContentUtil.changContent(mViewDataBinding.loginEdtVerifyCode, '*');
                }
            }

        });
    }

    /**
     * 监听登陆按钮的点击事件
     */
    private void addListenerToBtnLogin() {
        //监听登陆按钮的点击事件
        mViewDataBinding.loginBtnEnterMain.setOnClickListener(v -> {

            //记录登陆时的方式，防止一些人登陆后直接切换方式，以至于结果返回后难以判断
            mTypeWhenLogin = mType.getValue();
            /**
             * 这一行会影响密码登陆，造成点击登陆按钮后按钮变灰，这个我理解，但是其对
             * 验证码登陆没有影响
             * TODO：查清原因
             */
//            mViewModel.getLoginType().setValue(mType.getValue());
            mViewModel.getCodeOrPassword().setValue(mViewDataBinding.loginEdtVerifyCode.getText().toString().trim());
            mViewModel.getPhone().setValue(mViewDataBinding.loginEdtPhone.getPhoneText());
            /**
             * 每次登陆时获得登陆时的手机号，为了出现问题时使用
             */
            AppCurrentUser.getInstance().setLoginPhone(mViewDataBinding.loginEdtPhone.getPhoneText());
            mViewModel.login();
            /**
             * 每次点击登陆后，登陆按钮设置为不可点击
             */
//            mViewDataBinding.loginBtnEnterMain.setClickable(false);
//            setNewUserFragment();

        });
    }

    /**
     * 对btnVerify添加监测
     */
    private void addListenerToBtnGetVerify() {
        //对btnVerify添加监测，点击则隐藏btnVerify，显示倒计时
        mViewDataBinding.loginBtnGetVerifyCode.setOnClickListener(v -> {
            AppCurrentUser.getInstance().setLoginPhone(mViewDataBinding.loginEdtPhone.getPhoneText());
            mViewDataBinding.loginBtnGetVerifyCode.setVisibility(View.GONE);
            mViewModel.getPhone().setValue(mViewDataBinding.loginEdtPhone.getPhoneText());
            mViewModel.sendVerifyCode();
            mViewDataBinding.loginTxtCountDown.beginCountDown(60 * 1000, 1000, "s");
        });
    }

    /**
     * 对倒计时textView添加监听
     */
    private void addListenerToCutDown() {
        //对倒计时textView添加监听，倒计时开始的时候显示countDown，隐藏btnVerify
        //倒计时完成，则隐藏countDown，显示btnVerify
        mViewDataBinding.loginTxtCountDown.setOnFinishListener(new CountDownTv.CountDownListener() {
            @Override
            public void onFinish(TextView textView) {
                isCountDownContinue.setValue(false);
                mViewDataBinding.loginTxtCountDown.setVisibility(View.GONE);
                if (mType.getValue() == LoginViewModel.LoginType.SMS) {
                    setLoginBtnGetVerifyEnable(isPhoneComplete.getValue());
                }
            }

            @Override
            public void onTick(Long time) {

            }

            @Override
            public void beforeStart() {
                isCountDownContinue.setValue(true);
                mViewDataBinding.loginTxtCountDown.setVisibility(View.VISIBLE);
                mViewDataBinding.loginBtnGetVerifyCode.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 对输入验证码或者密码的edt添加监听
     */
    private void addListenerToEdtGetVerify() {
        mViewDataBinding.loginEdtVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isCodeComplete.setValue(!(s.toString().length() == 0));
            }
        });
    }

    /**
     * 对手机号输入框添加监听
     */
    private void addListenerToEdtPhone() {
        //对手机号输入框添加监听，如果现在是验证码页面，并且没有在倒计时，如果满11个数字则显示可点击的请求验证码按钮，否则则显示不可点击的
        //如果正在倒计时，则直接显示倒计时
        mViewDataBinding.loginEdtPhone.setOnPhoneEditTextChangeListener((s, isEleven) -> {
            isPhoneComplete.setValue(isEleven);
            if (mType.getValue() == LoginViewModel.LoginType.SMS && !isCountDownContinue.getValue()) {
                setLoginBtnGetVerifyEnable(isEleven);
            }
        });
    }

    /**
     * 对用什么方式登陆的textView的点击事件进行监听
     */
    private void addListenerToChangeType() {
        //对用什么方式登陆的textView的点击事件进行监听
        mViewDataBinding.loginTxtChangeLoginType.setOnClickListener(v -> {
            /**
             * 我叼他妈的我在这里设置清空就没问题，在每次状态变化的时候设置清空就有问题，diao!!!!!!!!!!!!
             */
            mViewDataBinding.loginEdtVerifyCode.setText("");
            if (mType.getValue() == LoginViewModel.LoginType.SMS) {
                mType.setValue(LoginViewModel.LoginType.PASSWORD);
            } else {
                mType.setValue(LoginViewModel.LoginType.SMS);
            }
        });
    }

    /**
     * 动态监听两个输入框的情况，设置登陆按钮是否可点击
     */
    private void addListenerToEdit() {

        //动态监听两个输入框的情况，设置登陆按钮是否可点击
        isLoginBtnClickable.addSource(isPhoneComplete, aBoolean -> {
            isLoginBtnClickable.setValue(aBoolean && isCodeComplete.getValue());
        });
        //动态监听两个输入框的情况，设置登陆按钮是否可点击
        isLoginBtnClickable.addSource(isCodeComplete, aBoolean -> {
            isLoginBtnClickable.setValue(aBoolean && isPhoneComplete.getValue());
        });
    }

    /**
     * 观察登陆后的结果
     */
    private void observeChangeOfLoginData() {
        mViewModel.getLoginLiveData().observe(this, o -> {
            if (mTypeWhenLogin == LoginViewModel.LoginType.SMS) {
                if (((LoginResponseBean)o).isNewUser()) {
                    //TODO:是新用户，开启设置密码的界面
                    setNewUserFragment();
                } else {
                    //TODO:直接打开近享主页
                    ARouter.getInstance().startActivity(this, Constants.ACTIVITY_MAIN);
                }
            } else {
                //TODO: 打开近享主页
                ARouter.getInstance().startActivity(this, Constants.ACTIVITY_MAIN);
            }
        });
    }

    /**
     * 打开新用户界面
     */
    private void setNewUserFragment() {

        isNewUser.setValue(true);
        newUserFragment = new LoginNewUserFragment();

        fragmentStack.push(newUserFragment);

        getSupportFragmentManager().beginTransaction().add(R.id.login_linear, newUserFragment).commit();
    }

    /**
     * 关闭新用户界面
     */
    private void hideNewUser() {
        isNewUser.setValue(false);
        getSupportFragmentManager().beginTransaction().hide(newUserFragment).commit();
    }

    /**
     * 观察页面变化
     */
    private void observeTypeChange() {

        /**
         * 我叼他妈的我在这里设置清空就有问题，在点击状态变化文字的时候清空就可以了？，diao!!!!!!!!!!!!
         */

//        mViewDataBinding.loginEdtVerifyCode.setText("");

        mType.observe(this, loginType -> {

            mViewDataBinding.loginTxtNoteForUser.setVisibility(loginType == LoginViewModel.LoginType.SMS ?
                    View.VISIBLE : View.INVISIBLE);

            mViewDataBinding.loginEdtVerifyCode.setHint(loginType == LoginViewModel.LoginType.SMS ?
                    "输入验证码" : "输入密码");

            mViewDataBinding.loginTxtChangeLoginType.setText(loginType == LoginViewModel.LoginType.SMS ?
                    "密码登陆" : "验证码登陆");

            mViewDataBinding.loginImgVisibleEye.setVisibility(loginType == LoginViewModel.LoginType.SMS ?
                    View.GONE : View.VISIBLE);

            if (loginType == LoginViewModel.LoginType.PASSWORD) {

                isPasswordVisible.setValue(false);
                mViewDataBinding.loginEyePart.setVisibility(View.VISIBLE);

                mViewDataBinding.loginBtnGetVerifyCode.setVisibility(View.GONE);
                mViewDataBinding.loginTxtCountDown.setVisibility(View.GONE);
                ChangeEditContentUtil.changContent(mViewDataBinding.loginEdtVerifyCode, '*');

            } else {

                mViewDataBinding.loginEyePart.setVisibility(View.GONE);

                ChangeEditContentUtil.clearEditTransformation(mViewDataBinding.loginEdtVerifyCode);
               if (isCountDownContinue.getValue()) {
                   onCountDownContinue();
               } else {
                   mViewDataBinding.loginTxtCountDown.setVisibility(View.GONE);
                   if (isPhoneComplete.getValue()) {
                       setLoginBtnGetVerifyEnable(true);
                   } else {
                       setLoginBtnGetVerifyEnable(false);
                   }
               }
            }

        });

    }

    private void setLoginBtnGetVerifyEnable(boolean isEnable) {
        mViewDataBinding.loginBtnGetVerifyCode.setVisibility(View.VISIBLE);
        mViewDataBinding.loginTxtVerifyCode.setVisibility(View.VISIBLE);
        if (isEnable) {
            mViewDataBinding.loginBtnGetVerifyCode.setBackground(getResources().getDrawable(R.drawable.login_bg_login_btn_enable_verify));
            mViewDataBinding.loginTxtVerifyCode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.login_white));
            mViewDataBinding.loginBtnGetVerifyCode.setClickable(true);
        } else {
            mViewDataBinding.loginBtnGetVerifyCode.setBackground(getResources().getDrawable(R.drawable.login_bg_login_btn_verify_normal));
            mViewDataBinding.loginTxtVerifyCode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.login_grey_txt));
            mViewDataBinding.loginBtnGetVerifyCode.setClickable(false);
        }
    }

    private void setLoginBtnEnable(boolean isEnable) {
        mViewDataBinding.loginBtnEnterMain.setBackground(getResources().getDrawable(isEnable ? R.drawable.login_bg_login_btn_enter_main_complete
                : R.drawable.login_bg_login_btn_enter_main_normal));

        mViewDataBinding.loginBtnEnterMain.setClickable(isEnable);
    }

    private void onCountDownContinue() {
        mViewDataBinding.loginBtnGetVerifyCode.setVisibility(View.GONE);
        mViewDataBinding.loginTxtCountDown.setVisibility(View.VISIBLE);
    }

    /**
     * 点击了登陆之后，只有登陆成功或者失败之后，登陆键才可以继续点击
     * @param key
     * @param status
     */
    @Override
    public void onNetDone(String key, BaseNetworkStatus status) {
        switch (key) {
            case VerifyModel.VERIFY_TAG:
                ToastUtil.show(this, "验证码已发送");
                break;
            case LoginModel.tagName:
                mViewDataBinding.loginBtnEnterMain.setClickable(true);
                ToastUtil.show(this, "登陆成功");
                break;
            default:
                break;
        }
    }

    /**
     * 点击了登陆之后，只有登陆成功或者失败之后，登陆键才可以继续点击
     * @param key
     * @param status
     */
    @Override
    public void onNetFailed(String key, BaseNetworkStatus status) {
       if (key.equals(LoginModel.tagName)) {
           ToastUtil.show(this, "登陆失败");
           mViewDataBinding.loginBtnEnterMain.setClickable(true);

           if (mType.getValue() == LoginViewModel.LoginType.PASSWORD) {
               passwordLoginFailTime++;

               if (passwordLoginFailTime == RETRY_PWD_LOGIN_TIMES) {
                   //TODO:展示dialog
                   mDialog = new ListDialog.Builder()
                           .initContext(this)
                           .setTitle("失误？")
                           .setItem("手机验证码登陆（推荐）", ListDialog.ListDialogItemType.NORMAL)
                           .setItem("找回密码", ListDialog.ListDialogItemType.NORMAL)
                           .setItem("取消", ListDialog.ListDialogItemType.CANCEL)
                           .setOnclickItemListener((itemView, wrapper, position) -> {
                               if (wrapper.getType() == ListDialog.ListDialogItemType.CANCEL) {
                                   mDialog.dismiss();
                                   passwordLoginFailTime = 0;
                               } else {
                                   ToastUtil.show(getApplicationContext(), wrapper.getContent());
                               }
                           }).setWindowPx(3000, 0)
                           .build();
                   passwordLoginFailTime = 0;

               }
           }
       }
    }

    /**
     * 点击了登陆之后，只有登陆成功或者失败之后，登陆键才可以继续点击
     * @param key
     * @param status
     */
    @Override
    public void onNoNetwork(String key, BaseNetworkStatus status) {
        if (key.equals(LoginModel.tagName)) {
            ToastUtil.show(this, "没有网络，无法登陆");
            mViewDataBinding.loginBtnEnterMain.setClickable(true);
        }
    }

    @Override

    protected void onNetTypeChange(NetType netType) {
        switch (netType) {
            case NONE:
                Log.e("LoginActivity", "断网了");
                break;
            case WIFI:
                Log.e("LoginActivity", "wifi");
                break;
            case CMWAP:
                Log.e("LoginActivity", "流量");
                break;
            case CMNET:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle findPwdBundle = intent.getBundleExtra(FIND_PASSWORD_TAG);
        if (findPwdBundle != null) {
            String info = findPwdBundle.getString(FIND_PASSWORD_TAG);
            if (info != null) {
                if (info.equals("newUser")) {
                    setNewUserFragment();
                }
            }
        }

        Bundle changePwdBundle = intent.getBundleExtra(LoginChangePwdFragment.CHANG_PWD_TAG);
        if (changePwdBundle != null) {
            String info = changePwdBundle.getString(LoginChangePwdFragment.CHANG_PWD_TYPE_TAG);
            if (info != null) {
                if (info.equals("pwd")) {
                    mType.setValue(LoginViewModel.LoginType.PASSWORD);
                }
            }

            String phone = changePwdBundle.getString(LoginChangePwdFragment.CHANG_PWD_PHONE_TAG);
            if (phone != null) {
                mViewDataBinding.loginEdtPhone.setText(phone);
            }
        }

    }

}
