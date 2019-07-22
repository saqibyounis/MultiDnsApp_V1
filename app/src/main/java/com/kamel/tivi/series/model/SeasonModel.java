package com.kamel.tivi.series.model;

import java.util.List;

public class SeasonModel {
    String airDate;
    String episodCount;
    String id;
    String cover;
    String seasonNumber;

    public SeasonModel(String airDate, String episodCount, String id, String cover, String seasonNumber, List<EpisodeModel> episodeModels, String name) {
        this.airDate = airDate;
        this.episodCount = episodCount;
        this.id = id;
        this.cover = cover;
        this.seasonNumber = seasonNumber;
        this.episodeModels = episodeModels;
        this.name = name;
    }

    List<EpisodeModel> episodeModels;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EpisodeModel> getEpisodeModels() {
        return episodeModels;
    }

    public void setEpisodeModels(List<EpisodeModel> episodeModels) {
        this.episodeModels = episodeModels;
    }

    String name;


    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public String getEpisodCount() {
        return episodCount;
    }

    public void setEpisodCount(String episodCount) {
        this.episodCount = episodCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(String seasonNumber) {
        this.seasonNumber = seasonNumber;
    }


}
