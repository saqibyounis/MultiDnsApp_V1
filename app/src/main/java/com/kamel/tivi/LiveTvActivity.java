package com.kamel.tivi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.common.collect.Maps;
import com.kamel.tivi.channelcategory.constants.Constants;
import com.kamel.tivi.channels.ChannelAdapter;
import com.kamel.tivi.channels.ChannelsModel;
import com.kamel.tivi.channels.favouritechannels.FavouriteChannels;
//import com.kamel.kameltv.player.GiraftiVidoPlayer;
import com.kamel.tivi.epg.EPG;
import com.kamel.tivi.epg.domain.EPGChannel;
import com.kamel.tivi.epg.domain.EPGEvent;
import com.kamel.tivi.epg.epgadapter.EpgAdapterForLive;
import com.kamel.tivi.multiviewfragment.MultiviewPlayer;
import com.kamel.tivi.network.URLs;
//import com.kamel.kameltv.player.ijk.IJKVidoPlayear;
//import com.kamel.kameltv.player.xplyer.XVideoPlayer;
//import com.kamel.kameltv.player.ijk.IJKVidoPlayear;
import com.kamel.tivi.player.vlc.VlcNew;
import com.kamel.tivi.player.vlc.VlcSetup;
import com.kamel.tivi.useaccount.UserAccount;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.playerlibrary.view.IjkVideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;

public class LiveTvActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener{
         static   GridView channelGrid;
           public static ChannelAdapter channelAdapter;
           public static List<ChannelsModel> channelList;
           LinearLayout channelLinear;
           public MultiviewPlayer player;
    public static int postionForChannelAdapter=-1;

    public static String SAMPLE_URL = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_640x360.m4v";
       public GridView epgGrid;
    private static EPG epg;
          public int currentPlayingIdex=-1;
     static HashMap<EPGChannel, List<EPGEvent>> result;
     List<EPGEvent> epgEvents=new ArrayList<>();
           SearchView channelSearch;
    private long exitTime = 0;
  //  private IjkVideoView videoView;
    private ProgressDialog progressDialog;
    String url;
   static Context context;
    private AudioManager mAudioManager;
    LinearLayout playerlinear;
      EpgAdapterForLive epgAdapter;
    private AdapterView.AdapterContextMenuInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        showProgress();

        setContentView(R.layout.activity_live_tv);
        epgGrid=findViewById(R.id.epg);
        playerlinear=findViewById(R.id.playerlinear);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x/3;
        int height = size.y/3;

        playerlinear.setLayoutParams(new LinearLayout.LayoutParams(size.x/3,size.y/3));
        player= (MultiviewPlayer) getSupportFragmentManager().findFragmentById(R.id.multifrag);
        player.scalx=3;
        player.scaly=3;
       // videoView=findViewById(R.id.video_view);
    context=this;
          channelLinear=findViewById(R.id.cl);
        channelGrid=findViewById(R.id.channelgrid);
         channelLinear.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT));



        if(Constants.channelCategories.get(0).getChanneles()!=null&&Constants.channelCategories.get(0).getChanneles().size()>0) {
            ////////////////////////////
         initView();

        }else{
            String url = URLs.url +"/player_api.php?username=" + UserAccount.userAccount.getUserName() + "&password=" + UserAccount.userAccount.getPassword() + "&action=get_live_streams";
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONArray array = new JSONArray(new String(responseBody));
                        for (int i = 0; i < array.length(); i++) {
                            String name = array.getJSONObject(i).getString("name");
                            String streamId = array.getJSONObject(i).getString("stream_id");
                            String streamIcon = array.getJSONObject(i).getString("stream_icon");
                            String epgChannelId = array.getJSONObject(i).getString("epg_channel_id");
                            String categoryId = array.getJSONObject(i).getString("category_id");
                            System.out.println("Stream id "+streamId);
                            for (int j = 0; j < Constants.channelCategories.size(); j++) {
                                if (Constants.channelCategories.get(j).getCategoryId().equalsIgnoreCase(categoryId)) {
                                    Constants.channelCategories.get(j).getChanneles().add(new ChannelsModel(name, streamId, epgChannelId, categoryId, "", streamIcon));


                                }


                            }
                            initView();


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });


        }
        //////////////////



    }
    public void initView(){
        result = Maps.newLinkedHashMap();

        hideProgress();
        //mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        channelSearch=findViewById(R.id.channelsearch);
        channelSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                channelAdapter.filter(newText);


                return false;
            }
        });
        channelSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){

                    channelAdapter.filter("");

                }
            }
        });

        channelList=Constants.channelCategories.get(0).getChanneles();

        channelAdapter=new ChannelAdapter(this,channelList);
        channelGrid.setAdapter(channelAdapter);

        channelGrid.setDrawSelectorOnTop(true);

