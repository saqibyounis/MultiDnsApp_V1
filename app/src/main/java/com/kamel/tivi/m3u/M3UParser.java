package com.kamel.tivi.m3u;

import com.kamel.tivi.m3u.model.M3UItem;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class M3UParser {

    private static final String EXT_M3U = "#EXTM3U";
    private static final String EXT_INF = "#EXTINF:";
    private static final String EXT_PLAYLIST_NAME = "#PLAYLIST";
    private static final String EXT_LOGO = "tvg-logo";
    private static final String EXT_URL = "http://";
    private static final String tvg_id= "tvg-id";
    private static final String tvg_name = "tvg-name";
    private static final String group_title = "group-title";


    public String convertStreamToString(InputStream is) {
        try {
            return new Scanner(is).useDelimiter("\\A").next();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    public List<M3UItem> parseFile(String inputStream) throws FileNotFoundException {
        String prevCategory="";
        boolean first=true;
        List<M3UItem> m3UItemsList=new ArrayList<>();
        M3UPlayList m3UPlaylist = new M3UPlayList();
        List<M3UItem> playlistItems = new ArrayList<>();
        String stream = inputStream;
        String linesArray[] = stream.split(EXT_INF);
        for (int i = 0; i < linesArray.length; i++) {
            String currLine = linesArray[i];
            if (currLine.contains(EXT_M3U)) {

            } else {
                M3UItem playlistItem = new M3UItem();
                String[] dataArray = currLine.split(",");
                if (dataArray[0].contains(EXT_LOGO)) {
                    String duration = dataArray[0].substring(0, dataArray[0].indexOf(EXT_LOGO)).replace(":", "").replace("\n", "");
                    String icon = dataArray[0].substring(dataArray[0].indexOf(EXT_LOGO) + EXT_LOGO.length()).replace("=", "").replace("\"", "").replace("\n", "");
                    playlistItem.setItemDuration(duration);
                    playlistItem.setItemIcon(icon);
                } else {
                    String duration = dataArray[0].replace(":", "").replace("\n", "");
                    playlistItem.setItemDuration(duration);
                    playlistItem.setItemIcon("");
                }
                try {

                    String url = dataArray[1].substring(dataArray[1].indexOf(EXT_URL)).replace("\n", "").replace("\r", "");
                    String name = dataArray[0].substring(dataArray[0].indexOf(tvg_name),dataArray[0].indexOf(EXT_LOGO)).replace("\n", "");
                    String icon= dataArray[0].substring(dataArray[0].indexOf(EXT_LOGO),dataArray[0].indexOf(group_title)).replace("\n", "");

                    String tvgid=dataArray[0].substring(dataArray[0].indexOf(tvg_id),dataArray[0].indexOf(tvg_name)).replace("\n", "");
                    String group=dataArray[0].substring(dataArray[0].indexOf(group_title)).replace("\n", "");
                      StringBuilder nameBuilder=new StringBuilder(name);
                    StringBuilder groupBuilder=new StringBuilder(group);
                    StringBuilder iconBuilder=new StringBuilder(icon);
                    name=removeExtra(nameBuilder,name);
                         group=removeExtra(groupBuilder,group);
                         if(icon.equalsIgnoreCase("tvg-logo=\"\"")){
                             icon="noimage";

                         }else {
                             icon=removeExtra(iconBuilder,icon);

                         }

                   playlistItem.setItemIcon(icon);
                    playlistItem.setItemName(name);
                    playlistItem.setItemUrl(url);
                    playlistItem.setGroup(group);
                    playlistItem.setTvg_id(tvgid);
                   /* playlistItem.setItemIcon(icon);
                    if(first){
                        prevCategory=group;
                        M3UListConstants.channelCategories.add(group);
                        first=false;
                    }
                    if(prevCategory.equalsIgnoreCase(group)){



                    }else{
                            prevCategory=group;
                        M3UListConstants.channelCategories.add(group);


                    }*/
                    M3UListConstants.channelCategories.add(group);



                    m3UItemsList.add(playlistItem);

                } catch (Exception fdfd) {
fdfd.printStackTrace();

                }
                playlistItems.add(playlistItem);
            }
        }
        m3UPlaylist.setPlaylistItems(playlistItems);
        return m3UItemsList;
    }

    public String removeExtra(StringBuilder builder,String string){
        builder=builder.delete(0,string.indexOf("\""));
        builder.deleteCharAt(0);
        builder.deleteCharAt(builder.indexOf("\""));
        return builder.toString();


    }
}