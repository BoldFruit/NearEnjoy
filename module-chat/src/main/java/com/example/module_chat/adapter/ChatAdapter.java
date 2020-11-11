package com.example.module_chat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.lib_neuqer_chat.config.DefaultConfig;
import com.example.lib_neuqer_chat_ui.message.BaseMessage;
import com.example.module_chat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Time:2020/3/25 14:29
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class ChatAdapter extends RecyclerView.Adapter<BaseChatViewHolder> {

    private Map<String, BaseChatViewHolder> statusMap;
    //防止条目没渲染出来之前，消息发送结果已经传回来，如果这种情况出现的话
    //就要将状态放进map中存储，等到渲染时先查看有没有存的状态
    private Map<String, DefaultConfig.SendType> typeStoreMap;
    private static final int TYPE_SEND_TXT = 1;
    private static final int TYPE_RECEIVE_TXT = 2;
    private static final int TYPE_SEND_IMG = 3;
    private static final int TYPE_RECEIVE_IMG = 4;

    private static final int LAYOUT_SEND_TXT = R.layout.item_send_txt;
    private static final int LAYOUT_SEND_IMG = R.layout.item_send_img;
    private static final int LAYOUT_RECEIVE_TXT = R.layout.item_receive_txt;
    private static final int LAYOUT_RECEIVE_IMG = R.layout.item_receive_img;

    private List<BaseMessage> list = new ArrayList<>();


    public ChatAdapter() {
        statusMap = new HashMap<>();
        typeStoreMap = new HashMap<>();
    }

    private View itemViewCreate(ViewGroup parent, int type) {

        return LayoutInflater.from(parent.getContext()).inflate(getViewLayout(type), parent, false);

    }

    private int getViewLayout(int type) {
        switch (type) {
            case TYPE_SEND_TXT:
                return LAYOUT_SEND_TXT;
            case TYPE_SEND_IMG:
                return LAYOUT_SEND_IMG;
            case TYPE_RECEIVE_TXT:
                return LAYOUT_RECEIVE_TXT;
            case TYPE_RECEIVE_IMG:
                return TYPE_RECEIVE_IMG;
            default:
                throw new IllegalStateException("known type");
        }
    }

    @NonNull
    @Override
    public BaseChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_SEND_TXT:
                return new ChatSendTextViewHolder(itemViewCreate(parent, viewType));
            case TYPE_SEND_IMG:
                return new ChatSendImgViewHolder(itemViewCreate(parent, viewType));
            case TYPE_RECEIVE_TXT:
                return new ChatReceiveTextViewHolder(itemViewCreate(parent, viewType));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseChatViewHolder holder, int position) {
        holder.bind(list.get(position));
//        String id = list.get(position).getUuid();
//        if (typeStoreMap.containsKey(id)) {
//            holder.setStatus(typeStoreMap.remove(id));
//        } else {
//            statusMap.put(list.get(position).getUuid(), holder);
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        BaseMessage message = list.get(position);
        switch (message.getType()) {
            case BaseMessage.TXT:
               return message.isMyself() ? TYPE_SEND_TXT : TYPE_RECEIVE_TXT;
            case BaseMessage.IMG:
                return message.isMyself() ? TYPE_SEND_IMG : TYPE_RECEIVE_IMG;
            default:
                return -1;
        }

    }

    public void addData(BaseMessage message) {
        list.add(message);
        notifyDataSetChanged();
    }

    public void addDatas(List<? extends BaseMessage> messages) {
        list.addAll(messages);
        notifyDataSetChanged();
    }

    public void setStatus(String id, DefaultConfig.SendType type) {
//        if (statusMap.containsKey(id)) {
//            statusMap.get(id).setStatus(type);
//            statusMap.remove(id);
//        } else {
//            typeStoreMap.put(id, type);
//        }
        int temp = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUuid().equals(id)) {
                temp = i;
                switch (type) {
                    case SENDING:
                        list.get(i).setSendingType(BaseMessage.SEND_SENDING_TYPE);
                        break;
                    case SEND_SUCCESS:
                        list.get(i).setSendingType(BaseMessage.SEND_SUCCESS_TYPE);
                        break;
                    case SEND_FAILD:
                        list.get(i).setSendingType(BaseMessage.SEND_FAILED_TYPE);
                        break;
                }
            }
        }
        if (-1 != temp) {
            notifyItemChanged(temp);
        }
    }
}
