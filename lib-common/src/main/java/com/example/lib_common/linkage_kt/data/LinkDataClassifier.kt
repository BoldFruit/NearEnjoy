package com.example.lib_common.linkage_kt.data

/**
 * Time:2020/4/10 9:54
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
class LinkDataClassifier {
    /**
     * {
    "id": 1,
    "name": "书籍",
    "secondList": [
    {
    "id": 2,
    "name": "教材",
    "image": "https://blog.csxjh.vip/usr/uploads/2020/03/3694667439.jpg"
    },
    {
    "id": 5,
    "name": "教辅",
    "image": "https://blog.csxjh.vip/usr/uploads/2020/03/3694667439.jpg"
    },
    {
    "id": 8,
    "name": "工具书",
    "image": "https://blog.csxjh.vip/usr/uploads/2020/03/3694667439.jpg"
    }
    ]
    }
     */
    companion object {
        fun getLinkData(dataList: ArrayList<LinkData>, hasHeader: Boolean): ArrayList<IBaseLinkData> {
            val resultList: ArrayList<IBaseLinkData> = ArrayList()
            for (i in dataList.indices) {
                if (i == 0 && hasHeader) {
                    //TODO:添加Header
                }
                resultList.add(ItemHeaderData(dataList[i].name))
                for (j in dataList[i].secondList) {
                    resultList.add(j)
                }
            }
            resultList.add(ItemFooterData("---到底了---"))
            return resultList
        }

        fun getLinkDataOriginal(dataList: ArrayList<LinkData>): ArrayList<IBaseLinkData> {
            val resultList: ArrayList<IBaseLinkData> = ArrayList()
            for (i in dataList.indices) {
                if (dataList[i].name == "电子设备及配件") {
                    dataList[i].secondList.add(SecondLinkData(11, "电子表", "https://i.ibb.co/LYkq8q6/1586598331411-ckt.png"))
                    dataList[i].secondList.add(SecondLinkData(12, "电子秤", "https://i.ibb.co/LYkq8q6/1586598331411-ckt.png"))
                    dataList[i].secondList.add(SecondLinkData(13, "电子羊", "https://i.ibb.co/LYkq8q6/1586598331411-ckt.png"))
                }
            }
            for (i in dataList.indices) {
                resultList.add(dataList[i])
            }
            for (i in dataList.indices) {
                resultList.add(dataList[i])
            }
            for (i in dataList.indices) {
                resultList.add(dataList[i])
            }
            for (i in dataList.indices) {
                resultList.add(dataList[i])
            }
            for (i in dataList.indices) {
                resultList.add(dataList[i])
            }
            resultList.add(ItemFooterData("---到底了---"))
            return resultList
        }

        fun getSideList(dataList: ArrayList<LinkData>): ArrayList<String> {
            val resultList: ArrayList<String> = ArrayList()
            for (i in dataList) {
                resultList.add(i.name)
            }
            for (i in dataList) {
                resultList.add(i.name)
            }
            for (i in dataList) {
                resultList.add(i.name)
            }
            for (i in dataList) {
                resultList.add(i.name)
            }
            for (i in dataList) {
                resultList.add(i.name)
            }

            return resultList
        }
    }

}