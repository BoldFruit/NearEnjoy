package com.example.lib_neuqer_chat_ui.emoji;

/**
 * Time:2020/3/20 13:42
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class EmojiBean implements IEmoji {

    private int unicodeInt;
    private int emojiId;
    private EmojiType type;

    public EmojiBean(int unicodeInt, EmojiType type) {
        this.unicodeInt = unicodeInt;
        this.type = type;
    }

    public EmojiBean() {

    }

    public void setUnicodeInt(int unicodeInt) {
        this.unicodeInt = unicodeInt;
    }

    public void setType(EmojiType type) {
        this.type = type;
    }

    @Override
    public String getUnicodeString() {
        return new String(Character.toChars(unicodeInt));
    }

    @Override
    public int getUnicodeInt() {
        return unicodeInt;
    }

    @Override
    public int getId() {
        return emojiId;
    }

    @Override
    public EmojiType getType() {
        return EmojiType.EMOJI;
    }

    public void setEmojiId(int emojiId) {
        this.emojiId = emojiId;
    }
}
