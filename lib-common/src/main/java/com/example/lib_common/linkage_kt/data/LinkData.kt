package com.example.lib_common.linkage_kt.data

/**
 * Time:2020/4/10 8:49
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */

class DataType {
    companion object {
        const val HEADER: Int = 0
        const val FOOTER: Int = 1
        const val ITEM_HEADER: Int = 2
        const val ITEM_DATA: Int = 3
    }
}

data class SecondLinkData(val id: Int, val name: String, val image: String): IBaseLinkData {
    override fun getType(): Int {
       return DataType.ITEM_DATA
    }
}

data class LinkData(val id: Int, val name: String, val secondList: ArrayList<SecondLinkData>): IBaseLinkData {
    override fun getType(): kotlin.Int {
       return DataType.ITEM_DATA
    }
}

data class ItemHeaderData(val title: String): IBaseLinkData {
    override fun getType(): Int {
        return DataType.ITEM_HEADER
    }
}

data class ItemFooterData(val footStr: String): IBaseLinkData {
    override fun getType(): Int {
        return DataType.FOOTER
    }
}