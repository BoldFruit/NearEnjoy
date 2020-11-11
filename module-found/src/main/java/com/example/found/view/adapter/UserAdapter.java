package com.example.found.view.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.found.R;
import com.example.found.databinding.ItemUserBinding;
import com.example.found.model.bean.ListRecommendResponse;
import com.example.found.model.bean.SearchUserResponse;
import com.example.found.viewmodel.SearchViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author DuLong
 * @since 2020/3/4
 * email 798382030@qq.com
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<SearchUserResponse> mData;
    private ItemUserBinding mBinding;

    public UserAdapter(Context mContext, List<SearchUserResponse> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_user, parent, false);
        return new UserAdapter.ViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        SearchUserResponse data = mData.get(position);
        //加载头像
        RequestOptions mOptions = new RequestOptions()
                //占位图
                .placeholder(R.drawable.pic_placehodler)
                .error(R.drawable.pic_placehodler);
        Glide.with(mContext)
                .load(data.getAvatar())
                .apply(mOptions)
                .into(mBinding.imgAvatar);
        if (data.getName() != null) {
            mBinding.txtName.setText(data.getName());
        }
        if (data.getSignature() != null) {
            mBinding.txtPersonalIntroduction.setText(data.getSignature());
        }
        mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
