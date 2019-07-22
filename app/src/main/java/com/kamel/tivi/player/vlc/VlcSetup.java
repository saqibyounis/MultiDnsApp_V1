package com.kamel.tivi.player.vlc;

import com.kamel.tivi.channels.ChannelsModel;

import java.util.List;

public class VlcSetup {
    List<ChannelsModel> channelList;
    int channelPosition;
           public static VlcSetup vlcSetup;
    public VlcSetup(List<ChannelsModel> channelList, int channelPosition) {
        this.channelList = channelList;
        this.channelPosition = channelPosition;
           this.vlcSetup=this;
    }

    public List<ChannelsModel> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<ChannelsModel> channelList) {
        this.channelList = channelList;
    }

    public int getChannelPosition() {
        return channelPosition;
    }

    public void setChannelPosition(int channelPosition) {
        this.channelPosition = channelPosition;
    }


    public ChannelsModel chPlus(){
        ChannelsModel channelsModel;

        try {
              channelsModel = channelList.get(channelPosition);
              this.channelPosition++;
          }catch (Exception ex){
              this.channelPosition=0;
            channelsModel = channelList.get(channelPosition);

            return channelsModel;


          }

          return channelsModel;


    }

    public ChannelsModel chMinus(){

        ChannelsModel channelsModel;

        try {
            this.channelPosition--;
            channelsModel = channelList.get(channelPosition);

        }catch (Exception ex){
            this.channelPosition=channelList.size()-1;
            channelsModel = channelList.get(channelPosition);

            return channelsModel;


        }

        return channelsModel;


    }
}
