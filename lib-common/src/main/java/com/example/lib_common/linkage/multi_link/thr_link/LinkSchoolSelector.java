package com.example.lib_common.linkage.multi_link.thr_link;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.lib_common.R;
import com.example.lib_common.linkage.multi_link.MultiLinkController;
import com.example.lib_common.linkage.multi_link.WheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Time:2020/5/17 20:53
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class LinkSchoolSelector extends LinearLayout {
    private LinearLayout mLinkSelector;
    private WheelView mProvinceView;
    private WheelView mCityView;
    private WheelView mSchoolView;
    private List<SchoolData> schoolData;

    private SparseArray<List<SchoolData.CityListBean>> mCitiesMap;
    private SparseArray<List<SchoolData.CityListBean.SchoolListBean>> mSchoolsMap;
    private MultiLinkController.LinkedChangeListener linkedChangeListener;
    private MultiLinkController mController;
    public LinkSchoolSelector(Context context) {
        super(context);
        init(context);
    }

    public LinkSchoolSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LinkSchoolSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        mCitiesMap = new SparseArray<>();
        mSchoolsMap = new SparseArray<>();
//        linkedChangeListener = (s, index) -> {
//            switch (s) {
//                case 0:
//                    return getCityNameList(mCitiesMap.get(index));
//                case 1:
//                    return getSchoolNameList(mSchoolsMap.get(index));
//                default:
//                    return new ArrayList<>();
//            }
//        };
        mLinkSelector = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.common_three_link_layout, this);
        mProvinceView = mLinkSelector.findViewById(R.id.common_three_link_province_view);
        mCityView = mLinkSelector.findViewById(R.id.common_three_link_city_view);
        mSchoolView = mLinkSelector.findViewById(R.id.common_three_link_school_view);
        mController = new MultiLinkController(linkedChangeListener);

    }

    public void setSchoolData(List<SchoolData> datas) {
        this.schoolData = datas;
        List<String> provinces = new ArrayList<>();
        List<String> cities = new ArrayList<>();
        List<String> schools = new ArrayList<>();
        List<SchoolData.CityListBean> tempCities;
        List<SchoolData.CityListBean.SchoolListBean> tempSchools;


        for (int i = 0; i < datas.size(); i++) {
            SchoolData dataBean = datas.get(i);
            provinces.add(dataBean.getName());
            tempCities = dataBean.getCityList();
            mCitiesMap.append(i, tempCities);
            for (int j = 0; j < tempCities.size(); j++) {
                SchoolData.CityListBean cityBean = tempCities.get(j);
                cities.add(cityBean.getName());
                tempSchools = cityBean.getSchoolList();
                mSchoolsMap.append(j, tempSchools);
                for (int k = 0; k < tempSchools.size(); k++) {
                    schools.add(tempSchools.get(k).getName());
                }
            }
        }
        mProvinceView.setItems(provinces);
        mCityView.setItems(cities);
        mSchoolView.setItems(schools);
        mController.addWheelView(mProvinceView);
        mController.addWheelView(mCityView);
        mController.addWheelView(mSchoolView);

    }


    public List<String> getCityNameList(List<SchoolData.CityListBean> cityListBeans) {
        List<String> result = new ArrayList<>();
        for (SchoolData.CityListBean bean : cityListBeans) {
            result.add(bean.getName());
        }
        return result;
    }

    public List<String> getSchoolNameList(List<SchoolData.CityListBean.SchoolListBean> schoolListBeans) {
        List<String> result = new ArrayList<>();
        for (SchoolData.CityListBean.SchoolListBean bean : schoolListBeans) {
            result.add(bean.getName());
        }
        return result;
    }




}
