package com.example.lib_common.widget.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.lib_common.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by zhy on 15/9/10.
 */
public class TagFlowLayout extends FlowLayout
        implements TagAdapter.OnDataChangedListener {

    private TagAdapter mTagAdapter;
    //标签选择的最大个数
    private int mSelectedMax = -1;//-1为不限制数量
    private static final String TAG = "TagFlowLayout";
    //选中的View,集合的特点是单一，无重复
    private Set<Integer> mSelectedView = new HashSet<Integer>();
    //选中和取消选中时调用
    private OnSelectListener mOnSelectListener;
    //点击时的监听
    private OnTagClickListener mOnTagClickListener;
    //长时间点击时的监听
    private OnTagLongClickListener mOnTagLongClickListener;
    //记录下所有的itemView
    private List<View> mAllViews = new ArrayList<>();

    public interface OnSelectListener {
        void onSelected(Set<Integer> selectPosSet);
    }

    public interface OnTagClickListener {
        boolean onTagClick(View view, int position, FlowLayout parent);
    }

    public interface OnTagLongClickListener {
        boolean onTagLongClickListener(View view, int position, FlowLayout parent);
    }

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //读取xml文档的数据
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
        mSelectedMax = ta.getInt(R.styleable.TagFlowLayout_max_select, -1);
        ta.recycle();
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFlowLayout(Context context) {
        this(context, null);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int cCount = getChildCount();
        //适配子控件的GONE属性
        for (int i = 0; i < cCount; i++) {
            TagView tagView = (TagView) getChildAt(i);
            if (tagView.getVisibility() == GONE) {
                continue;
            }
            if (tagView.getTagView().getVisibility() == GONE) {
                tagView.setVisibility(GONE);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }


    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        mOnTagClickListener = onTagClickListener;
    }

    public void setOnTagLongClickListener(OnTagLongClickListener onTagLongClickListener) {
        mOnTagLongClickListener = onTagLongClickListener;
    }

    /**
     * 获得指定的子布局
     * @param position
     * @return
     */
    public View getViewItem(int position) {
        return mAllViews.get(position);
    }

    public void setAdapter(TagAdapter adapter) {
        mTagAdapter = adapter;
        //设置当选中数据变化时的监听
        mTagAdapter.setOnDataChangedListener(this);
        mSelectedView.clear();
        changeAdapter();
    }

    @SuppressWarnings("ResourceType")
    private void changeAdapter() {
        if (mAllViews != null) {
            mAllViews.clear();
        }
        removeAllViews();
        TagAdapter adapter = mTagAdapter;
        TagView tagViewContainer = null;
        //获取设置的初始选中的View
        HashSet preCheckedList = mTagAdapter.getPreCheckedList();
        for (int i = 0; i < adapter.getCount(); i++) {
            //将adapter中的View先加入到tagView中，然后再将tagView加入到TagFlowLayout中
            View tagView = adapter.getView(this, i, adapter.getItem(i));
            tagViewContainer = new TagView(getContext());
            //这会使得tagView的点击状态和父View一致
            tagView.setDuplicateParentStateEnabled(true);
            if (tagView.getLayoutParams() != null) {
                tagViewContainer.setLayoutParams(tagView.getLayoutParams());
            } else {
                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(dip2px(getContext(), 5),
                        dip2px(getContext(), 5),
                        dip2px(getContext(), 5),
                        dip2px(getContext(), 5));
                tagViewContainer.setLayoutParams(lp);
            }
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            tagView.setLayoutParams(lp);
            tagViewContainer.addView(tagView);
            addView(tagViewContainer);
            //查看子控件是否在被选中的名单中
            if (preCheckedList.contains(i)) {
                setChildChecked(i, tagViewContainer);
            }

            if (mTagAdapter.setSelected(i, adapter.getItem(i))) {
                setChildChecked(i, tagViewContainer);
            }
            //使子控件无法点击
            tagView.setClickable(false);
            //防止对象被更改
            final TagView finalTagViewContainer = tagViewContainer;
            final int position = i;
            tagViewContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doSelect(finalTagViewContainer, position);
                    if (mOnTagClickListener != null) {
                        mOnTagClickListener.onTagClick(finalTagViewContainer, position,
                                TagFlowLayout.this);
                    }
                }
            });
            tagViewContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    doSelect(finalTagViewContainer, position);
                    if (mOnTagLongClickListener != null) {
                        mOnTagLongClickListener.onTagLongClickListener(finalTagViewContainer, position,
                                TagFlowLayout.this);
                        return true;
                    }
                    return false;
                }
            });
            mAllViews.add(tagViewContainer);
        }
        mSelectedView.addAll(preCheckedList);
    }

    public void setMaxSelectCount(int count) {
        if (mSelectedView.size() > count) {
            Log.w(TAG, "you has already select more than " + count + " views , so it will be clear .");
            mSelectedView.clear();
        }
        mSelectedMax = count;
    }

    public Set<Integer> getSelectedList() {
        return new HashSet<Integer>(mSelectedView);
    }

    private void setChildChecked(int position, TagView view) {
        view.setChecked(true);
        mTagAdapter.onSelected(position, view.getTagView());
    }

    private void setChildUnChecked(int position, TagView view) {
        view.setChecked(false);
        mTagAdapter.unSelected(position, view.getTagView());
    }

    private void doSelect(TagView child, int position) {
        if (!child.isChecked()) {
            //处理max_select=1的情况
            if (mSelectedMax == 1 && mSelectedView.size() == 1) {
                //取消上一次的选中，将本次选中
                Iterator<Integer> iterator = mSelectedView.iterator();
                Integer preIndex = iterator.next();
                TagView pre = (TagView) getChildAt(preIndex);
                setChildUnChecked(preIndex, pre);
                setChildChecked(position, child);
                //更新集合
                mSelectedView.remove(preIndex);
                mSelectedView.add(position);
            } else {
                //超出过后无法选中
                if (mSelectedMax > 0 && mSelectedView.size() >= mSelectedMax) {
                    return;
                }
                setChildChecked(position, child);
                mSelectedView.add(position);
            }
        } else {
            //选中的控件再次选中将取消选中状态
            setChildUnChecked(position, child);
            mSelectedView.remove(position);
        }
        //选中状态发生改变时调用
        if (mOnSelectListener != null) {
            mOnSelectListener.onSelected(new HashSet<Integer>(mSelectedView));
        }
    }

    public TagAdapter getAdapter() {
        return mTagAdapter;
    }


    private static final String KEY_CHOOSE_POS = "key_choose_pos";
    private static final String KEY_DEFAULT = "key_default";

    /**
     * 设置当意外被销毁时保存控件的状态
     * @return
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        //系统默认保存的东西
        bundle.putParcelable(KEY_DEFAULT, super.onSaveInstanceState());

        StringBuilder selectPos = new StringBuilder();
        if (mSelectedView.size() > 0) {
            for (int key : mSelectedView) {
                selectPos.append(key).append("|");
            }
            selectPos = new StringBuilder(selectPos.substring(0, selectPos.length() - 1));
        }
        //存取选中信息
        bundle.putString(KEY_CHOOSE_POS, selectPos.toString());
        return bundle;
    }

    /**
     * 回复数据时的操作
     *
     *
     *
     * @param state
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            String mSelectPos = bundle.getString(KEY_CHOOSE_POS);
            if (!TextUtils.isEmpty(mSelectPos)) {
                String[] split = mSelectPos.split("\\|");
                for (String pos : split) {
                    int index = Integer.parseInt(pos);
                    mSelectedView.add(index);

                    TagView tagView = (TagView) getChildAt(index);
                    if (tagView != null) {
                        setChildChecked(index, tagView);
                    }
                }

            }
            super.onRestoreInstanceState(bundle.getParcelable(KEY_DEFAULT));
            return;
        }
        super.onRestoreInstanceState(state);
    }


    @Override
    public void onChanged() {
        mSelectedView.clear();
        changeAdapter();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
