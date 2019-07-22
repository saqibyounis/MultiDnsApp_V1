/*
package com.kamel.kameltv;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player.DefaultEventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline.Window;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource.Factory;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
*/
/*
import com.streamztv.tv.adapter.ChannelAdapter;
import com.streamztv.tv.adapter.MainMenuAdapter;
import com.streamztv.tv.main.TvGuideActivity.GROUP_CHANNEL_KIND;
import com.streamztv.tv.model.ArrayChannelItem;
import com.streamztv.tv.model.ChannelItem;
import com.streamztv.tv.model.LiveCategoryInfo;
import com.streamztv.tv.utils.AppConstants;
import com.streamztv.tv.utils.Utils;
*//*

import java.util.ArrayList;
import java.util.Iterator;

public class MultiviewActivity extends Activity {
    private static final String KEY_PLAY_WHEN_READY = "play_when_ready";
    private static final String KEY_POSITION = "position";
    private static final String KEY_WINDOW = "window";
    private BandwidthMeter bandwidthMeter;
    private int curPos;
    private int currentWindow;
    private int[] errorIndex = new int[]{0, 0, 0, 0};
    private TrackGroupArray lastSeenTrackGroupArray;
    private LinearLayout[] linear_parent;
    private LinearLayout[] linear_player;
    private RelativeLayout[] linear_rplayer;
 //   private ArrayChannelItem m_arrayChannelItem = new ArrayChannelItem();
    private String m_currentCategory;
    private OnKeyListener m_listenerKey;
    private ProgressBar[] m_progressBar;
    private Factory mediaDataSourceFactory;
    private boolean playWhenReady;
    private long playbackPosition;
    private SimpleExoPlayer[] player;
    private ImageView player1Btn;
    private ImageView player2Btn;
    private ImageView player3Btn;
    private ImageView player4Btn;
    private int[] playerStatus;
    private PlayerView[] playerView;
    private Handler reconnectHandler = new Handler();
    private Runnable reconnectRunnable = new Runnable() {
        public void run() {
            MultiviewActivity.this.currentWindow = -1;
            MultiviewActivity.this.playbackPosition = -1;
            for (int i = 0; i < 4; i++) {
                if (MultiviewActivity.this.errorIndex[i] == 1) {
                    MultiviewActivity.this.initializePlayer(i);
                }
            }
        }
    };
    private boolean[] shouldAutoPlay;
    private String[] streamUrl;
    private DefaultTrackSelector trackSelector;
    private Window window;

    private class PlayerEventListener extends DefaultEventListener {
        public int playerIndex = -1;

        public PlayerEventListener(int i) {
            this.playerIndex = i;
        }

        public void onPlayerStateChanged(boolean z, int i) {
            if (i == 2) {
                MultiviewActivity.this.m_progressBar[this.playerIndex].setVisibility(View.VISIBLE);
            }
            if (i == 3) {
                MultiviewActivity.this.m_progressBar[this.playerIndex].setVisibility(View.GONE);
                MultiviewActivity.this.errorIndex[this.playerIndex] = 0;
            }
        }

        public void onPlayerError(ExoPlaybackException exoPlaybackException) {
            MultiviewActivity.this.errorIndex[this.playerIndex] = 1;
        */
/*    new Thread(new Runnable() {
                public void run() {
                    if (Utils.getConnectivityStatus(MultiviewActivity.this) != Utils.TYPE_NOT_CONNECTED) {
                        MultiviewActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                MultiviewActivity.this.m_progressBar[PlayerEventListener.this.playerIndex].setVisibility(8);
                            }
                        });
                        return;
                    }
                    MultiviewActivity.this.player[PlayerEventListener.this.playerIndex].stop();
                    MultiviewActivity.this.playerView[PlayerEventListener.this.playerIndex].setVisibility(4);
                    MultiviewActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            MultiviewActivity.this.m_progressBar[PlayerEventListener.this.playerIndex].setVisibility(8);
                        }
                    });
                }
            }).start();
            MultiviewActivity.this.reconnectHandler.postDelayed(MultiviewActivity.this.reconnectRunnable, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
        *//*
}
    }

    */
