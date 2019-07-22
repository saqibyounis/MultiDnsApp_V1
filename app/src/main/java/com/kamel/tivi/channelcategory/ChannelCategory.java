package com.kamel.tivi.channelcategory;

import com.kamel.tivi.channels.ChannelsModel;

import java.util.ArrayList;
import java.util.List;

public class ChannelCategory {
String name;

    public List<ChannelsModel> getChanneles() {
        return channeles;
    }

    public void setChanneles(List<ChannelsModel> channeles) {
        this.channeles = channeles;
    }

    List<ChannelsModel> channeles;
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public ChannelCategory(String name, String categoryId, boolean locked) {
        this.name = name;
        this.categoryId = categoryId;
        this.locked = locked;
        this.channeles=new ArrayList<>();
    }

    String categoryId;



boolean locked=false;

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
