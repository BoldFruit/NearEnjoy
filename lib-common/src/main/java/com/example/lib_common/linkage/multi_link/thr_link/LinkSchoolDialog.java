package com.example.lib_common.linkage.multi_link.thr_link;

import android.content.Context;
import android.util.SparseArray;
import android.view.DragAndDropPermissions;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.lib_common.R;
import com.example.lib_common.dialog.bottom_dialog.BottomDialogBase;
import com.example.lib_common.linkage.multi_link.MultiLinkController;
import com.example.lib_common.linkage.multi_link.WheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Time:2020/5/17 22:13
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class LinkSchoolDialog extends BottomDialogBase {

    private LinearLayout mLinkSelector;
    private WheelView mProvinceView;
    private WheelView mCityView;
    private WheelView mSchoolView;
    private List<SchoolData> schoolData;

    private SparseArray<List<SchoolData.CityListBean>> mCitiesMap;
    private SparseArray<List<SchoolData.CityListBean.SchoolListBean>> mSchoolsMap;
    private MultiLinkController.LinkedChangeListener linkedChangeListener;
    private MultiLinkController mController;

    private HashMap<Integer, List<SchoolData.CityListBean>> mCityMap;
    private HashMap<Integer, List<SchoolData.CityListBean.SchoolListBean>> mSchoolMap;

    public LinkSchoolDialog(Context context, List<SchoolData> datas) {
        super(context);
        this.schoolData = datas;
        setSchoolData();
    }

    @Override
    protected void onCreate() {
        setContentView(R.layout.common_three_link_layout);

        mCitiesMap = new SparseArray<>();
        mSchoolsMap = new SparseArray<>();

        mCityMap = new HashMap<>();
        mSchoolMap = new HashMap<>();
        linkedChangeListener = new MultiLinkController.LinkedChangeListener() {
            @Override
            public List<String> getChangedResult(int s, int id) {
                switch (s) {
                    case 0:
                        return getCityNameList(mCitiesMap.get(id));
                    case 1:
                        return getSchoolNameList(mSchoolsMap.get(id));
                    default:
                        return new ArrayList<>();
                }
            }

            @Override
            public List<ContentWrapper> getChangedWrapperResult(int s, int id) {
                switch (s) {
                    case 0:
                        return getCityWrapperList(mCityMap.get(id));
                    case 1:
                        return getSchoolWrapperList(mSchoolMap.get(id));
                    default:
                        return new ArrayList<>();
                }
            }
        };
        mProvinceView = findViewById(R.id.common_three_link_province_view);
        mCityView = findViewById(R.id.common_three_link_city_view);
        mSchoolView = findViewById(R.id.common_three_link_school_view);
        mController = new MultiLinkController(linkedChangeListener);
    }

    public void setSchoolData() {
        List<String> provinces = new ArrayList<>();
        List<String> cities = new ArrayList<>();
        List<String> schools = new ArrayList<>();
        List<SchoolData.CityListBean> tempCities;
        List<SchoolData.CityListBean.SchoolListBean> tempSchools;


//        for (int i = 0; i < schoolData.size(); i++) {
//            SchoolData dataBean = schoolData.get(i);
//            provinces.add(dataBean.getName());
//            tempCities = dataBean.getCityList();
//            mCitiesMap.append(i, tempCities);
//            for (int j = 0; j < tempCities.size(); j++) {
//                SchoolData.CityListBean cityBean = tempCities.get(j);
//                cities.add(cityBean.getName());
//                tempSchools = cityBean.getSchoolList();
//                mSchoolsMap.append(j, tempSchools);
//                for (int k = 0; k < tempSchools.size(); k++) {
//                    schools.add(tempSchools.get(k).getName());
//                }
//            }
//        }


        for (int i = 0; i < schoolData.size(); i++) {
            SchoolData dataBean = schoolData.get(i);
            provinces.add(dataBean.getName());
            tempCities = dataBean.getCityList();
            mCityMap.put(dataBean.getId(), tempCities);
            for (int j = 0; j < tempCities.size(); j++) {
                SchoolData.CityListBean cityBean = tempCities.get(j);
                cities.add(cityBean.getName());
                tempSchools = cityBean.getSchoolList();
                mSchoolMap.put(cityBean.getId(), tempSchools);
                for (int k = 0; k < tempSchools.size(); k++) {
                    schools.add(tempSchools.get(k).getName());
                }
            }
        }

//        mProvinceView.setItems(provinces);
//        mCityView.setItems(cities);
//        mSchoolView.setItems(schools);
        mProvinceView.setRealItems(getProvinceWrapperList(schoolData));
        mCityView.setRealItems(getCityWrapperList(schoolData.get(0).getCityList()));
        mSchoolView.setRealItems(getSchoolWrapperList(schoolData.get(0).getCityList().get(0).getSchoolList()));
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


    public List<ContentWrapper> getProvinceWrapperList(List<SchoolData> dataListBeans) {
        List<ContentWrapper> result = new ArrayList<>();
        for (SchoolData bean : dataListBeans) {
            ContentWrapper wrapper = new ContentWrapper(bean.getId(), bean.getName(), ContentWrapper.PRO_SER);
            result.add(wrapper);
        }
        return result;
    }

    public List<ContentWrapper> getCityWrapperList(List<SchoolData.CityListBean> cityListBeans) {
        List<ContentWrapper> result = new ArrayList<>();
        if (cityListBeans == null) {
            System.out.println("city_list_bean null");
        }
        for (SchoolData.CityListBean bean : cityListBeans) {
            ContentWrapper wrapper = new ContentWrapper(bean.getId(), bean.getName(), ContentWrapper.CITY_SER);
            result.add(wrapper);
        }
        return result;
    }

    public List<ContentWrapper> getSchoolWrapperList(List<SchoolData.CityListBean.SchoolListBean> schoolListBeans) {
        List<ContentWrapper> result = new ArrayList<>();
        for (SchoolData.CityListBean.SchoolListBean bean : schoolListBeans) {
            ContentWrapper wrapper = new ContentWrapper(bean.getId(), bean.getName(), ContentWrapper.SCHOOL_SER);
            result.add(wrapper);
        }
        return result;
    }
}