/* Access modifiers changed, original: protected *//*

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_multi_view);
        this.m_currentCategory = getIntent().getStringExtra("Category");
        if (bundle == null) {
            this.playWhenReady = true;
            this.currentWindow = 0;
            this.playbackPosition = 0;
        } else {
            this.playWhenReady = bundle.getBoolean(KEY_PLAY_WHEN_READY);
            this.currentWindow = bundle.getInt(KEY_WINDOW);
            this.playbackPosition = bundle.getLong(KEY_POSITION);
        }
        initView();
        this.playerView = new PlayerView[4];
        this.player = new SimpleExoPlayer[4];
        this.shouldAutoPlay = new boolean[4];
        this.linear_player = new LinearLayout[4];
        this.linear_parent = new LinearLayout[2];
        this.linear_rplayer = new RelativeLayout[4];
        this.m_progressBar = new ProgressBar[4];
        this.playerStatus = new int[4];
        this.streamUrl = new String[4];
        this.curPos = 0;
        this.linear_player[0] = (LinearLayout) findViewById(R.id.linear_player1);
        this.linear_player[1] = (LinearLayout) findViewById(R.id.linear_player2);
        this.linear_player[2] = (LinearLayout) findViewById(R.id.linear_player3);
        this.linear_player[3] = (LinearLayout) findViewById(R.id.linear_player4);
        this.linear_rplayer[0] = (RelativeLayout) findViewById(R.id.linear_rplayer1);
        this.linear_rplayer[1] = (RelativeLayout) findViewById(R.id.linear_rplayer2);
        this.linear_rplayer[2] = (RelativeLayout) findViewById(R.id.linear_rplayer3);
        this.linear_rplayer[3] = (RelativeLayout) findViewById(R.id.linear_rplayer4);
        this.m_progressBar[0] = (ProgressBar) findViewById(R.id.pbProgress1);
        this.m_progressBar[1] = (ProgressBar) findViewById(R.id.pbProgress2);
        this.m_progressBar[2] = (ProgressBar) findViewById(R.id.pbProgress3);
        this.m_progressBar[3] = (ProgressBar) findViewById(R.id.pbProgress4);
        this.linear_parent[0] = (LinearLayout) findViewById(R.id.linear_parent1);
        this.linear_parent[1] = (LinearLayout) findViewById(R.id.linear_parent2);
        for (int i = 0; i < 4; i++) {
            this.shouldAutoPlay[i] = true;
            this.playerStatus[i] = 0;
        }
        this.bandwidthMeter = new DefaultBandwidthMeter();
        this.mediaDataSourceFactory = new DefaultDataSourceFactory((Context) this, Util.getUserAgent(this, "mediaPlayerSample"), (TransferListener) this.bandwidthMeter);
        this.window = new Window();
        this.streamUrl[0] = "http://awe-website-karouselnetwork-1154.s3-website.eu-central-1.amazonaws.com/Videos/movies/beauty_and_the_beast/index.m3u8";
        this.streamUrl[1] = "http://awe-website-karouselnetwork-1154.s3-website.eu-central-1.amazonaws.com/Videos/movies/wonder_woman/index.m3u8";
        this.streamUrl[2] = "http://awe-website-karouselnetwork-1154.s3-website.eu-central-1.amazonaws.com/Videos/movies/beauty_and_the_beast/index.m3u8";
        this.streamUrl[3] = "http://awe-website-karouselnetwork-1154.s3-website.eu-central-1.amazonaws.com/Videos/movies/wonder_woman/index.m3u8";
    //    Utils.disableSSLCertificateChecking();
        SetKeyListener();
    }

    public void initView() {
        this.player1Btn = (ImageView) findViewById(R.id.img_player1);
        this.player2Btn = (ImageView) findViewById(R.id.img_player2);
        this.player3Btn = (ImageView) findViewById(R.id.img_player3);
        this.player4Btn = (ImageView) findViewById(R.id.img_player4);
    }

    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            for (int i = 0; i < 4; i++) {
                if (this.playerStatus[i] > 0) {
                    initializePlayer(i);
                }
            }
        }
    }

    public void onResume() {
        super.onResume();
        int i = 0;
        while (i < 4) {
            if ((Util.SDK_INT <= 23 || this.player[i] == null) && this.playerStatus[i] > 0) {
                initializePlayer(i);
            }
            i++;
        }
    }

    public void onPause() {
        super.onPause();
        this.reconnectHandler.removeCallbacks(this.reconnectRunnable);
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    public void onStop() {
        super.onStop();
        this.reconnectHandler.removeCallbacks(this.reconnectRunnable);
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    */
/* Access modifiers changed, original: protected *//*

    public void onSaveInstanceState(Bundle bundle) {
        updateStartPosition();
        bundle.putBoolean(KEY_PLAY_WHEN_READY, this.playWhenReady);
        bundle.putInt(KEY_WINDOW, this.currentWindow);
        bundle.putLong(KEY_POSITION, this.playbackPosition);
        super.onSaveInstanceState(bundle);
    }

    private void updateStartPosition() {
        if (this.player[0] != null) {
            this.playbackPosition = this.player[0].getCurrentPosition();
            this.currentWindow = this.player[0].getCurrentWindowIndex();
            this.playWhenReady = this.player[0].getPlayWhenReady();
        }
    }

    private void gotoFullScreen(int i) {
        int i2 = 0;
        switch (i) {
            case 0:
                this.linear_parent[1].setVisibility(View.GONE);
                this.linear_player[1].setVisibility(View.GONE);
                this.linear_rplayer[1].setVisibility(View.GONE);
                break;
            case 1:
                this.linear_parent[1].setVisibility(View.GONE);
                this.linear_player[0].setVisibility(View.GONE);
                this.linear_rplayer[0].setVisibility(View.GONE);
                break;
            case 2:
                this.linear_parent[0].setVisibility(View.GONE);
                this.linear_player[3].setVisibility(View.GONE);
                this.linear_rplayer[3].setVisibility(View.GONE);
                break;
            case 3:
                this.linear_parent[0].setVisibility(View.GONE);
                this.linear_player[2].setVisibility(View.GONE);
                this.linear_rplayer[2].setVisibility(View.GONE);
                break;
        }
        while (i2 < 4) {
            Log.i("hide=", String.valueOf(i));
            if (!(i2 == i || this.playerView[i2] == null)) {
                this.playerView[i2].setVisibility(View.GONE);
                Log.i("hidesss=", String.valueOf(i2));
            }
            i2++;
        }
        pausePlayer();
        this.linear_player[this.curPos].setBackgroundResource(R.color.transparent);
        this.playerStatus[i] = 2;
    }

    private void showAllScreens() {
        this.linear_parent[0].setVisibility(View.VISIBLE);
        this.linear_parent[1].setVisibility(View.VISIBLE);
        for (int i = 0; i < 4; i++) {
            this.linear_player[i].setVisibility(View.VISIBLE);
            this.linear_rplayer[i].setVisibility(View.VISIBLE);
            if (this.playerView[i] != null) {
                this.playerView[i].setVisibility(View.VISIBLE);
            }
            if (this.playerStatus[i] == 2) {
                this.playerStatus[i] = 1;
                this.linear_player[i].setBackgroundResource(R.drawable.linear_border_sel);
            }
        }
        startPlayer();
    }

    private void initializePlayer(int i) {
        MediaSource createMediaSource = null;
        if (this.player[i] == null) {
            if (i == 0) {
                this.playerView[i] = (PlayerView) findViewById(R.id.ID_HEAD_VIDEO_VIEW1);
            } else if (i == 1) {
                this.playerView[i] = (PlayerView) findViewById(R.id.ID_HEAD_VIDEO_VIEW2);
            } else if (i == 2) {
                this.playerView[i] = (PlayerView) findViewById(R.id.ID_HEAD_VIDEO_VIEW3);
            } else if (i == 3) {
                this.playerView[i] = (PlayerView) findViewById(R.id.ID_HEAD_VIDEO_VIEW4);
            }
            TrackSelection.Factory factory = new AdaptiveTrackSelection.Factory(this.bandwidthMeter);
            this.playerView[i].setVisibility(View.VISIBLE);
            this.playerStatus[i] = 1;
            this.trackSelector = new DefaultTrackSelector(factory);
            this.lastSeenTrackGroupArray = null;
            this.player[i] = ExoPlayerFactory.newSimpleInstance((Context) this, this.trackSelector);
            this.player[i].addListener(new PlayerEventListener(i));
            this.playerView[i].setPlayer(this.player[i]);
            this.playerView[i].setControllerShowTimeoutMs(1000);
            this.player[i].setPlayWhenReady(this.shouldAutoPlay[i]);
        }
        //this.playerView[i].getSubtitleView().setVisibility(Boolean.valueOf(Utils.getSharePreferenceValue(this, AppConstants.CLOSED_CAPTION, String.valueOf(false))).booleanValue() ? View.VISIBLE : View.GONE);
        if (this.streamUrl[i].endsWith(".m3uView.GONE")) {
           // createMediaSource = new HlsMediaSource.Factory(this.mediaDataSourceFactory).createMediaSource(Uri.parse(Utils.makeAvaiableUrl(this.streamUrl[i])));
        } else {
            createMediaSource = new ExtractorMediaSource.Factory(this.mediaDataSourceFactory).createMediaSource(Uri.parse(Utils.makeAvaiableUrl(this.streamUrl[i])));
        }
        int i2 = this.currentWindow != -1 ? 1 : 0;
        if (i2 != 0) {
            this.player[i].seekTo(this.currentWindow, this.playbackPosition);
        }
        this.player[i].prepare(createMediaSource, true, false);
        Log.i("startplayer,", "player");
    }

    private void releasePlayer() {
        for (int i = 0; i < 4; i++) {
            if (this.player[i] != null) {
                updateStartPosition();
                this.shouldAutoPlay[i] = this.player[i].getPlayWhenReady();
                this.player[i].release();
                this.player[i] = null;
                this.trackSelector = null;
            }
        }
    }

    public void SetKeyListener() {
        this.m_listenerKey = new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                int keyCode = keyEvent.getKeyCode();
                if (keyCode != 4) {
                    if (keyCode != 66) {
                        if (keyCode != View.GONE) {
                            switch (keyCode) {
                                case 19:
                                    if (keyEvent.getAction() == 1) {
                                        if (MultiviewActivity.this.playerStatus[MultiviewActivity.this.curPos] != 2) {
                                            MultiviewActivity.this.setUnFocus();
                                            Log.i("Enter=", "up");
                                            MultiviewActivity.this.curPos = (MultiviewActivity.this.curPos + 2) % 4;
                                            MultiviewActivity.this.setFocus();
                                            break;
                                        }
                                        return false;
                                    }
                                    return true;
                                case 20:
                                    if (keyEvent.getAction() == 1) {
                                        if (MultiviewActivity.this.playerStatus[MultiviewActivity.this.curPos] != 2) {
                                            MultiviewActivity.this.setUnFocus();
                                            Log.i("Enter=", "down");
                                            MultiviewActivity.this.curPos = (MultiviewActivity.this.curPos + 2) % 4;
                                            MultiviewActivity.this.setFocus();
                                            break;
                                        }
                                        return false;
                                    }
                                    return true;
                                case 21:
                                    if (keyEvent.getAction() == 1) {
                                        if (MultiviewActivity.this.playerStatus[MultiviewActivity.this.curPos] != 2) {
                                            MultiviewActivity.this.setUnFocus();
                                            Log.i("Enter=", "left");
                                            MultiviewActivity.this.curPos = (MultiviewActivity.this.curPos + 3) % 4;
                                            MultiviewActivity.this.setFocus();
                                            break;
                                        }
                                        return false;
                                    }
                                    return true;
                                case 22:
                                    if (keyEvent.getAction() == 1) {
                                        if (MultiviewActivity.this.playerStatus[MultiviewActivity.this.curPos] != 2) {
                                            MultiviewActivity.this.setUnFocus();
                                            Log.i("Enter=","right");
                                            MultiviewActivity.this.curPos = (MultiviewActivity.this.curPos + 1) % 4;
                                            MultiviewActivity.this.setFocus();
                                            break;
                                        }
                                        return false;
                                    }
                                    return true;
                                case 23:
                                    break;
                            }
                        } else if (keyEvent.getAction() != 1) {
                            return true;
                        } else {
                            MultiviewActivity.this.showCategoryDialog();
                        }
                    }
                    if (keyEvent.isLongPress() && keyEvent.getAction() == 0) {
                        MultiviewActivity.this.showCategoryDialog();
                        return false;
                    } else if (keyEvent.isLongPress() || keyEvent.getAction() != 1) {
                        return true;
                    } else {
                        Log.i("Enter=", "ok");
                        if (MultiviewActivity.this.playerStatus[MultiviewActivity.this.curPos] == 2) {
                            return false;
                        }
                        MultiviewActivity.this.loadVideo(MultiviewActivity.this.curPos);
                    }
                } else if (keyEvent.getAction() != 1) {
                    return true;
                } else {
                    Log.i("finish", "finish");
                    if (MultiviewActivity.this.playerStatus[MultiviewActivity.this.curPos] == 2) {
                        MultiviewActivity.this.showAllScreens();
                    } else {
                        MultiviewActivity.this.finish();
                    }
                }
                return false;
            }
        };
        findViewById(R.id.activity_multiview).getRootView().clearFocus();
        findViewById(R.id.activity_multiview).setFocusable(true);
        getWindow().setSoftInputMode(3);
        findViewById(R.id.activity_multiview).setOnKeyListener(this.m_listenerKey);
    }

    public void setUnFocus() {
        this.linear_player[this.curPos].setBackgroundResource(R.drawable.linear_border);
        for (int i = 0; i < 4; i++) {
            if (this.player[i] != null) {
                this.player[i].setVolume(0.0f);
            }
        }
    }

    private void pausePlayer() {
        int i = 0;
        while (i < 4) {
            if (!(this.curPos == i || this.player[i] == null)) {
                this.player[i].setPlayWhenReady(false);
                this.player[i].getPlaybackState();
            }
            i++;
        }
    }

    private void startPlayer() {
        int i = 0;
        while (i < 4) {
            if (!(this.curPos == i || this.player[i] == null)) {
                this.player[i].setPlayWhenReady(true);
                this.player[i].getPlaybackState();
            }
            i++;
        }
    }

    public void setFocus() {
        this.linear_player[this.curPos].setBackgroundResource(R.drawable.linear_border_sel);
        if (this.player[this.curPos] != null) {
            this.player[this.curPos].setVolume(1.0f);
        }
    }

    public void loadVideo(int i) {
        if (this.playerStatus[i] == 1) {
            gotoFullScreen(i);
        } else {
            showCategoryDialog();
        }
    }

    private void showCategoryDialog() {
       */
