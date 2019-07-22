package com.kamel.tivi.player.betterplayer;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.halilibo.bettervideoplayer.BetterVideoPlayer;
import com.kamel.tivi.R;

public class BetterPlayerActivity extends AppCompatActivity {
     BetterVideoPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_better_player);
    String url=getIntent().getExtras().getString("url");

    player=findViewById(R.id.multiplayer);
    player.setSource(Uri.parse(url));
    player.setAutoPlay(true);

     }

    @Override
    public void onBackPressed() {
           player.stop();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        player.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        player.reset();
        super.onResume();
    }
}
