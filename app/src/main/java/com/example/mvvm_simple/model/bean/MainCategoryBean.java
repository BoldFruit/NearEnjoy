package com.example.mvvm_simple.model.bean;

import java.util.List;

/**
 * @author DuLong
 * @since 2020/4/10
 * email 798382030@qq.com
 */
public class MainCategoryBean {
    /**
     * name : 书改
     * icon : /images/2020/2/10/xxx.jpg
     * categoryList : [{"id":2,"level":2},{"id":5,"level":3}]
     */

    private String name;
    private String icon;
    private List<CategoryListBean> categoryList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<CategoryListBean> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryListBean> categoryList) {
        this.categoryList = categoryList;
    }

    public static class CategoryListBean {
        /**
         * id : 2
         * level : 2
         */

        private int id;
        private int level;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }
}
