package kr.co.parkcom.store.api.datalab.dto;

import java.util.List;

public class Result {
    private String title;
    private List<String> keywords;
    private List<Data> data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getKeyword() {
        return keywords;
    }

    public void setKeywords(List<String> keyword) {
        this.keywords = keywords;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
