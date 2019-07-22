package com.kamel.tivi.multiviewfragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kamel.tivi.R;
import com.khizar1556.mkvideoplayer.IjkVideoView;

public class IJKPlayerMultiview extends Fragment {
    public IjkVideoView videoView;
    public  String path;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_ijkvido_playear,container,false);
      videoView=view.findViewById(R.id.uvv_vido);

     return view;
    }

    public void startPlay(){

        videoView.setVideoURI(Uri.parse(path));
        videoView.start();
    }

    public void stop(){
        videoView.release(true);

    }
}

