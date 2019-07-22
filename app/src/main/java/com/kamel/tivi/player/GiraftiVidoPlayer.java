/*
package com.kamel.kameltv.player;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.kamel.kameltv.R;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;


public class GiraftiVidoPlayer extends AppCompatActivity {
  //  ProgressDialog progressDialog;
VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loadding..");
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        setContentView(R.layout.activity_girafti_vido_player);
        videoView=findViewById(R.id.surface);
        String url=getIntent().getExtras().getString("url");

        System.out.println(url);
        videoView.setVideoURI(Uri.parse(url));
     videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
         @Override
         public void onPrepared(MediaPlayer mp) {
progressDialog.dismiss();
             videoView.start();

         }
     });

     videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
         @Override
         public boolean onError(MediaPlayer mp, int what, int extra) {
             Toast.makeText(GiraftiVidoPlayer.this, "Please try again some network error!", Toast.LENGTH_SHORT).show();
             onBackPressed();

             return false;
         }
     });
    }

    @Override
    protected void onResume() {
        videoView.resume();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        videoView.pause();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        videoView.pause();
        super.onPause();
    }
}
*/
