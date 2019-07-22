package com.kamel.tivi.player.exoplayer;


import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.kamel.tivi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExoPlayerFragment extends Fragment implements MediaPlayer.OnPreparedListener   {
    private long exitTime = 0;
    private VideoView videoView;
    private ProgressDialog progressDialog;
    String url;
    public ExoPlayerFragment() {
         // Required empty public constructor



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exo_player, container, false);

        if(getArguments()!=null) {

       String url = getArguments().getString("url");
       // Inflate the layout for this fragment
            System.out.println(url);
       videoView = (VideoView) view.findViewById(R.id.video_view);
       videoView.setOnPreparedListener((MediaPlayer.OnPreparedListener) this);
       videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
           @Override
           public boolean onError(MediaPlayer mp, int what, int extra) {
               Toast.makeText(ExoPlayerFragment.this.getContext(), "Please try again later!", Toast.LENGTH_LONG).show();

               return false;
           }
       });

       final Uri videoUri = Uri.parse(url);

       videoView.setVideoURI(videoUri);
   }
        System.out.println("IN fragment ex");

        return view;
    }
    @Override
    public void onPause() {
        super.onPause();
        videoView.pause();
        videoView.setVisibility(View.GONE );
    }

    @Override
    public void onResume() {
        super.onResume();
        videoView.resume();
        videoView.setVisibility(View.VISIBLE);
    }

    public void closeStreaming() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {

            //  exitTime = System.currentTimeMillis();
        } else {
//            finish();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        hideProgress();
        //Starts the video playback as soon as it is ready
        videoView.start();
    }


    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
