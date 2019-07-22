package com.kamel.tivi.m3u.model;

public class M3UItem {
public String itemDuration;
public String itemName;

    public String itemUrl;
    public String itemIcon;
    String tvg_id;
    String group;

    public String getTvg_id() {
        return tvg_id;
    }

    public void setTvg_id(String tvg_id) {
        this.tvg_id = tvg_id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getItemDuration() {
        return itemDuration;
    }

    public void setItemDuration(String itemDuration) {
        this.itemDuration = itemDuration;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public String getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(String itemIcon) {
        this.itemIcon = itemIcon;
    }

    public M3UItem() {
    }

    public M3UItem(String itemName, String itemUrl, String itemIcon, String group) {
        this.itemName = itemName;
        this.itemUrl = itemUrl;
        this.itemIcon = itemIcon;
        this.group = group;
    }
}
