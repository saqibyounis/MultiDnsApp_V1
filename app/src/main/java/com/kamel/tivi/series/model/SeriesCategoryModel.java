package com.kamel.tivi.series.model;

import java.util.ArrayList;
import java.util.List;

public class SeriesCategoryModel {
    String categoryId;
    String categoryName;
    String parentId;

    public List<SeriesModel> getSeriesModels() {
        return seriesModels;
    }

    public void setSeriesModels(List<SeriesModel> seriesModels) {
        this.seriesModels = seriesModels;
    }

    List<SeriesModel> seriesModels;
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public SeriesCategoryModel(String categoryId, String categoryName, String parentId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.seriesModels=new ArrayList<>();
    }
}