/* int i = 0;
        View inflate = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.main_menu_dialog, null, false);
        final Dialog dialog = new Dialog(this, R.style.f364Theme.CustomDialog);
        dialog.setContentView(inflate);
        ListView listView = (ListView) inflate.findViewById(R.id.ID_MENU);
        final ArrayList arrayList = new ArrayList();
        arrayList.add(getString(R.string.favorite_category));
        arrayList.add(getString(R.string.all_channels));
        int i2 = (this.m_currentCategory == null || this.m_currentCategory.equals(getString(R.string.favorite_category)) || !this.m_currentCategory.equals(getString(R.string.all_channels))) ? 0 : 1;
        int i3 = i2;
        i2 = 0;
        while (i2 < AppConstants.CHANNEL_CATEGORY_LIST.size()) {
            if (!((LiveCategoryInfo) AppConstants.CHANNEL_CATEGORY_LIST.get(i2)).m_sCategroyID.equals("17") || !Utils.getSharePreferenceValue(this, AppConstants.PARENTAL_LOCK, "false").equals("true")) {
                arrayList.add(((LiveCategoryInfo) AppConstants.CHANNEL_CATEGORY_LIST.get(i2)).m_sCategroyName.toUpperCase());
                if (!(this.m_currentCategory == null || this.m_currentCategory.isEmpty() || !this.m_currentCategory.equalsIgnoreCase(((LiveCategoryInfo) AppConstants.CHANNEL_CATEGORY_LIST.get(i2)).m_sCategroyID))) {
                    i3 = i2 + 2;
                }
            }
            i2++;
        }
        if (this.m_currentCategory == null || i3 <= 0) {
            i = i3;
        } else {
            String str = (String) arrayList.get(i3);
            arrayList.remove(i3);
            arrayList.add(0, str);
        }
        listView.setAdapter(new MainMenuAdapter(this, arrayList));
        listView.setSelection(i);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                String str = "";
                if (!((String) arrayList.get(i)).equals(MultiviewActivity.this.getResources().getString(R.string.favorite_category))) {
                    if (!((String) arrayList.get(i)).equals(MultiviewActivity.this.getResources().getString(R.string.all_channels))) {
                        Iterator it = AppConstants.CHANNEL_CATEGORY_LIST.iterator();
                        while (it.hasNext()) {
                            LiveCategoryInfo liveCategoryInfo = (LiveCategoryInfo) it.next();
                            if (liveCategoryInfo.m_sCategroyName.equalsIgnoreCase((String) arrayList.get(i))) {
                                str = liveCategoryInfo.m_sCategroyID;
                                break;
                            }
                        }
                    }
                    str = MultiviewActivity.this.getResources().getString(R.string.all_channels);
                } else {
                    str = MultiviewActivity.this.getResources().getString(R.string.favorite_category);
                }
                MultiviewActivity.this.filterChannel(str);
                dialog.dismiss();
            }
        });
        dialog.show();*//*

    }

    public void filterChannel(String str) {
        */
