/*
package com.kamel.kameltv.player.ijk;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;

import com.kamel.kameltv.R;
import com.playerlibrary.view.IjkVideoView;
import com.playerlibrary.view.IjkVideoView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class IJKVidoPlayear extends AppCompatActivity implements MediaController.MediaPlayerControl {
IjkVideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_ijkvido_playear);


        String url=getIntent().getExtras().getString("url");
       videoView=findViewById(R.id.ijkVideoView);
       //videoView.mediPla(this);
       videoView.setVideoURI(Uri.parse(url));
       videoView.start();

    }

    @Override
    protected void onResume() {
        videoView.resume();

        super.onResume();
    }

    @Override
    protected void onPause() {
        videoView.pause();
        super.onPause();
    }

    @Override
    public void onBackPressed() {

        videoView.stopPlayback();
        super.onBackPressed();
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
*/
