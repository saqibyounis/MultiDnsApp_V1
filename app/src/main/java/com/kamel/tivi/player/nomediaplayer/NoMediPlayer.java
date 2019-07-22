package com.kamel.tivi.player.nomediaplayer;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.novoda.noplayer.ContentType;
import com.novoda.noplayer.NoPlayer;
import com.kamel.tivi.R;
import com.novoda.noplayer.Options;
import com.novoda.noplayer.OptionsBuilder;
import com.novoda.noplayer.PlayerBuilder;
import com.novoda.noplayer.PlayerView;

public class NoMediPlayer extends AppCompatActivity {
    private static  String URI_VIDEO_WIDEVINE_EXAMPLE_MODULAR_MPD = "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd";
    private static final String EXAMPLE_MODULAR_LICENSE_SERVER_PROXY = "https://proxy.uat.widevine.com/proxy?provider=widevine_test";
    private static final int HALF_A_SECOND_IN_MILLIS = 500;
    private static final int TWO_MEGABITS = 2000000;
    private static final int MAX_VIDEO_BITRATE = 800000;

    private NoPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_medi_player);
      String url=getIntent().getExtras().getString("url");
      URI_VIDEO_WIDEVINE_EXAMPLE_MODULAR_MPD=url;
        PlayerView playerView = findViewById(R.id.player_view);
        DataPostingModularDrm drmHandler = new DataPostingModularDrm(EXAMPLE_MODULAR_LICENSE_SERVER_PROXY);

        NoPlayer player = new PlayerBuilder()
                .withWidevineModularStreamingDrm(drmHandler)
                .withDowngradedSecureDecoder()
                .withUserAgent("Android/Linux")
                .allowCrossProtocolRedirects()
                .build(this);


        player.getListeners().addDroppedVideoFrames(new NoPlayer.DroppedVideoFramesListener() {
            @Override
            public void onDroppedVideoFrames(int droppedFrames, long elapsedMsSinceLastDroppedFrames) {
                Log.v(getClass().toString(), "dropped frames: " + droppedFrames + " since: " + elapsedMsSinceLastDroppedFrames + "ms");
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        Uri uri = Uri.parse(URI_VIDEO_WIDEVINE_EXAMPLE_MODULAR_MPD);
        Options options = new OptionsBuilder()
                .withContentType(ContentType.DASH)
                .withMinDurationBeforeQualityIncreaseInMillis(HALF_A_SECOND_IN_MILLIS)
                .withMaxInitialBitrate(TWO_MEGABITS)
                .withMaxVideoBitrate(getMaxVideoBitrate())
                .build();

    }

    private int getMaxVideoBitrate() {
      /*  if (hdSelectionCheckBox.isChecked()) {
            return Integer.MAX_VALUE;
        }*/
        return MAX_VIDEO_BITRATE;
    }
    @Override
    protected void onStop() {
       // demoPresenter.stopPresenting();
        super.onStop();
    }
}
