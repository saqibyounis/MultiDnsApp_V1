package com.kamel.tivi.series.model;

public class SeriesModel {
String name;
String seriesId;
String cover;
String plot;
String gener;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getGener() {
        return gener;
    }

    public void setGener(String gener) {
        this.gener = gener;
    }

    public SeriesModel(String name, String seriesId, String cover, String plot, String gener) {
        this.name = name;
        this.seriesId = seriesId;
        this.cover = cover;
        this.plot = plot;
        this.gener = gener;
    }
}
