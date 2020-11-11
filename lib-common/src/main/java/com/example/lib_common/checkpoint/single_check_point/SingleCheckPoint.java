package com.example.lib_common.checkpoint.single_check_point;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.lib_common.R;
import com.example.lib_common.checkpoint.base_single_check.BaseSingleCheck;
import com.example.lib_common.view.EasyRoundView;

/**
 * Time:2020/5/6 11:30
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class SingleCheckPoint extends BaseSingleCheck {

    //圆圈边缘线
    private EasyRoundView mEdge;
    //背景
    private EasyRoundView mBackground;
    //普通颜色的按钮
    private EasyRoundView mNormalPoint;
    //选中后的按钮
    private View mCheckedView;
    private int edgeColor;
    private int backgroundColor;
    private int pointCheckedColor;
    private int pointNormalColor;
    //附加信息
    private String checkTag = "";
    //题目内容
    private String content = "";
    private final String DEFAULT_EDGE_COLOR = "#c7c7bf";
    private final String DEFAULT_BACKGROUND_COLOR = "#FFFFFF";
    private final String DEFAULT_POINT_CHECKED_COLOR = "#ff80bf";
    private final String DEFAULT_POINT_NORMAL_COLOR = "#FFFFFF";

    private View checkPointView;
    private TextView mTextContent;


    public SingleCheckPoint(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SingleCheckPoint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        checkPointView = LayoutInflater.from(context).inflate(R.layout.layout_select_point, this);
        mEdge = checkPointView.findViewById(R.id.layout_select_point_edge);
        mBackground = checkPointView.findViewById(R.id.layout_select_point_background);
        mNormalPoint = checkPointView.findViewById(R.id.layout_normal_point);
        mTextContent = checkPointView.findViewById(R.id.layout_select_point_txt_content);
        mCheckedView = checkPointView.findViewById(R.id.layout_select_point_view);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SingleCheckPoint);

        edgeColor = array.getColor(
                R.styleable.CheckPoint_cp_edge_color,
                Color.parseColor(DEFAULT_EDGE_COLOR)
        );

        backgroundColor = array.getColor(
                R.styleable.CheckPoint_cp_background_color,
                Color.parseColor(DEFAULT_BACKGROUND_COLOR)
        );

        pointNormalColor = array.getColor(
                R.styleable.CheckPoint_cp_normal_color,
                Color.parseColor(DEFAULT_POINT_NORMAL_COLOR)
        );

        pointCheckedColor = array.getColor(
                R.styleable.CheckPoint_cp_checked_color,
                Color.parseColor(DEFAULT_POINT_CHECKED_COLOR)
        );

        checkTag = array.getString(R.styleable.CheckPoint_cp_tag);
        content = array.getString(R.styleable.CheckPoint_cp_txt);
        array.recycle();
        initViewContent();
    }

    private void initViewContent() {
        if (mTextContent != null) {
            mTextContent.setText(content);
        } else {
            Log.d("CheckPoint", "didn't find TextView");
        }

        mNormalPoint.setBackGroundColor(pointNormalColor);

        checkPointView.setOnClickListener(v -> {
            isChecked = !isChecked;
            checkController.onClickedItem(this, isChecked);
        });
    }


    @Override
    protected void canceled() {
        mCheckedView.setVisibility(GONE);
    }

    @Override
    protected void selected() {
        mCheckedView.setVisibility(VISIBLE);
    }

    //设置问题
    public void setContent(String content) {
        this.content = content;
        mTextContent.setText(content);
    }
}
