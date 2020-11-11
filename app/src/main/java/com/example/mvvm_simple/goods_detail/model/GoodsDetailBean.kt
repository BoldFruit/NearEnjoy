package com.example.mvvm_simple.goods_detail.model

/**
 * Time:2020/4/18 17:54
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
data class GoodsDetailBean(
    val categoryId: Int,
    val description: String,
    val id: Int,
    val images: String,
    val labelIds: String,
    val name: String,
    val nums: Int,
    val price: Double,
    val userAvatar: String,
    val userId: Int,
    val userName: String,
    val wants: Int
)