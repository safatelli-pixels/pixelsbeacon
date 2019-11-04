package com.pixelstrade.audioguide.models;


import java.util.ArrayList;
import java.util.List;

public class ArticleResponse {


    private int code;
    private List<Article> data = new ArrayList<>();


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Article> getData() {
        return data;
    }

    public void setData(List<Article> data) {
        this.data = data;
    }
}
