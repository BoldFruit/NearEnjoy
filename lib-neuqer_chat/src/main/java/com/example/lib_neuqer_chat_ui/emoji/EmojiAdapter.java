package com.example.lib_neuqer_chat_ui.emoji;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.lib_neuqer_chat.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Time:2020/3/21 17:51
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class EmojiAdapter extends BaseQuickAdapter<EmojiBean, BaseViewHolder> {

    private int deleteRes;

    public EmojiAdapter(int layoutResId, @Nullable List<EmojiBean> data, int deleteRes) {
        super(layoutResId, data);
        this.deleteRes = deleteRes;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, EmojiBean emojiBean) {
        if (emojiBean.getId() == 0) {
            baseViewHolder.setBackgroundResource(R.id.txt_emoji, deleteRes == 0 ? R.mipmap.rc_icon_emoji_delete : deleteRes);
        } else {
            baseViewHolder.setText(R.id.txt_emoji, emojiBean.getUnicodeString());
        }
    }
}
