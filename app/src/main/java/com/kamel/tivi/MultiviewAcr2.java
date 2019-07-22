package com.kamel.tivi;

import android.app.Dialog;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.kamel.tivi.channels.ChannelAdapter;
import com.kamel.tivi.channels.ChannelsModel;
import com.kamel.tivi.multiviewfragment.ExoMultiviewfragment;
import com.kamel.tivi.multiviewfragment.MultiviewPlayer;
import com.kamel.tivi.multiviewfragment.adapter.MultiViewPlayerAdapter;
import com.kamel.tivi.network.URLs;
import com.kamel.tivi.sqlitedatabase.DatabaseHelper;
import com.kamel.tivi.useaccount.UserAccount;

import java.util.List;

import static com.kamel.tivi.LiveTvActivity.channelAdapter;
import static com.kamel.tivi.LiveTvActivity.channelGrid;

public class MultiviewAcr2 extends AppCompatActivity {
    GridView gridView;
    MultiViewPlayerAdapter adapter;
    List<Fragment> playerlist;
    MultiviewPlayer player1;
    MultiviewPlayer player2;
    // ExoMultiviewfragment player3;
    // ExoMultiviewfragment player4;
    // IjkVideoView videoView;
    List<ChannelsModel> channelList;
    Button mute1, mute2,mute3,mute4;
    LinearLayout ppanel,p1linear, p2linear,p1panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiview_acr2);
        channelList= new DatabaseHelper(this).getAllChannel();

        mute1=findViewById(R.id.player1mute);
        mute2=findViewById(R.id.player2mute);
        //mute3=findViewById(R.id.player3mute);
        // mute4=findViewById(R.id.player4mute);

        p1linear=findViewById(R.id.p1linear);
        p2linear=findViewById(R.id.p2linear);
/*
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        videoView = (IjkVideoView) findViewById(R.id.ijkPlayer);

            String url = "http://cuthecord.ddns.net:25461/live/Test0987/Apk1234/124118.ts";
            // String url = "http://o6wf52jln.bkt.clouddn.com/演员.mp3";
            videoView.setVideoURI(Uri.parse(url));

        videoView.start();*/
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        p1linear.setLayoutParams(new LinearLayout.LayoutParams(width/2,height/2));
        p2linear.setLayoutParams(new LinearLayout.LayoutParams(width/2,height/2));
        p1panel=p1linear.findViewById(R.id.p1panel);


        player1= (MultiviewPlayer) getSupportFragmentManager().findFragmentById(R.id.fragment2);
        player2= (MultiviewPlayer) getSupportFragmentManager().findFragmentById(R.id.fragment3);
        //  player3= (ExoMultiviewfragment) getSupportFragmentManager().findFragmentById(R.id.fragment4);
        //  player4= (ExoMultiviewfragment) getSupportFragmentManager().findFragmentById(R.id.fragment5);
        //  player1.mMediaPlayer.setAspectRatio("3:2");
      /*  player1.SAMPLE_URL="http://cuthecord.ddns.net:25461/live/Test0987/Apk1234/124118.ts";
        player2.SAMPLE_URL="http://cuthecord.ddns.net:25461/live/Test0987/Apk1234/124118.ts";
        player3.SAMPLE_URL="http://cuthecord.ddns.net:25461/live/Test0987/Apk1234/124105.ts";
        player4.SAMPLE_URL="http://cuthecord.ddns.net:25461/live/Test0987/Apk1234/124105.ts";
        player1.fragStart();
        player2.fragStart();
        player3.fragStart();
        player4.fragStart();*/
        /*
    playerlist=new ArrayList<>();
    MultiviewPlayer player1=new MultiviewPlayer();

        MultiviewPlayer player2=new MultiviewPlayer();
        MultiviewPlayer player3=new MultiviewPlayer();
        MultiviewPlayer player4=new MultiviewPlayer();


        playerlist.add(player1);
        playerlist.add(player2);

        playerlist.add(player3);

        playerlist.add(player4);

        adapter=new MultiViewPlayerAdapter(this,playerlist);*/
