package com.kamel.tivi.epg.domain;

/**
 * Created by Kristoffer.
 */
public class EPGEvent {

    private final long start;
    private final long end;
    private final String title;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    private String startTime;

    public EPGEvent(long start, long end, String title, String startTime) {
        this.start = start;
        this.end = end;
        this.title = title;
        this.startTime = startTime;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCurrent() {
        long now = System.currentTimeMillis();
        return now >= start && now <= end;
    }
}
