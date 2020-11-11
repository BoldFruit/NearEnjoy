package com.example.mvvm_simple.categories.mvvm.model.bean

/**
 * Time:2020/4/13 20:32
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */

/**
 * id : 49
 * userId : 6
 * userName : images/2020/1/28/7c3f852baca13c5e95775643355e724d.jpg
 * userAvatar : null
 * categoryId : 2
 * labelIds : ["2"]
 * name : 高等数学
 * description : 高等数学
 * images : ["images/2020/1/28/7c3f852baca13c5e95775643355e724d.jpg",
 * "images/2020/1/28/7c3f852baca13c5e95775643355e724d.jpg"]
 * price : 15
 * nums : 2
 * wants : 0
 */

data class CategoryDetailBean(val prevPage: Int, val nextPage: Int, val searchList: ArrayList<SearchListBean>)
data class SearchListBean(
          val id: Int,
          val userId: Int,
          val userName: String,
          val userAvatar: String,
          val categoryId: Int,
          val labelIds: String,
          val name: String,
          val description: String,
          val images: String,
          val price: Float,
          val nums: Int,
          val wants: Int
)

class AutoCategoryDetailBean {
    var prevPage: Int = 0
    var nextPage: Int = 0
    var searchList: ArrayList<AutoCategorySearchList> = ArrayList()
}

class AutoCategorySearchList: ISearchData {
    var id: Int = 0
    var userId: Int = 0
    var userName: String = ""
    var userAvatar: String = ""
    var categoryId: Int = 0
    var labelIds: String = ""
    var name: String = ""
    var description: String = ""
    var images: String = ""
    var price: Float = 0f
    var nums: Int = 0
    var wants: Int = 0
    override fun positionType(): Int = ISearchData.POSITION_NORMAL
}

class CategoryFooterData: ISearchData {
    override fun positionType(): Int {
        return ISearchData.POSITION_FOOTER
    }
}

interface ISearchData {
    companion object {
        const val POSITION_HEADER: Int = 0
        const val POSITION_NORMAL: Int = 1
        const val POSITION_FOOTER: Int = 2
    }
    fun positionType(): Int
}