/*        gridView.setDrawSelectorOnTop(true);
gridView.setAdapter(adapter);

gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        View view1= MultiViewConstants.layoutList.get(position);
        ImageView add=view.findViewById(R.id.add);
        if(add.getVisibility()==View.GONE){


        }else{


        }

    }
});*/


    }

    public void player1channel(View view){

        channelDialog2(player1);

        try {
           /// float vol=player1.player.getVolume();
            player1.mMediaPlayer.setVolume(70);
            player2.mMediaPlayer.setVolume(0);
        }catch (Exception ex){

            ex.printStackTrace();
        }
        // player3.player.setVolume(0.0f);
        //player4.player.setVolume(0.0f);
        mute1.setText("MUTE");
        mute2.setText("UNMUTE");
        //  mute3.setText("UNMUTE");
        //  mute4.setText("UNMUTE");


    }

    private void channelDialog(final ExoMultiviewfragment player){

        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.multiviewchanneldialog);
        channelGrid=dialog.findViewById(R.id.channelgrid);

        channelAdapter=new ChannelAdapter(this,channelList);
        channelGrid.setAdapter(channelAdapter);

        channelGrid.setDrawSelectorOnTop(true);
        channelGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                player.stop();
                String url= URLs.url+"/live/"+ UserAccount.userAccount.getUserName()+"/"+UserAccount.userAccount.getPassword()+"/"+channelList.get(position).streamId+".rtmp";
                player.testLink=url;
                player.initializePlayer();
                dialog.dismiss();
            }
        });

        try {
            dialog.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void player2channel(View view){
        channelDialog2(player2);

        try {
            //float vol=player2.player.getVolume();
            player1.mMediaPlayer.setVolume(0);
            player2.mMediaPlayer.setVolume(70);
            // player3.player.setVolume(0.0f);
            // player4.player.setVolume(0.0f);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        mute1.setText("UNMUTE");
        mute2.setText("MUTE");
        //mute3.setText("UNMUTE");
        //mute4.setText("UNMUTE");
    }

    private void channelDialog2(final MultiviewPlayer player) {

        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.multiviewchanneldialog);
        channelGrid=dialog.findViewById(R.id.channelgrid);

        channelAdapter=new ChannelAdapter(this,channelList);
        channelGrid.setAdapter(channelAdapter);

        channelGrid.setDrawSelectorOnTop(true);
        channelGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                player.stop();
                String url=URLs.url+"/live/"+ UserAccount.userAccount.getUserName()+"/"+UserAccount.userAccount.getPassword()+"/"+channelList.get(position).streamId+".rtmp";
                player.SAMPLE_URL=url;
                player.fragStart();
                dialog.dismiss();
            }
        });

        try {
            dialog.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        try{
            player1.stop();
            player2.stop();
            // player4.stop();
            // player3.stop();


        }catch (Exception ex){

            ex.printStackTrace();
        }
        super.onBackPressed();



    }

    /* public void player3channel(View view){
 try {
     player1.player.setVolume(0.0f);
     player2.player.setVolume(0.0f);
    // player3.player.setVolume(1.0f);
     //player4.player.setVolume(0.0f);
 }catch (Exception ex){

     ex.printStackTrace();
 }
         channelDialog(player3);
         mute1.setText("UNMUTE");
         mute2.setText("UNMUTE");
         mute3.setText("MUTE");
         mute4.setText("UNMUTE");

     }*/
   /* public void player4channel(View view){

        try {
            player1.player.setVolume(0.0f);
            player2.player.setVolume(0.0f);
           // player3.player.setVolume(0.0f);
           // player4.player.setVolume(1.0f);
        }catch (Exception ex){

            ex.printStackTrace();
        }
        mute1.setText("UNMUTE");
        mute2.setText("UNMUTE");
        mute3.setText("UNMUTE");
        mute4.setText("MUTE");
        channelDialog(player4);


    }*/
    public void mute1(View view){

        if(mute1.getText().equals("UNMUTE")) {
            mute1.setText("MUTE");
            mute2.setText("UNMUTE");
            // mute3.setText("UNMUTE");
            //mute4.setText("UNMUTE");
            try{
                float vol=player2.mMediaPlayer.getVolume();
                player1.mMediaPlayer.setVolume(50);
                player2.mMediaPlayer.setVolume(0);
                // player3.player.setVolume(0.0f);
                //  player4.player.setVolume(0.0f);

            }catch (Exception ex){ex.printStackTrace();}

        }else {
            mute1.setText("UNMUTE");
            mute2.setText("UNMUTE");
            // mute3.setText("UNMUTE");
            ////  mute4.setText("UNMUTE");
            try{

                player1.mMediaPlayer.setVolume(0);
                player2.mMediaPlayer.setVolume(0);
                // player3.player.setVolume(0.0f);
                // player4.player.setVolume(0.0f);
            }catch (Exception ex){ex.printStackTrace();}

        }


    }
    public void mute2(View view){
        if(mute2.getText().equals("UNMUTE")) {
            mute1.setText("UNMUTE");
            mute2.setText("MUTE");
            //  mute3.setText("UNMUTE");
            // mute4.setText("UNMUTE");
            try{
                float vol=player2.mMediaPlayer.getVolume();
                player1.mMediaPlayer.setVolume(0);
                player2.mMediaPlayer.setVolume(50);
                // player3.player.setVolume(0.0f);
                // player4.player.setVolume(0.0f);
            }catch (Exception ex){ex.printStackTrace();}

        }else {

            mute1.setText("UNMUTE");
            mute2.setText("UNMUTE");
            // mute3.setText("UNMUTE");
            // mute4.setText("UNMUTE");
            try {
                player1.mMediaPlayer.setVolume(0);
                player2.mMediaPlayer.setVolume(0);
                // player3.player.setVolume(0.0f);
                //  player4.player.setVolume(0.0f);
            }catch (Exception ex){ex.printStackTrace();}

        }


    }
    public void mute3(View view){
        if(mute3.getText().equals("UNMUTE")) {

            mute1.setText("UNMUTE");
            mute2.setText("UNMUTE");
            //   mute3.setText("MUTE");
            // mute4.setText("UNMUTE");
            player1.mMediaPlayer.setVolume(0);
            player2.mMediaPlayer.setVolume(0);
            // player3.player.setVolume(1.0f);
            //player4.player.setVolume(0.0f);
        }else {

            mute1.setText("UNMUTE");
            mute2.setText("UNMUTE");
            //  mute3.setText("UNMUTE");
            // mute4.setText("UNMUTE");
            try{
                player1.mMediaPlayer.setVolume(0);
                player2.mMediaPlayer.setVolume(0);
                //player3.player.setVolume(0.0f);
                //player4.player.setVolume(0.0f);
            }catch (Exception ex){ex.printStackTrace();}
        }


    }
    public void mute4(View view){
        if(mute4.getText().equals("UNMUTE")) {

            mute1.setText("UNMUTE");
            mute2.setText("UNMUTE");
            // mute3.setText("UNMUTE");
            // mute4.setText("MUTE");
            try{
                player1.mMediaPlayer.setVolume(0);
                player2.mMediaPlayer.setVolume(0);
                // player3.player.setVolume(0.0f);
                //player4.player.setVolume(1.0f);
            }catch (Exception ex){ex.printStackTrace();}

        }else if(mute4.getText().equals("UNMUTE")){

            mute1.setText("UNMUTE");
            mute2.setText("UNMUTE");
            // mute3.setText("UNMUTE");
            //  mute4.setText("UNMUTE");

            try{        player1.mMediaPlayer.setVolume(0);
                player2.mMediaPlayer.setVolume(0);
                //player3.player.setVolume(0.0f);
                // player4.player.setVolume(0.0f);
            }catch (Exception ex){ex.printStackTrace();}
        }


    }


}
