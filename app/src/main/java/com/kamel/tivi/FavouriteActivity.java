package com.kamel.tivi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.VideoView;

import com.kamel.tivi.channels.ChannelAdapter;
import com.kamel.tivi.channels.ChannelsModel;
import com.kamel.tivi.channels.favouritechannels.FavouriteChannels;
import com.kamel.tivi.epg.model.constants.Constants;
import com.kamel.tivi.network.URLs;
//import com.kamel.kameltv.player.xplyer.XVideoPlayer;
//import com.kamel.kameltv.player.ijk.IJKVidoPlayear;
import com.kamel.tivi.player.vlc.VlcNew;
import com.kamel.tivi.player.vlc.VlcSetup;
import com.kamel.tivi.sqlitedatabase.DatabaseHelper;
import com.kamel.tivi.sqlitedatabase.DatabaseHelperHours;
import com.kamel.tivi.sqlitedatabase.DatabaseHelperSway;
import com.kamel.tivi.useaccount.UserAccount;

import java.util.List;

public class FavouriteActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener{
    static GridView channelGrid;
    private static ChannelAdapter channelAdapter;
    private static List<ChannelsModel> channelList;

    SearchView channelSearch;
    private long exitTime = 0;
    private VideoView videoView;
    private ProgressDialog progressDialog;
    String url;
    static Context context;
    private int currentPositoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       if(URLs.serverName.equalsIgnoreCase("PiTv")) {
           channelList = new DatabaseHelper(this).getAllChannel();
       }else if(URLs.serverName.equalsIgnoreCase("HoursTv")){
           channelList = new DatabaseHelperHours(this).getAllChannel();


       }else{

           channelList = new DatabaseHelperSway(this).getAllChannel();

       }
        if(channelList.size()==0){
            Toast.makeText(this, "No channel in favourites", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        setContentView(R.layout.activity_favourite);
        context=this;
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
        channelGrid=findViewById(R.id.channelgrid);

        channelAdapter=new ChannelAdapter(this,channelList);
        channelGrid.setAdapter(channelAdapter);

        channelGrid.setDrawSelectorOnTop(true);


        registerForContextMenu(channelGrid);
try{



        channelGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                     currentPositoin=position;
                com.kamel.tivi.epg.model.constants.Constants.channelAdapters=channelAdapter;
                Constants.streamid=channelList.get(position).getStreamId();

                String url= URLs.url+"/live/"+ UserAccount.userAccount.getUserName()+"/"+UserAccount.userAccount.getPassword()+"/"+channelList.get(position).getStreamId()+".ts";
                    FavouriteActivity.this.url=url;
                final Uri videoUri = Uri.parse(FavouriteActivity.this.url);
             fullScreen(null);
              ///  videoView.setVideoURI(videoUri);

            }
        });

}catch (Exception ex){
    Toast.makeText(this, "No channel in favourites", Toast.LENGTH_SHORT).show();

    onBackPressed();

}

    }


    @Override
    protected void onPause() {
        super.onPause();
        //videoView.pause();
     //   videoView.setVisibility(View.GONE );
    }

    @Override
    protected void onResume() {
        super.onResume();
     //   videoView.resume();
      //  videoView.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void closeStreaming() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {

            //  exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // hideProgress();
        //Starts the video playback as soon as it is ready
      //  videoView.start();
    }

    public void showProgress() {
        progressDialog = new ProgressDialog(FavouriteActivity.this);
        progressDialog = ProgressDialog.show(FavouriteActivity.this, "Please Wait",
                "Loading...", true);
    }

    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }



    public void fullScreen(View view){
        VlcSetup vlcSetup=new VlcSetup(channelList,currentPositoin);
        Intent intent=new Intent(FavouriteActivity.this, VlcNew.class);
        intent.putExtra("url",FavouriteActivity.this.url);

        startActivity(intent);

    }
    public static void updateChannelList(List<ChannelsModel> list){
        channelList=list;
        ChannelAdapter channelAdapter=new ChannelAdapter(context,list);
        channelGrid.setAdapter(channelAdapter);
        System.out.println("INUPDATE"+list.size());
        channelAdapter.notifyDataSetChanged();



    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chanelcontextmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // below variable info contains clicked item info and it can be null; scroll down to see a fix for it
        //  ChannelAdapter info = (ChannelAdapter) item.getMenuInfo();
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

    }

}
