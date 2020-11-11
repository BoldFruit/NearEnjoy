package com.example.mvvm_simple

import com.example.lib_data.data_user.token.TokenManager
import com.example.network.INetworkRequestInfo
import java.util.*

/**
 * @author DuLong
 * @since 2020/4/5
 * email 798382030@qq.com
 */
class NetworkRequestInfo : INetworkRequestInfo{
    companion object {
        const val BASE_URL = "https://blog.csxjh.vip:8000/"
    }
    override fun getRequestHeadersMap(): HashMap<String, String> {
        val mHeadersMap = HashMap<String, String>()
        return if (TokenManager.getToken() != "") {
            mHeadersMap["token"] = TokenManager.getToken()
            mHeadersMap
        } else {
            mHeadersMap["token"] = "5055a737c62249a2a1b338a3b1861cfa"
            mHeadersMap
        }
    }

    override fun getNetCorrectCode(): ArrayList<Int> {
        val codes = ArrayList<Int>()
        codes.add(0)
        codes.add(200)
        return codes
    }

    override fun getBaseUrl(): String {
        return "https://blog.csxjh.vip:8000/"
    }

    override fun isDebug(): Boolean {
        return true
    }
}