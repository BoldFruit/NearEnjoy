/*
 * Copyright (c) 2017.  Joe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.mvvm_simple.jump;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.example.mvvm_simple.categories.mvvm.view.DetailCategoryFragment;
import com.example.mvvm_simple.goods_detail.view.GoodsDetailActivity;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

/**
 * Created by joe on 2017/12/28.
 * Email: lovejjfg@gmail.com
 */

@SuppressWarnings({ "WeakerAccess", "unused" })
public class JumpUtils {
    private static final String TAG = JumpUtils.class.getSimpleName();

    public static boolean isKnownSchemes(String url) {
        return !TextUtils.isEmpty(url) && (isKnown(url) || isHttp(url));
    }

    private static boolean isHttp(String url) {
        return url.startsWith(Constants.HTTP_SCHEME) || url.startsWith(Constants.HTTPS_SCHEME);
    }
    private static boolean isKnown(String url) {
        return url.startsWith(Constants.KNOWN_SCHEME);
    }

    public static Intent parseIntent(Context context, String url, String tile) {
        if (!isKnownSchemes(url)) {
            return null;
        }
        if (isHttp(url)) {
            return parseHttp(context, url, tile);
        }

        try {
            Uri data = Uri.parse(url);
            String scheme = data.getScheme();
            String host = data.getHost();
            String path = data.getPath();
            Set<String> queryParameterNames = data.getQueryParameterNames();
            HashMap<String, String> map = null;
            if (!queryParameterNames.isEmpty()) {
                map = new HashMap<>();
                for (String name: queryParameterNames) {
                    map.put(name, data.getQueryParameter(name));
                }
            }
            assert path != null;
            assert map != null;
            return parseSchemes(context, host, path, map);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Intent parseSchemes(Context context, String host, String path, HashMap<String, String> parameters) {
        String[] paths = path.split("/");
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        if (parameters.get("id") == null) {
            return null;
        }
        intent.putExtra(DetailCategoryFragment.START_GOODS_DETAIL, Integer.valueOf(Objects.requireNonNull(parameters.get("id"))));
        return intent;
    }

    private static Intent parseHttp(Context context, String url, String title) {
        Intent intent = new Intent(context,null);
        intent.putExtra(Constants.URL, url);
        intent.putExtra(Constants.TITLE, title);
        return intent;
    }
}
