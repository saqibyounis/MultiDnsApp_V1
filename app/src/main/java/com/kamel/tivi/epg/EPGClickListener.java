package com.kamel.tivi.epg;

import com.kamel.tivi.epg.domain.EPGChannel;
import  com.kamel.tivi.epg.domain.EPGEvent;

/**
 * Created by Kristoffer on 15-05-25.
 */
public interface EPGClickListener {

    void onChannelClicked(int channelPosition, EPGChannel epgChannel);

    void onEventClicked(int channelPosition, int programPosition, EPGEvent epgEvent);

    void onResetButtonClicked();
}
