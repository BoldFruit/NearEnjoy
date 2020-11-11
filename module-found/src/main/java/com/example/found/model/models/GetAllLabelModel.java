package com.example.found.model.models;

import android.webkit.HttpAuthHandler;

import com.example.base.model.BaseModel;
import com.example.base.model.bean.BaseCachedData;
import com.example.found.model.SearchRepository;
import com.example.found.model.bean.LabelBean;
import com.example.lib_common.util.SPUtils;
import com.example.network.exception.ApiException;
import com.example.network.observer.BaseObserver;

import java.util.HashMap;
import java.util.List;

/**
 * @author DuLong
 * @since 2020/3/14
 * email 798382030@qq.com
 */

/**
 * 获取所有标签的model
 */
public class GetAllLabelModel extends BaseModel<List<LabelBean>> {
    public static final String TAG = "GetAllLabelModel";


    @Override
    protected void load() {
        SearchRepository.getInstance().getAllLabels(new BaseObserver<List<LabelBean>>(this) {
            @Override
            public void onError(ApiException e) {
                loadFail(e.toString());
            }

            @Override
            public void onNext(List<LabelBean> mLabelBeans) {
                loadSuccess(mLabelBeans);
            }
        });
    }

    @Override
    protected String getCachedPreferenceKey() {
        return "labels";
    }

    /**
     * 存储到sp中供以后查找标签时使用
     * @param data
     */
    @Override
    protected void saveDataToPreference(BaseCachedData<List<LabelBean>> data) {
        HashMap<Integer, String> labels = new HashMap<>();
        for (LabelBean item : data.getData()) {
            labels.put(item.getId(), item.getName());
        }
        SPUtils.put("labels", labels);
    }


}
