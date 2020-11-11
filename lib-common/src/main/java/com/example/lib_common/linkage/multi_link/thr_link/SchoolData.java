package com.example.lib_common.linkage.multi_link.thr_link;

import java.util.List;

/**
 * Time:2020/5/17 16:04
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class SchoolData {

    /**
     * id : 3
     * name : 河北省
     * cityList : [{"id":4,"name":"秦皇岛市","schoolList":[{"id":1,"name":"东北大学秦皇岛分校"},{"id":2,"name":"燕山大学"}]}]
     */

    private int id;
    private String name;
    private List<CityListBean> cityList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityListBean> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityListBean> cityList) {
        this.cityList = cityList;
    }

    public static class CityListBean {
        /**
         * id : 4
         * name : 秦皇岛市
         * schoolList : [{"id":1,"name":"东北大学秦皇岛分校"},{"id":2,"name":"燕山大学"}]
         */

        private int id;
        private String name;
        private List<SchoolListBean> schoolList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<SchoolListBean> getSchoolList() {
            return schoolList;
        }

        public void setSchoolList(List<SchoolListBean> schoolList) {
            this.schoolList = schoolList;
        }

        public static class SchoolListBean {
            /**
             * id : 1
             * name : 东北大学秦皇岛分校
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
