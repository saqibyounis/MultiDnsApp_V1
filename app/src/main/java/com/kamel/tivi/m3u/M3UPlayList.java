package com.kamel.tivi.m3u;

import com.kamel.tivi.m3u.model.M3UItem;

import java.util.List;

public class M3UPlayList {

    private String playlistName;

    private String playlistParams;

    private List<M3UItem> playlistItems;

   public     List<M3UItem> getPlaylistItems() {
        return playlistItems;
    }

   public void setPlaylistItems(List<M3UItem> playlistItems) {
        this.playlistItems = playlistItems;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getPlaylistParams() {
        return playlistParams;
    }

    public void setPlaylistParams(String playlistParams) {
        this.playlistParams = playlistParams;
    }

    public String getSingleParameter(String paramName) {
        String[] paramsArray = this.playlistParams.split(" ");
        for (String parameter : paramsArray) {
            if (parameter.contains(paramName)) {
                return parameter.substring(parameter.indexOf(paramName) + paramName.length()).replace("=", "");
            }
        }
        return "";
    }
}