package com.example.lib_common.checkpoint;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lib_common.R;
import com.example.lib_common.view.EasyRoundView;

/**
 * Time:2020/3/4 8:46
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:自定义的CheckPoint，用来替代RadioButton
 */
public class CheckPoint extends RelativeLayout implements ICheckPoint{

    interface IPointCheckListener {
        void onChecked(int tag, boolean isChecked);
    }

    private IPointCheckListener listener;
    private CheckPointController checkPointController;

    private int pointTag;

    //圆圈边缘线
    private EasyRoundView mEdge;
    //背景
    private EasyRoundView mBackground;
    //普通颜色的按钮
    private EasyRoundView mNormalPoint;
    //选中后的按钮
    private View mCheckedView;
    private boolean isChecked = false;
    private boolean isCancelable = false;
    private int edgeColor;
    private int backgroundColor;
    private int pointCheckedColor;
    private int pointNormalColor;
    private String checkTag = "";
    private String content = "";
    private final String DEFAULT_EDGE_COLOR = "#c7c7bf";
    private final String DEFAULT_BACKGROUND_COLOR = "#FFFFFF";
    private final String DEFAULT_POINT_CHECKED_COLOR = "#ff80bf";
    private final String DEFAULT_POINT_NORMAL_COLOR = "#FFFFFF";

    private View checkPointView;
    private TextView mTextContent;


    public CheckPoint(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public CheckPoint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        checkPointView = LayoutInflater.from(context).inflate(R.layout.layout_select_point, this);
        mEdge = checkPointView.findViewById(R.id.layout_select_point_edge);
        mBackground = checkPointView.findViewById(R.id.layout_select_point_background);
        mNormalPoint = checkPointView.findViewById(R.id.layout_normal_point);
        mTextContent = checkPointView.findViewById(R.id.layout_select_point_txt_content);
        mCheckedView = checkPointView.findViewById(R.id.layout_select_point_view);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CheckPoint);

        isChecked = array.getBoolean(R.styleable.CheckPoint_cp_is_checked, false);
        isCancelable = array.getBoolean(R.styleable.CheckPoint_cp_is_cancelable, false);
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

        initClickListener();

    }

    private void initViewContent() {
        if (mTextContent != null) {
            mTextContent.setText(content);
        } else {
            Log.d("CheckPoint", "didn't find TextView");
        }

        mNormalPoint.setBackGroundColor(pointNormalColor);
    }

    private void initClickListener() {
        checkPointView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = !isChecked;
                checkPointController.onClickPoint(CheckPoint.this, isChecked);
                if (listener != null) {
                    listener.onChecked(pointTag, isChecked);
                }
            }
        });
    }



    @Override
    public void setPointTag(int tag) {
        pointTag = tag;
    }

    @Override
    public void onChecked() {
        isChecked = true;
        mCheckedView.setVisibility(VISIBLE);
    }

    @Override
    public void onCanceled() {
        isChecked = false;
        mCheckedView.setVisibility(GONE);
    }

    public void addListener(IPointCheckListener listener) {
        this.listener = listener;
    }

    public void setController(CheckPointController controller) {
        this.checkPointController = controller;
    }

    public int getPointTag() {
        return pointTag;
    }
}