/*this.m_currentCategory = str;
        this.m_arrayChannelItem.clear();
        Iterator it = AppConstants.EPGDATA.iterator();
        while (it.hasNext()) {
            ChannelItem channelItem = (ChannelItem) it.next();
            if (this.m_currentCategory.equals(getString(R.string.favorite_category))) {
                if (AppConstants.FAVORITE_CHANNEL_ARRAY.contains(channelItem.m_sStreamID)) {
                    if (!channelItem.m_sCategory_ID.equals("17") || !Utils.getSharePreferenceValue(this, AppConstants.PARENTAL_LOCK, "false").equals("true")) {
                        this.m_arrayChannelItem.add(channelItem);
                    }
                }
            } else if ((channelItem.m_sCategory_ID != null && channelItem.m_sCategory_ID.equalsIgnoreCase(this.m_currentCategory)) || this.m_currentCategory.equals(getString(R.string.all_channels))) {
                if (!channelItem.m_sCategory_ID.equals("17") || !Utils.getSharePreferenceValue(this, AppConstants.PARENTAL_LOCK, "false").equals("true")) {
                    this.m_arrayChannelItem.add(channelItem);
                }
            }
        }
        if (this.m_arrayChannelItem.size() >= 0) {
            View inflate = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.multiview_select_channel_dialog, null, false);
            final Dialog dialog = new Dialog(this, R.style.f364Theme.CustomDialog);
            dialog.setContentView(inflate);
            ListView listView = (ListView) inflate.findViewById(R.id.ID_LIST_VIEW_CHANNEL);
            listView.setAdapter(new ChannelAdapter(this, this.m_arrayChannelItem, "MULTI_CHANNEL"));
            if (str.equals("5")) {
                if (AppConstants.MLBCHANNEL.size() > 0) {
                    AppConstants.EPG_FILTERED_DATA.add(0, AppConstants.MLBCHANNEL.get(0));
                }
                if (AppConstants.NBACHANNEL.size() > 0) {
                    AppConstants.EPG_FILTERED_DATA.add(0, AppConstants.NBACHANNEL.get(0));
                }
                if (AppConstants.NFLCHANNEL.size() > 0) {
                    AppConstants.EPG_FILTERED_DATA.add(0, AppConstants.NFLCHANNEL.get(0));
                }
                if (AppConstants.NHLCHANNEL.size() > 0) {
                    AppConstants.EPG_FILTERED_DATA.add(0, AppConstants.NHLCHANNEL.get(0));
                }
                if (AppConstants.EPLCHANNEL.size() > 0) {
                    AppConstants.EPG_FILTERED_DATA.add(0, AppConstants.EPLCHANNEL.get(0));
                }
                if (AppConstants.PPVCHANNEL.size() > 0) {
                    AppConstants.EPG_FILTERED_DATA.add(0, AppConstants.PPVCHANNEL.get(0));
                }
            }
            listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    dialog.dismiss();
                    ChannelItem channelItem = (ChannelItem) MultiviewActivity.this.m_arrayChannelItem.get(i);
                    if (AppConstants.MLBCHANNEL.contains(channelItem)) {
                        MultiviewActivity.this.showGroupChannelMenu(GROUP_CHANNEL_KIND.MLB_CHANNEL);
                    } else if (AppConstants.NBACHANNEL.contains(channelItem)) {
                        MultiviewActivity.this.showGroupChannelMenu(GROUP_CHANNEL_KIND.NBA_CHANNEL);
                    } else if (AppConstants.NFLCHANNEL.contains(channelItem)) {
                        MultiviewActivity.this.showGroupChannelMenu(GROUP_CHANNEL_KIND.NFL_CHANNEL);
                    } else if (AppConstants.NHLCHANNEL.contains(channelItem)) {
                        MultiviewActivity.this.showGroupChannelMenu(GROUP_CHANNEL_KIND.NHL_CHANNEL);
                    } else if (AppConstants.PPVCHANNEL.contains(channelItem)) {
                        MultiviewActivity.this.showGroupChannelMenu(GROUP_CHANNEL_KIND.PPV_CHANNEL);
                    } else if (AppConstants.EPLCHANNEL.contains(channelItem)) {
                        MultiviewActivity.this.showGroupChannelMenu(GROUP_CHANNEL_KIND.EPL_CHANNEL);
                    } else {
                        MultiviewActivity.this.streamUrl[MultiviewActivity.this.curPos] = Utils.makeStreamURL(MultiviewActivity.this, ((ChannelItem) MultiviewActivity.this.m_arrayChannelItem.get(i)).m_sStreamID);
                        if (MultiviewActivity.this.curPos == 0) {
                            MultiviewActivity.this.player1Btn.setVisibility(View.GONE);
                        }
                        if (MultiviewActivity.this.curPos == 1) {
                            MultiviewActivity.this.player2Btn.setVisibility(View.GONE);
                        }
                        if (MultiviewActivity.this.curPos == 2) {
                            MultiviewActivity.this.player3Btn.setVisibility(View.GONE);
                        }
                        if (MultiviewActivity.this.curPos == 3) {
                            MultiviewActivity.this.player4Btn.setVisibility(View.GONE);
                        }
                        MultiviewActivity.this.initializePlayer(MultiviewActivity.this.curPos);
                    }
                }
            });
            dialog.show();
        }
    *//*

    }

    private void showGroupChannelMenu(*/
