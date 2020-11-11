package com.example.mvvm_simple.model.bean

/**
 * @author DuLong
 * @since 2020/4/29
 * email 798382030@qq.com
 */
data class PublicGoodsReqBean(var categoryId: Int?, var labelIds: ArrayList<Int>? = null,
                              var name: String?, var description: String?, var images: ArrayList<String>?,
                              var price: String?, var nums: Int? = 1) {
    constructor() : this(null, null, null, null, null, null, null)
}