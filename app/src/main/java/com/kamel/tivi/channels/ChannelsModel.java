package com.kamel.tivi.channels;

public class ChannelsModel {

    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getEpgChannelid() {
        return epgChannelid;
    }

    public void setEpgChannelid(String epgChannelid) {
        this.epgChannelid = epgChannelid;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String streamId;
    public String epgChannelid;
    public String categoryId;
    public String link;
public ChannelsModel(){}
    public ChannelsModel(String name, String streamId, String epgChannelid, String categoryId, String link, String logo) {
        this.name = name;
        this.streamId = streamId;
        this.epgChannelid = epgChannelid;
        this.categoryId = categoryId;
        this.link = link;
        this.logo = logo;
    }

    public String logo;



}
