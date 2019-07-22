package com.kamel.tivi.vod.model;

import java.util.ArrayList;
import java.util.List;

public class VodCategoryModel {
    String categoryId;
    String vategoryName;
    String parentId;

    public List<MovieModel> getMoviles() {
        return moviles;
    }

    public void setMoviles(List<MovieModel> moviles) {
        this.moviles = moviles;
    }

    List<MovieModel> moviles ;
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getVategoryName() {
        return vategoryName;
    }

    public void setVategoryName(String vategoryName) {
        this.vategoryName = vategoryName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public VodCategoryModel(String categoryId, String vategoryName, String parentId) {
        this.categoryId = categoryId;
        this.vategoryName = vategoryName;
        this.parentId = parentId;
        this.moviles=new ArrayList<>();
    }
}
