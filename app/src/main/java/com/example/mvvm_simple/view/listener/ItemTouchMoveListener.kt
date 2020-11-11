package com.example.mvvm_simple.view.listener

import java.text.FieldPosition

/**
 * @author DuLong
 * @since 2020/4/20
 * email 798382030@qq.com
 */
interface ItemTouchMoveListener {
    /**
     * 当拖拽的时候回调,交换fromPosition与toPosition
     */
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

    /**
     * 当条目被移除是回调，删除position条目
     */
    fun onItemRemove(position: Int): Boolean
}