//         url=URLs.url +"/live/"+UserAccount.userAccount.getUserName()+"/"+UserAccount.userAccount.getPassword()+"/"+channelList.get(0).streamId+".rtmp";
        /*player.SAMPLE_URL=url;
        player.fragStart();*/




        registerForContextMenu(channelGrid);



        channelGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                result.clear();

                postionForChannelAdapter = position;

                updateChannelCategory();

                showProgress();
              String url=URLs.url +"/live/"+UserAccount.userAccount.getUserName()+"/"+UserAccount.userAccount.getPassword()+"/"+channelList.get(position).getStreamId()+".ts";
              player.SAMPLE_URL=url;
              player.stop();
              player.fragStart();
                LiveTvActivity.this.url=url;
             epgEvents.clear();
                final Uri videoUri = Uri.parse(url);
                com.kamel.tivi.epg.model.constants.Constants.streamid=channelList.get(position).getStreamId();

                com.kamel.tivi.epg.model.constants.Constants.size=channelList.size();
                AsyncHttpClient client=new AsyncHttpClient();
                String epginfourl = URLs.url+"/player_api.php?username="+UserAccount.userAccount.getUserName()+"&password="+UserAccount.userAccount.getPassword()+"&action=get_short_epg&stream_id="+channelList.get(position).streamId;
               // final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");


                //simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH.mm a EEE MM/dd");
                long rawOffset = (long) (TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings());
                long currentTimeMillis = System.currentTimeMillis() + rawOffset;


                final long wrongMedialaanTime = (long) (TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings());


                System.out.println(epginfourl);
                client.get(epginfourl, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            long rawOffset = (long) (TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings());
                                StringBuilder startTime;
                            try {
                                JSONObject object=new JSONObject(new String(responseBody));
                                JSONArray array = object.getJSONArray("epg_listings");

                                for (int i = 0; i < array.length(); i++) {
                                    String id = array.getJSONObject(i).getString("id");
                                    String epgid = array.getJSONObject(i).getString("epg_id");
                                    String title = array.getJSONObject(i).getString("title");
                                    String start = array.getJSONObject(i).getString("start");
                                    String end = array.getJSONObject(i).getString("end");
                                    String description = array.getJSONObject(i).getString("description");

                                    String start_time = array.getJSONObject(i).getString("start_timestamp");
                                    String stop_time = array.getJSONObject(i).getString("stop_timestamp");
                                    int duration = Integer.parseInt(start_time) - Integer.parseInt(stop_time);
                                    String settime = simpleDateFormat.format(new Date(((Long.parseLong(start_time) * 1000)+3600000)));
                                    long parseLong = Long.parseLong(stop_time);
                                    Long.signum(parseLong);
                                    String timeto = simpleDateFormat.format(new Date(((parseLong * 1000) ) ));
                                    System.out.println(title);
                                    long parseLong1 =((Long.parseLong(start_time) * 1000)+rawOffset)+3600000;// ((Long.parseLong(((ChannelModels) VlcPlayerActivity.this.epgList.get(0)).getStart_timestamp()) * 1000) + rawOffset) + 3600000;
                                    long parseLong2 = ((Long.parseLong(stop_time) * 1000)+rawOffset)+3600000;
                                         settime=simpleDateFormat.format(new Date(parseLong1 - rawOffset));

                                     startTime=new StringBuilder(start);
                                     startTime=startTime.delete(0,start.indexOf(" "));
                                    EPGEvent epgEvent = new EPGEvent((Long.parseLong(start_time) * 1000) + 3600000, (parseLong * 1000) + 3600000, new String(Base64.decode(title, 0)), startTime.toString());
                                    epgEvents.add(epgEvent);


                                }

                                result.put(new EPGChannel(channelList.get(position).getLogo(), channelList.get(position).getName(), ""), epgEvents);

                            } catch (Exception ex) {
                            }





                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        setEpgData();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

                channelGrid.setSelection(position);
                com.kamel.tivi.epg.model.constants.Constants.position=position;
              //  fullScreen(null,position);
                if(currentPlayingIdex==-1){

                    currentPlayingIdex=position;

                }else if(currentPlayingIdex==position)
                {
                      fullScreen(null,position);

                }else{
                    currentPlayingIdex=position;

                }





            }
        });

