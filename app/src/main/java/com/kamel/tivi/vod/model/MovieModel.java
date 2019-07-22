package com.kamel.tivi.vod.model;

public class MovieModel {
    String name;
    String streaamId;
    String icon;
    String categoryId;
    String extension;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreaamId() {
        return streaamId;
    }

    public void setStreaamId(String streaamId) {
        this.streaamId = streaamId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public MovieModel(String name, String streaamId, String icon, String categoryId, String extension) {
        this.name = name;
        this.streaamId = streaamId;
        this.icon = icon;
        this.categoryId = categoryId;
        this.extension = extension;
    }
}
