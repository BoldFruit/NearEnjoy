package com.example.lib_common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import com.example.lib_common.R;
import com.example.lib_common.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2020/2/25 16:07
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ListDialog extends Dialog {

    public enum ListDialogItemType {
        NORMAL,

        CANCEL
    }


    public interface OnClickDialogItem {
        void onClick(View itemView, ListDialogItemWrapper wrapper, int position);
    }

    private int mDIYLayoutItem;
    private RecyclerView mListRecyclerView;
    private TextView mTitle;
    private DialogListAdapter mAdapter;
    private String title;
    private float height;
    private float width;
    private List<ListDialogItemWrapper> mWrapperList = new ArrayList<>();
    private OnClickDialogItem mOnClickListener;
    private Context context;


    public ListDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public ListDialog(Context context,
                      String title,
                      List<ListDialogItemWrapper> wrappers,
                      OnClickDialogItem onClickDialogItem,
                      float height,
                      float width) {
        super(context);
        this.title = title;
        this.mWrapperList = wrappers;
        this.mOnClickListener = onClickDialogItem;
        this.height = height;
        this.width = width;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.common_dialog_layout, null);
        setContentView(view);
        mListRecyclerView = view.findViewById(R.id.common_list_dialog_recycler);
        mTitle = view.findViewById(R.id.common_txt_list_dialog_title);
        mTitle.setText(title);
        mAdapter = new DialogListAdapter();
        mListRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = (int) this.width;
        if (height != 0) {
            params.height = (int)this.height;
        } else {
            params.height = this.getWindow().getAttributes().height;
        }

        this.getWindow().setAttributes(params);
    }

    public ListDialog setItem(String content, ListDialogItemType type) {
        ListDialogItemWrapper wrapper = new ListDialogItemWrapper(content, type);
        mWrapperList.add(wrapper);
        return this;
    }

    public ListDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public ListDialog setOnItemClickListener(OnClickDialogItem listener) {
        this.mOnClickListener = listener;
        return this;
    }



    public void setDIYLayoutItem(int mLayout) {
        this.mDIYLayoutItem = mLayout;
    }


    public static class ListDialogItemWrapper {

        private String content;
        private ListDialogItemType type;

        public ListDialogItemWrapper(String content, ListDialogItemType type) {
            this.content = content;
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public ListDialogItemType getType() {
            return type;
        }
    }

    class DialogListAdapter extends RecyclerView.Adapter<DialogListViewHolder> {

        private List<ListDialogItemWrapper> mWrappers = new ArrayList<>();

        public DialogListAdapter() {

            mWrappers = mWrapperList;

        }

        @NonNull
        @Override
        public DialogListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(mDIYLayoutItem == 0 ?
                            R.layout.common_dialog_list_item : mDIYLayoutItem,
                    parent, false);
            return new DialogListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DialogListViewHolder holder, int position) {
            holder.bind(mWrapperList.get(position), position, mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mWrappers.size();
        }
    }

    public static class DialogListViewHolder extends RecyclerView.ViewHolder {

        private TextView mTxtContent;

        public DialogListViewHolder(@NonNull View itemView) {
            super(itemView);
            mTxtContent = itemView.findViewById(R.id.common_txt_list_dialog);
        }

        public void bind(ListDialogItemWrapper wrapper, int position, OnClickDialogItem onClickDialogItem) {

            mTxtContent.setText(wrapper.content);
            itemView.setOnClickListener(v -> {
                if (onClickDialogItem != null) {
                    onClickDialogItem.onClick(v, wrapper, position);
                }
            });
        }

    }

    public static class Builder {

        private String title = "title";
        private List<ListDialogItemWrapper> mWrapperList;
        private OnClickDialogItem mOnClickListener;
        private float height = 200;
        private float width = 300;
        private Context context;

        public Builder initContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setItem(String content, ListDialogItemType type) {
            if (mWrapperList == null) {
                mWrapperList = new ArrayList<>();
            }
            mWrapperList.add(new ListDialogItemWrapper(content, type));
            return this;
        }

        public Builder setWindowDp(float width, float height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setWindowPx(int width, int height)  {
            if (context == null) {
                Log.d("ListDialog", "未设置context，无法设置宽高");
            } else {
                this.width = DensityUtil.px2dip(width, context);
                this.height = DensityUtil.px2dip(height, context);
            }

            return this;
        }

        public Builder setOnclickItemListener(OnClickDialogItem onclickItemListener) {
            this.mOnClickListener = onclickItemListener;
            return this;
        }

        public ListDialog build() {
            return new ListDialog(context, title, mWrapperList, mOnClickListener, height, width);
        }
    }

}