hideProgress();

    }

    private void updateChannelCategory() {
    channelAdapter=new ChannelAdapter(this,channelList);
    channelGrid.setAdapter(channelAdapter);
     channelAdapter.notifyDataSetChanged();
     channelGrid.setSelection(postionForChannelAdapter);

    }

    private void setEpgData() {
        System.out.println("events size "+epgEvents.size());
        int currentHour;

        for (EPGEvent epgEvent : epgEvents) {


        }


           epgAdapter=new EpgAdapterForLive(context,epgEvents);
           epgGrid.setAdapter(epgAdapter);
           epgAdapter.notifyDataSetChanged();

           hideProgress();
        ///new LiveTvActivity.AsyncLoadEPGData(epg).execute();



    }


    @Override
    protected void onPause() {
        super.onPause();
        player.stop();
       // videoView.pause();
       // videoView.setVisibility(View.GONE );
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.stop();
      // // videoView.resume();
      //  videoView.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
      /*  for (ChannelCategory category:  Constants.channelCategories) {
            category.getChanneles().clear();
        }*/

          player.stop();
        super.onBackPressed();
    }



    @Override
    public void onPrepared(MediaPlayer mp) {
       hideProgress();
        //Starts the video playback as soon as it is ready
       // videoView.start();
    }

    public void showProgress() {
        progressDialog = new ProgressDialog(LiveTvActivity.this);
        progressDialog = ProgressDialog.show(LiveTvActivity.this, "Please Wait",
                "Loading...", true);
    }

    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public void fullScreen(View view,int position){
      /*  hideProgress();
         com.kamel.tivi.epg.model.constants.Constants.channelAdapters=channelAdapter;
        com.kamel.tivi.epg.model.constants.Constants.channels=channelList;

        SAMPLE_URL= LiveTvActivity.this.url;
             Intent intent=new Intent(LiveTvActivity.this, VLCActivity.class);
          intent.putExtra("url",LiveTvActivity.this.url);
          //intent.putExtra("index",position);
          startActivity(intent);*/
        VlcSetup vlcSetup=new VlcSetup(channelList,position);
        Intent intent=new Intent(LiveTvActivity.this, VlcNew.class);
           startActivity(intent);



    }
public static void updateChannelList(List<ChannelsModel> list){
       channelList=list;
        channelAdapter=new ChannelAdapter(context,list);
       channelGrid.setAdapter(channelAdapter);
    System.out.println("INUPDATE"+list.size());
        channelAdapter.notifyDataSetChanged();



}
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        try {
            // Casts the incoming data object into the type for AdapterView objects.
            info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        } catch (ClassCastException e) {
            // If the menu object can't be cast, logs an error.
            Log.e("Menu", "bad menuInfo", e);
            return;
        }        inflater.inflate(R.menu.chanelcontextmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // below variable info contains clicked item info and it can be null; scroll down to see a fix for it
      //  ChannelAdapter info = (ChannelAdapter) item.getMenuInfo();

      try {

        switch (item.getItemId()) {
            case R.id.addfavorite:

                //hidTesteItem(info.position);
                ChannelsModel m3UItem= (ChannelsModel) channelGrid.getSelectedItem();
                if(URLs.serverName.equalsIgnoreCase("PiTv")){
                    FavouriteChannels.databaseHelper.addChannel(m3UItem);
                    Toast.makeText(context, m3UItem.getName()+" Favourite Added!"+channelGrid.getCheckedItemPosition(), Toast.LENGTH_SHORT).show();

                }else if(URLs.serverName.equalsIgnoreCase("HoursTv")){
                    FavouriteChannels.databaseHelperHours.addChannel(m3UItem);
                    Toast.makeText(context, m3UItem.getName()+" Favourite Added!"+channelGrid.getCheckedItemPosition(), Toast.LENGTH_SHORT).show();

                }else {

                    FavouriteChannels.databaseHelperSway.addChannel(m3UItem);
                    Toast.makeText(context, m3UItem.getName()+" Favourite Added!"+channelGrid.getCheckedItemPosition(), Toast.LENGTH_SHORT).show();


                }

                return true;
            case R.id.removefavorite:
                ChannelsModel m3UItemr= (ChannelsModel) channelGrid.getSelectedItem();

                if(URLs.serverName.equalsIgnoreCase("PiTv")){
                    FavouriteChannels.databaseHelper.deletechannel(m3UItemr);
                    Toast.makeText(context, m3UItemr.getName()+" Removed from favourite!", Toast.LENGTH_SHORT).show();


                }else if(URLs.serverName.equalsIgnoreCase("HoursTv")){
                    FavouriteChannels.databaseHelperHours.deletechannel(m3UItemr);
                    Toast.makeText(context, m3UItemr.getName()+" Removed from favourite!", Toast.LENGTH_SHORT).show();

                }else {

                    FavouriteChannels.databaseHelperSway.deletechannel(m3UItemr);
                    Toast.makeText(context, m3UItemr.getName()+" Removed from favourite!", Toast.LENGTH_SHORT).show();

                }
                return true;

            default:
                return super.onContextItemSelected(item);
        }
      }catch (Exception ex){
          ex.printStackTrace();
          Toast.makeText(context, "Unable to add this into favorite", Toast.LENGTH_SHORT).show();


      }
return super.onContextItemSelected(item);
    }

    public void playChannel(String link){


    }




    public void left(View view){

        epg.scrollTo(epg.getScrollX(),epg.getScrollY());

    }
    public void right(View view){}

    public void setTime(){


        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String formattedDate=dateFormat.format(date);
    }

}
