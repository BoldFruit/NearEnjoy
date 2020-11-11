package com.example.found;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.lib_data.data_user.token.TokenManager;
import com.example.network.INetworkRequestInfo;


/**
 * @author DuLong
 * @since 2020/2/23
 * email 798382030@qq.com
 */
public class NetworkRequestInfo implements INetworkRequestInfo {
    @Override
    public HashMap<String, String> getRequestHeadersMap() {
        HashMap<String, String> mHeadersMap = new HashMap<>();
        if (!TokenManager.getToken().equals("") ) {
            mHeadersMap.put("token", TokenManager.getToken());
            return mHeadersMap;
        } else {
            mHeadersMap.put("token", "5055a737c62249a2a1b338a3b1861cfa");
            return mHeadersMap;
        }
    }

    @Override
    public boolean isDebug() {
        return true;
    }

    @Override
    public String getBaseUrl() {
        return "https://blog.csxjh.vip:8000/";
    }

    @Override
    public ArrayList<Integer> getNetCorrectCode() {
        ArrayList<Integer> codes = new ArrayList<>();
        codes.add(0);
        codes.add(200);
        return codes;
    }
}
