package com.example.mvvm_simple.model;

/**
 * Time:2020/2/28 21:36
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class QuestionBean {


    /**
     * id : 1
     * question : 行政楼前的广场名叫
     * option : 城市广场#沉思广场#学识广场#创新广场
     */

    private int id;
    private String question;
    private String option;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
