package com.example.mvvm_simple.model.bean;

/**
 * @author DuLong
 * @since 2020/4/10
 * email 798382030@qq.com
 */
public class LabelCategoriesBean {
    /**
     * id : 1
     * name : 书籍
     * level : 1
     */

    private int id;
    private String name;
    private int level;

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
