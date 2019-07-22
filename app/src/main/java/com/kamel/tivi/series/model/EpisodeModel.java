package com.kamel.tivi.series.model;

public class EpisodeModel {

    String id;
    String title;
    String extention;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExtention() {
        return extention;
    }

    public void setExtention(String extention) {
        this.extention = extention;
    }

    public EpisodeModel(String id, String title, String extention) {
        this.id = id;
        this.title = title;
        this.extention = extention;
    }
}
