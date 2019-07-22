package com.kamel.tivi.player.exoplayer;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.kamel.tivi.R;

import com.google.android.exoplayer2.ExoPlayerFactory;

public class ExoPlayerActivity extends AppCompatActivity  {
    private VideoView videoView;

    boolean isselected = false;
    private SimpleExoPlayer player;
    private PlayerView playerView;

    private ProgressDialog progressDialog;
    String testLink;
    private boolean pauseorstop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_exo_player);
        showProgress();

        testLink = getIntent().getStringExtra("url");



        playerView = findViewById(R.id.video_view);

         initializePlayer();


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
       // videoView.resume();
        //videoView.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        player.stop();
        super.onBackPressed();
    }





    public void showProgress() {
        progressDialog = new ProgressDialog(ExoPlayerActivity.this);
        progressDialog = ProgressDialog.show(ExoPlayerActivity.this, "Please Wait",
                "Loading...", true);
    }

    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void initializePlayer() {

        final LoadControl loadControl = new DefaultLoadControl();

        player = ExoPlayerFactory.newSimpleInstance(this);

        player.addListener(new Player.EventListener() {

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (Player.STATE_ENDED == playbackState) {
                    player.setPlayWhenReady(true);
                    loadControl.shouldContinueLoading(22l, DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS);


                } else {



                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });

        player.setPlayWhenReady(true);
        playerView.setPlayer(player);

        Uri uri = Uri.parse(testLink);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, false, true);
        hideProgress();


    }



    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       if(KeyEvent.KEYCODE_DPAD_RIGHT==keyCode ){
           player.seekTo(player.getCurrentPosition()+15000);

       }else if(KeyEvent.KEYCODE_DPAD_LEFT ==keyCode){

           player.seekTo(player.getCurrentPosition()-15000);

       }else if(KeyEvent.KEYCODE_DPAD_CENTER ==keyCode || KeyEvent.KEYCODE_ENTER==keyCode){
        //if(player.is)

           /*if(pauseorstop){
               player.seekTo(playerPosition);
               player.selectTrack(DemoPlayer.TYPE_VIDEO, 0);
               player.selectTrack(DemoPlayer.TYPE_AUDIO, 0);

           }else {


           }*/
        playerView.showController();

       }


        return super.onKeyDown(keyCode, event);
    }
}
