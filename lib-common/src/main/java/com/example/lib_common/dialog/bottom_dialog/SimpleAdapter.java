package com.example.lib_common.dialog.bottom_dialog;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lib_common.R;
import com.example.lib_common.linkage_kt.adapter.SideListAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2020/4/20 15:54
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {

    public interface SimpleItemClickListener {
       void onClick(String title);
    }

    private SimpleItemClickListener listener;
    private ArrayList<Integer> imagesList = new ArrayList<>();
    private ArrayList<String> titleList = new ArrayList<>();

    public SimpleAdapter(ArrayList<Integer> imagesList, ArrayList<String> titleList) {
        this.imagesList = imagesList;
        this.titleList = titleList;
    }

    public SimpleAdapter() {

    }

    public void setListener(SimpleItemClickListener listener) {
        this.listener = listener;
    }
    public SimpleAdapter addImage(int resourceId, String title) {
        imagesList.add(resourceId);
        titleList.add(title);
        return this;
    }

    @NonNull
    @Override
    public SimpleAdapter.SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SimpleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.common_share_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleAdapter.SimpleViewHolder holder, int position) {
        holder.bindView(imagesList.get(position), titleList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.common_simple_share_item_image);
            textView = itemView.findViewById(R.id.common_simple_share_item_txt);
        }

        void bindView(int img, String title, SimpleItemClickListener listener) {
            if (listener != null) {
                imageView.setOnClickListener(v -> {
                    listener.onClick(title);
                });
            }
            imageView.setImageResource(img);
            textView.setText(title);
        }
    }

}
