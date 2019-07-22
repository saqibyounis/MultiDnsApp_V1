package com.kamel.tivi.epg.model;

public class EpgModel {


     String duration;
    String id;
    String epgid;
    String title;
    String start;

    String stop;
    String description;
  String start_time;
  String stop_time;
String settime;

    public String getSettime() {
        return settime;
    }

    public void setSettime(String settime) {
        this.settime = settime;
    }

    public String getTimeto() {
        return timeto;
    }

    public void setTimeto(String timeto) {
        this.timeto = timeto;
    }

    String timeto;
    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStop_time() {
        return stop_time;
    }

    public void setStop_time(String stop_time) {
        this.stop_time = stop_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEpgid() {
        return epgid;
    }

    public void setEpgid(String epgid) {
        this.epgid = epgid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public EpgModel(String id, String epgid, String title, String start, String stop, String description, String start_time, String stop_time, String settime, String timeto, String duration) {
        this.id = id;
        this.epgid = epgid;
        this.title = title;
        this.start = start;
        this.stop = stop;
        this.description = description;
        this.start_time=start_time;
        this.stop_time=stop_time;
        this.settime=settime;
        this.timeto=timeto;
        this.duration=duration;
    }


}
