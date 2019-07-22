package com.kamel.tivi.multiviewfragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
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

public class ExoMultiviewfragment extends Fragment {
    private VideoView videoView;
    boolean isselected = false;
    public SimpleExoPlayer player;
    private PlayerView playerView;

    private ProgressDialog progressDialog;
    public String testLink;
    private boolean pauseorstop;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.multiviewfragment,container,false);





        playerView = view.findViewById(R.id.video_view);

        return view;
    }

    public void initializePlayer() {

        try {

        showProgress();

        final LoadControl loadControl = new DefaultLoadControl();

        player = ExoPlayerFactory.newSimpleInstance(getActivity());

        player.addListener(new Player.EventListener() {

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                    if(!isLoading)
                hideProgress();
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {



                if (Player.STATE_ENDED == playbackState) {
                    player.setPlayWhenReady(true);
                    loadControl.shouldContinueLoading(22l, DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS);


                } else if(playbackState==Player.STATE_READY){
                    hideProgress();

                }else {



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
                hideProgress();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
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
        playerView.setUseController(false);
        playerView.setPlayer(player);

        Uri uri = Uri.parse(testLink);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, false, true);


        }catch (Exception ex){

            ex.printStackTrace();
        }
    }



    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);


    }


    public void stop() {
        if(player!=null)
    player.stop();

    }

    public void showProgress(){
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait.");
        progressDialog.setMessage("Loading channel data..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

    }
    public void hideProgress(){

        progressDialog.dismiss();

    }
}
