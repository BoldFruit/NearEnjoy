package com.example.lib_neuqer_chat_ui.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.media.Image;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.lib_neuqer_chat.R;
import com.example.lib_neuqer_chat_ui.emoji.EmojiAdapter;
import com.example.lib_neuqer_chat_ui.emoji.EmojiBean;
import com.example.lib_neuqer_chat_ui.emoji.EmojiDaoUtil;
import com.example.lib_neuqer_chat_ui.emoji.EmojiPagerAdapter;
import com.example.lib_neuqer_chat_ui.widegt.IndicatorView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * Time:2020/3/18 22:11
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function: 自带的UI，旨在简化发送消息的逻辑
 *
 * 2020.3.18
 * 目前的想法是，由用户自定义视图，但是视图中必须包含：
 * ·输入框
 * ·加号
 * ·表情
 * 使用ChatController进行绑定
 *
 */
public class ChatController {

    public static final String TAG = ChatController.class.getSimpleName();
    public static final String SOFT_INPUT_SP_KEY = "soft_input_key";
    public static final int DEFAULT_SOFT_KEY_HEIGHT = 270;
    private static final int DEFAULT_PER_PAGE_EMOJI_COUNT = 21;
    private static final int DEFAULT_PER_PAGE_EMOJI_COLUMN = 7;
    private static final int DEFAULT_TOTAL_EMOJI_COUNT = 59;
    private static final String NAVIGATION = "navigationBarBackground";
    private BasicConfig mBasicConfig;
    private List<EmojiBean> mEmojiBeans;
    private SharedPreferences mSp;
    private Activity mActivity;
    private Context mContext;
    private LinearLayout mContentLayout;//界面内容布局
    private LinearLayout mEmojiLayout;//表情布局
    private LinearLayout mPlusLayout;//加号布局
    private RelativeLayout mBottomLayout;//整体底部布局
    private View mPlusButton;//添加按钮
    private Button mSendButton;//发送按钮
    private ImageView mEmojiButton;//表情按钮
    private EditText mInputEdt;//输入框
    private InputMethodManager mInputManager;
    private boolean mIsFirst;

    public ChatController() {
        mEmojiBeans = new ArrayList<>();
    }

    public static ChatController with(Activity activity, BasicConfig basicConfig) {
        ChatController chatController = new ChatController();
        chatController.mActivity = activity;
        chatController.mInputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        chatController.mSp = activity.getSharedPreferences(SOFT_INPUT_SP_KEY, Context.MODE_PRIVATE);
        chatController.mBasicConfig = basicConfig;
        return chatController;
    }

    /**
     * 当你需要使用sqlite来存储和获得表情数据时
     * 时候这个方法，进行数据的初始化
     * @param application application
     * @param dirName     存储文件夹的名字
     * @param dbName      数据库名
     * @param table       数据库的表名
     * @param columns     数据库的列名组
     * @return this
     */
    public ChatController initEmojiData(Application application, String dirName, String dbName, String table, String[] columns) {
        EmojiDaoUtil.getInstance().init(application, dirName, dbName, table, columns);
        createEmojiView();
        return this;
    }

    /**
     * 使用这个方式，说明无需使用sqlite存储表情数据
     * @return this
     */
    public ChatController initEmojiData() {
        createEmojiView();
        return this;
    }

