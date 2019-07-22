/*
package com.kamel.kameltv.player.xplyer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.GridView;

import com.kamel.kameltv.LiveTvActivity;
import com.kamel.kameltv.R;
import hb.xvideoplayer.MxVideoPlayer;
import hb.xvideoplayer.MxTvPlayerWidget;
import hb.xvideoplayer.MxVideoPlayerWidget;

public class XVideoPlayer extends AppCompatActivity {
    MxVideoPlayerWidget videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xvideo_player);
        GridView gridView;
String url=getIntent().getExtras().getString("url");
     // int id=getIntent().getExtras().getInt("id");


        videoView=findViewById(R.id.video_view);
    //  gridView=findViewById(R.id.moviegrid);
  */
/*      if(id==1){

     gridView.setAdapter(LiveTvActivity.channelAdapter);

        }*//*


        videoView.autoStartPlay(url, MxVideoPlayer.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN, "video name");
       videoView.startWindowFullscreen();


    }

    @Override
    protected void onPause() {

        super.onPause();
        MxVideoPlayer.releaseAllVideos();

    }

    @Override
    public void onBackPressed() {
        if (MxVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
*/