/*GROUP_CHANNEL_KIND group_channel_kind*//*
) {
     */
/*   ArrayList arrayList;
        View inflate = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.group_channel_dialog, null, false);
        final Dialog dialog = new Dialog(this, R.style.f364Theme.CustomDialog);
        dialog.setContentView(inflate);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.ID_GROUP_CHANNEL_IMG);
        ListView listView = (ListView) inflate.findViewById(R.id.ID_LIST_VIEW_SUB_CHANNEL);
        switch (group_channel_kind) {
            case MLB_CHANNEL:
                arrayList = AppConstants.MLBCHANNEL;
                imageView.setImageResource(R.drawable.mlb);
                break;
            case NBA_CHANNEL:
                arrayList = AppConstants.NBACHANNEL;
                imageView.setImageResource(R.drawable.nba);
                break;
            case NFL_CHANNEL:
                arrayList = AppConstants.NFLCHANNEL;
                imageView.setImageResource(R.drawable.sunday_ticket);
                break;
            case NHL_CHANNEL:
                arrayList = AppConstants.NHLCHANNEL;
                imageView.setImageResource(R.drawable.nhl);
                break;
            case EPL_CHANNEL:
                arrayList = AppConstants.EPLCHANNEL;
                imageView.setImageResource(R.drawable.epl);
                break;
            default:
                arrayList = AppConstants.PPVCHANNEL;
                imageView.setImageResource(R.drawable.ppv);
                break;
        }
        listView.setAdapter(new ChannelAdapter(this, arrayList, "GROUP_CHANNEL"));
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                dialog.dismiss();
                MultiviewActivity.this.streamUrl[MultiviewActivity.this.curPos] = Utils.makeStreamURL(MultiviewActivity.this, ((ChannelItem) arrayList.get(i)).m_sStreamID);
                if (MultiviewActivity.this.curPos == 0) {
                    MultiviewActivity.this.player1Btn.setVisibility(View.GONE);
                }
                if (MultiviewActivity.this.curPos == 1) {
                    MultiviewActivity.this.player2Btn.setVisibility(View.GONE);
                }
                if (MultiviewActivity.this.curPos == 2) {
                    MultiviewActivity.this.player3Btn.setVisibility(View.GONE);
                }
                if (MultiviewActivity.this.curPos == 3) {
                    MultiviewActivity.this.player4Btn.setVisibility(View.GONE);
                }
                MultiviewActivity.this.initializePlayer(MultiviewActivity.this.curPos);
            }
        });
        dialog.show();*//*

    }
}
*/