    public void initView() {
        initEmojiView();
        initPlusView();
        initEditView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initEditView() {
        mInputEdt.requestFocus();
        mInputEdt.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP && mBottomLayout.isShown()) {
                lockContentHeight();
                hideBottomLayout(true);
                mEmojiButton.setImageResource(mBasicConfig.getEmojiBtnRes() == 0
                        ? R.mipmap.ic_chat_default_emoji : mBasicConfig.getEmojiBtnRes());
                mInputEdt.postDelayed(this::unlockContentHeightDelayed, 200L);
            }
            return false;
        });

        mInputEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mInputEdt.getText().toString().trim().length() > 0) {
                    mSendButton.setVisibility(View.VISIBLE);
                    mPlusButton.setVisibility(View.GONE);
                } else {
                    mSendButton.setVisibility(View.GONE);
                    mPlusButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initPlusView() {
        if (mPlusLayout == null || mPlusButton == null) {
            return;
        }

        mPlusButton.setOnClickListener(v -> {
            mInputEdt.clearFocus();
            if (mBottomLayout.isShown()) {
                if (mPlusLayout.isShown()) {
                    hideBottomLayout(true);
                } else {
                    //TODO:添加回调
                    hideEmojiLayout();
                }
            } else {
                if (isSoftInputShown()) {
                    hideEmojiLayout();
                    showBottomLayout();
                } else {
                    hideEmojiLayout();
                    showBottom();
                }
            }
        });
    }

    private void initEmojiView() {

        mEmojiButton.setOnClickListener(v -> {
            mInputEdt.clearFocus();
//            if (!mEmojiLayout.isShown()) {
//               if (!(mPlusLayout == null)) {
//                   if (mPlusLayout.isShown()) {
//                       showEmojiLayout();
//                       return;
//                   }
//               }
//               showEmojiLayout();
//            } else if (mEmojiLayout.isShown() && !mPlusLayout.isShown()){
//                mEmojiLayout.isShown();
//                if (!mPlusLayout.isShown()) {
//                    mEmojiButton.setImageResource(mBasicConfig.getEmojiBtnRes() == 0
//                            ? R.mipmap.ic_chat_default_emoji
//                            : mBasicConfig.getEmojiBtnRes());
//                    if (mBottomLayout.isShown()) {
//                        hideBottomLayout(true);
//                    } else {
//                        if (isSoftInputShown()) {
//                            showBottomLayout();
//                        } else {
//                            showBottom();
//                        }
//                    }
//
//                }
//
//                return;
//            }

            if (isSoftInputShown()) {
                hideSoftInput();
                showBottomLayout();
                showEmojiLayout();
            } else {
                if (mBottomLayout.isShown()) {
                    hideBottomLayout(true);
                } else {
                    showBottom();
                    showEmojiLayout();
                }
            }

        });

    }

    /**
     * 创建表情栏
     */
    private ChatController createEmojiView() {
        ViewPager vpEmoji = mActivity.findViewById(R.id.vp_emoji);
        final IndicatorView indicatorView = mActivity.findViewById(R.id.indicator_emoji);
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        EmojiBean emojiBean = new EmojiBean();
        emojiBean.setEmojiId(0);
        emojiBean.setUnicodeInt(0);
        List<EmojiBean> emojiBeans = getEmojiBeans();
        int deleteKeyPosition = (int) Math.ceil(getEmojiBeans().size() * 1.0 / getPerPageEmojiCount());
        for (int i = 1; i <= deleteKeyPosition; i++) {
            if (i == deleteKeyPosition) {
                emojiBeans.add(emojiBeans.size(), emojiBean);
            } else {
                emojiBeans.add(i * getPerPageEmojiCount() - 1, emojiBean);
            }
        }

        int pageCount = (int) Math.ceil(getEmojiBeans().size() * 1.0 / getPerPageEmojiCount());

        List<View> viewList = new ArrayList<>();
        for (int j = 0; j < pageCount; j++) {
            RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.item_emoji_recycler, vpEmoji, false);
            recyclerView.setLayoutManager(new GridLayoutManager(mActivity, getPerPageEmojiColumn()));
            List<EmojiBean> slice = new ArrayList<>();
            if (j == pageCount - 1) {
                slice = emojiBeans.subList(j * getPerPageEmojiCount(), emojiBeans.size());
            } else {
                slice = emojiBeans.subList(j * getPerPageEmojiCount(), (j + 1) * getPerPageEmojiCount());
            }
            EmojiAdapter emojiAdapter = new EmojiAdapter(R.layout.item_emoji_layout, slice, mBasicConfig.getDeleteRes());
            emojiAdapter.setOnItemClickListener((adapter, view, position) -> {
                EmojiBean bean = (EmojiBean) adapter.getData().get(position);
                if (bean.getId() == 0) {
                    //向EditText发送消息，点击事件，同时Mode为点击删除键
                    mInputEdt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_DEL));
                } else {
                    mInputEdt.append(bean.getUnicodeString());
                }
            });

            recyclerView.setAdapter(emojiAdapter);
            viewList.add(recyclerView);
        }
        EmojiPagerAdapter adapter = new EmojiPagerAdapter(viewList);
        vpEmoji.setAdapter(adapter);
        indicatorView.setIndicatorCount(pageCount);
        indicatorView.setPosition(vpEmoji.getCurrentItem());
        vpEmoji.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                indicatorView.setPosition(position);
            }
        });
        return this;
    }

    public ChatController bindContentLayout(LinearLayout contentLayout) {
        this.mContentLayout = contentLayout;
        return this;
    }

    public ChatController bindBottomLayout(RelativeLayout mBottomLayout) {
        this.mBottomLayout = mBottomLayout;
        return this;
    }

    public ChatController bindEmojiLayout(LinearLayout emojiLayout) {
        this.mEmojiLayout = emojiLayout;
        return this;
    }

    public ChatController bindAddLayout(LinearLayout plusLayout) {
        this.mPlusLayout = plusLayout;
        return this;
    }

    public ChatController bindEditText(EditText editText) {
        this.mInputEdt = editText;
        return this;
    }


    public ChatController bindEmoji(ImageView emojiButton) {
        this.mEmojiButton = emojiButton;
        return this;
    }

    private void hidePlusLayout() {
        //TODO:先不写了
    }

    private void showEmojiLayout() {
        mEmojiLayout.setVisibility(View.VISIBLE);
        mEmojiButton.setImageResource(mBasicConfig.getEmojiBtnRes() == 0 ?
                R.mipmap.ic_keyboard:
                mBasicConfig.getEmojiBtnRes());
    }

    private void hideEmojiLayout() {
        mEmojiLayout.setVisibility(View.GONE);
        mEmojiButton.setImageResource(mBasicConfig.getEmojiBtnRes() == 0 ?
                R.mipmap.ic_chat_default_emoji:
                mBasicConfig.getEmojiBtnRes());
    }

    public ChatController bindPlus(View plusButton) {
        this.mPlusButton = plusButton;
        return this;
    }

    public ChatController bindSendButton(Button sendButton) {
        this.mSendButton = sendButton;
        return this;
    }

    /**
     * 获取软件盘的高度
     *
     * @return
     */
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /*  *
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。*/
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;

        if (isNavigationBarExist(mActivity)) {
            softInputHeight = softInputHeight - getNavigationHeight(mActivity);
        }
        //存一份到本地
        if (softInputHeight > 0) {
            mSp.edit().putInt(SOFT_INPUT_SP_KEY, softInputHeight).apply();
        }
        return softInputHeight;
    }

    public int dip2Px(int dip) {
        float density = mActivity.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    /**
     * 是否显示软件盘
     *
     * @return
     */
    public boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }

    /**
     * 隐藏软件盘
     */
    public void hideSoftInput() {
        mInputManager.hideSoftInputFromWindow(mInputEdt.getWindowToken(), 0);
    }


    /**
     * 展示软键盘
     */
    private void showSoftInput() {
        mInputEdt.requestFocus();
        mInputEdt.post(new Runnable() {
            @Override
            public void run() {
                mInputManager.showSoftInput(mInputEdt, 0);
            }
        });
    }

    /**
     * 锁定内容高度
     */
    private void lockContentHeight() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentLayout.getLayoutParams();
        layoutParams.height = mContentLayout.getHeight();
        layoutParams.weight = 0.0F;
    }


    /**
     * 释放内容高度
     */
    private void unlockContentHeightDelayed() {
        mInputEdt.postDelayed(() -> {
            ((LinearLayout.LayoutParams) mContentLayout.getLayoutParams()).weight = 1.0F;
        }, 200L);
    }

    private void hideBottomLayout(boolean showSoftInput) {
        lockContentHeight();
        hideBottom(showSoftInput);
        unlockContentHeightDelayed();
    }

    private void hideBottom(boolean showSoftInput) {
        mEmojiButton.setImageResource(R.mipmap.ic_chat_default_emoji);
        if (mBottomLayout.isShown()) {
            mBottomLayout.setVisibility(View.GONE);
            if (showSoftInput) {
                showSoftInput();
            }
        }
    }

    private void showBottomLayout() {
        lockContentHeight();
        showBottom();
        unlockContentHeightDelayed();
    }

    private void showBottom() {
        int softHeight = getSupportSoftInputHeight();
        if (softHeight == 0) {
            softHeight = mSp.getInt(SOFT_INPUT_SP_KEY, dip2Px(270));
        }

        hideSoftInput();
        mBottomLayout.getLayoutParams().height = softHeight;
        mBottomLayout.setVisibility(View.VISIBLE);
    }


    /**
     * 是否存在NavigationBar
     * @param activity
     * @return
     */
    public boolean isNavigationBarExist(Activity activity) {
        ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).getContext().getPackageName();
            int vpId;
            if ((vpId = viewGroup.getChildAt(i).getId()) != View.NO_ID &&
            NAVIGATION.equals(activity.getResources().getResourceEntryName(vpId))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获得底部导航栏的高度
     * @param activity
     * @return
     */
    public int getNavigationHeight(Context activity) {
        if (activity == null) {
            return 0;
        }
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        int height = 0;
        if (resourceId > 0) {
            //获取NavigationBar的高度
            height = resources.getDimensionPixelSize(resourceId);
        }
        return height;
    }

    /**
     * 暂时废弃
     * @return
     */
    public boolean isFirst() {
        return mBasicConfig.isFirst();
    }

    /**
     * 获得提供的Unicode表情
     * @return
     */
    public List<EmojiBean> getEmojiBeans() {

        if (mBasicConfig.isEmojiDataBase()) {
            return EmojiDaoUtil.getInstance().getEmojiList();
        } else {

            if (mBasicConfig != null &&
                    mBasicConfig.getEmojiList() != null &&
                    mBasicConfig.getEmojiList().size() != 0) {
                return mBasicConfig.getEmojiList();
            }
        }
        return null;
    }

    /**
     * 获得每页表情的列数
     * @return
     */
    public int getPerPageEmojiColumn() {

        if (mBasicConfig.getPerPageEmojiColumn() != 0) {

            if (mBasicConfig.getPerPageEmojiColumn() > mBasicConfig.getPerPageEmojiCount()) {
                return mBasicConfig.getPerPageEmojiCount();
            }
            return mBasicConfig.getPerPageEmojiColumn();
        } else {
            return DEFAULT_PER_PAGE_EMOJI_COLUMN;
        }
    }

    /**
     * 获得每页表情的个数
     * @return
     */
    public int getPerPageEmojiCount() {
        if (mBasicConfig.getPerPageEmojiCount() != 0) {
            return mBasicConfig.getPerPageEmojiCount();
        } else {
            return DEFAULT_PER_PAGE_EMOJI_COUNT;
        }
    }

    private static final class Builder {

    }

    private class ExtLayoutMap {
        private SparseArray<Integer> extLayoutMap;
    }


}
