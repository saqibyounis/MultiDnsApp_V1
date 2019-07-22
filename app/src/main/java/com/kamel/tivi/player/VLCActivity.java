package com.kamel.tivi.player;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kamel.tivi.LiveTvActivity;
import com.kamel.tivi.R;
import com.kamel.tivi.epg.model.EpgModel;
import com.kamel.tivi.epg.model.constants.Constants;
import com.kamel.tivi.network.URLs;
import com.kamel.tivi.useaccount.UserAccount;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.videolan.libvlc.Dialog;
import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class VLCActivity extends AppCompatActivity  implements IVLCVout.OnNewVideoLayoutListener, AdapterView.OnItemClickListener {
          public int CURRENT_CHANNEL_COUNTER=0;
    private static final boolean USE_SURFACE_VIEW = true;
    private static final boolean ENABLE_SUBTITLES = true;
    private static final String TAG = "JavaActivity";
    private static final int SURFACE_BEST_FIT = 0;
    private static final int SURFACE_FIT_SCREEN = 1;
    private static final int SURFACE_FILL = 2;
    private static final int SURFACE_16_9 = 3;
    private static final int SURFACE_4_3 = 4;
    private static final int SURFACE_ORIGINAL = 5;
    private static int CURRENT_SIZE = SURFACE_BEST_FIT;
            public boolean cPanelFlag=false;
    private FrameLayout mVideoSurfaceFrame = null;
    private SurfaceView mVideoSurface = null;
    private SurfaceView mSubtitlesSurface = null;
    private TextureView mVideoTexture = null;
    private View mVideoView = null;

    private final Handler mHandler = new Handler();
    private View.OnLayoutChangeListener mOnLayoutChangeListener = null;

    private LibVLC mLibVLC = null;
    private MediaPlayer mMediaPlayer = null;
    private int mVideoHeight = 0;
    private int mVideoWidth = 0;
    private int mVideoVisibleHeight = 0;
    private int mVideoVisibleWidth = 0;
    private int mVideoSarNum = 0;
    private int mVideoSarDen = 0;
    Timer timer = new Timer();
    ////epg view
     LinearLayout linearLayout;
    TextView desc,currentchannel,nextp,time;
    TimerTask timerTask;
         GridView channelGrid;
         FrameLayout frameLayout;
         LinearLayout cpanel;
    private int staticpostion=0;

    Dialog.ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_vlc);
        desc=findViewById(R.id.cdes);
        currentchannel=findViewById(R.id.currentchannel);
        nextp=findViewById(R.id.nextchannel);
        time=findViewById(R.id.time);
        frameLayout=findViewById(R.id.video_surface_frame);
        frameLayout.requestFocus();
        linearLayout=findViewById(R.id.epgpanel);
        channelGrid=findViewById(R.id.channelgrid);
        channelGrid.setAdapter(Constants.channelAdapters);
     cpanel=findViewById(R.id.cpanel);
        String url=getIntent().getExtras().getString("url");
        LiveTvActivity.SAMPLE_URL=url;
        /*timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Your database code here
            linearLayout.setVisibility(View.GONE);

            }
        }, 2*60*1000);*/
// Since Java-8

            staticpostion=Constants.position;
        setEPgData();
        channelGrid.setSelection(Constants.position);
        channelGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  stopVlc();
                channelGrid.setSelection(position);
                staticpostion=position;
                    CURRENT_CHANNEL_COUNTER=position+1;
                Constants.streamid=Constants.channelAdapters.getItem(position).getStreamId();
                 setEPgData();
                String url= URLs.url +"/live/"+UserAccount.userAccount.getUserName()+"/"+UserAccount.userAccount.getPassword()+"/"+Constants.channelAdapters.getItem(position).getStreamId()+".ts";
                LiveTvActivity.SAMPLE_URL=url;

                if (mOnLayoutChangeListener != null) {
                    mVideoSurfaceFrame.removeOnLayoutChangeListener(mOnLayoutChangeListener);
                    mOnLayoutChangeListener = null;

                }


                mMediaPlayer.getVLCVout().detachViews();
                final IVLCVout vlcVout = mMediaPlayer.getVLCVout();
                if (mVideoSurface != null) {
                    vlcVout.setVideoView(mVideoSurface);
                    if (mSubtitlesSurface != null)
                        vlcVout.setSubtitlesView(mSubtitlesSurface);
                } else
                    vlcVout.setVideoView(mVideoTexture);
                vlcVout.attachViews(VLCActivity.this);

                Media media = new Media(mLibVLC, Uri.parse(LiveTvActivity.SAMPLE_URL));
                mMediaPlayer.setMedia(media);
                media.release();
                mMediaPlayer.play();

                if (mOnLayoutChangeListener == null) {
                    mOnLayoutChangeListener = new View.OnLayoutChangeListener() {
                        private final Runnable mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                updateVideoSurfaces();
                            }
                        };

                        @Override
                        public void onLayoutChange(View v, int left, int top, int right,
                                                   int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom) {
                                mHandler.removeCallbacks(mRunnable);
                                mHandler.post(mRunnable);
                            }
                        }
                    };
                }
                mVideoSurfaceFrame.addOnLayoutChangeListener(mOnLayoutChangeListener);
                mMediaPlayer.setVolume(60);

            }
        });
        final ArrayList<String> args = new ArrayList<>();
        args.add("-vvv");
        mLibVLC = new LibVLC(this, args);
        mMediaPlayer = new MediaPlayer(mLibVLC);

        mVideoSurfaceFrame = (FrameLayout) findViewById(R.id.video_surface_frame);
        if (USE_SURFACE_VIEW) {
            ViewStub stub = (ViewStub) findViewById(R.id.surface_stub);
            mVideoSurface = (SurfaceView) stub.inflate();
            if (ENABLE_SUBTITLES) {
                stub = (ViewStub) findViewById(R.id.subtitles_surface_stub);
                mSubtitlesSurface = (SurfaceView) stub.inflate();
                mSubtitlesSurface.setZOrderMediaOverlay(true);
                mSubtitlesSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            }
            mVideoView = mVideoSurface;
        } else {
            ViewStub stub = (ViewStub) findViewById(R.id.texture_stub);


            mVideoTexture = (TextureView) stub.inflate();
            mVideoView = mVideoTexture;
        }


        mVideoSurfaceFrame = (FrameLayout) findViewById(R.id.video_surface_frame);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
        mLibVLC.release();
    }

    @Override
    protected void onStart() {
        super.onStart();

        final IVLCVout vlcVout = mMediaPlayer.getVLCVout();
        if (mVideoSurface != null) {
            vlcVout.setVideoView(mVideoSurface);
            if (mSubtitlesSurface != null)
                vlcVout.setSubtitlesView(mSubtitlesSurface);
        } else
            vlcVout.setVideoView(mVideoTexture);
        vlcVout.attachViews(this);

        Media media = new Media(mLibVLC, Uri.parse(LiveTvActivity.SAMPLE_URL));
        mMediaPlayer.setMedia(media);
        media.release();
        mMediaPlayer.play();

        if (mOnLayoutChangeListener == null) {
            mOnLayoutChangeListener = new View.OnLayoutChangeListener() {
                private final Runnable mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        updateVideoSurfaces();
                    }
                };

                @Override
                public void onLayoutChange(View v, int left, int top, int right,
                                           int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom) {
                        mHandler.removeCallbacks(mRunnable);
                        mHandler.post(mRunnable);
                    }
                }
            };
        }
        mVideoSurfaceFrame.addOnLayoutChangeListener(mOnLayoutChangeListener);

        mMediaPlayer.setVolume(60);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mOnLayoutChangeListener != null) {
            mVideoSurfaceFrame.removeOnLayoutChangeListener(mOnLayoutChangeListener);
            mOnLayoutChangeListener = null;
        }

        mMediaPlayer.stop();

        mMediaPlayer.getVLCVout().detachViews();
    }

    private void changeMediaPlayerLayout(int displayW, int displayH) {
        /* Change the video placement using the MediaPlayer API */
        switch (CURRENT_SIZE) {
            case SURFACE_BEST_FIT:
                mMediaPlayer.setAspectRatio(null);
                mMediaPlayer.setScale(0);
                break;
            case SURFACE_FIT_SCREEN:
            case SURFACE_FILL: {
                Media.VideoTrack vtrack = mMediaPlayer.getCurrentVideoTrack();
                if (vtrack == null)
                    return;
                final boolean videoSwapped = vtrack.orientation == Media.VideoTrack.Orientation.LeftBottom
                        || vtrack.orientation == Media.VideoTrack.Orientation.RightTop;
                if (CURRENT_SIZE == SURFACE_FIT_SCREEN) {
                    int videoW = vtrack.width;
                    int videoH = vtrack.height;

                    if (videoSwapped) {
                        int swap = videoW;
                        videoW = videoH;
                        videoH = swap;
                    }
                    if (vtrack.sarNum != vtrack.sarDen)
                        videoW = videoW * vtrack.sarNum / vtrack.sarDen;

                    float ar = videoW / (float) videoH;
                    float dar = displayW / (float) displayH;

                    float scale;
                    if (dar >= ar)
                        scale = displayW / (float) videoW; /* horizontal */
                    else
                        scale = displayH / (float) videoH; /* vertical */
                    mMediaPlayer.setScale(scale);
                    mMediaPlayer.setAspectRatio(null);
                } else {
                    mMediaPlayer.setScale(0);
                    mMediaPlayer.setAspectRatio(!videoSwapped ? "" + displayW + ":" + displayH
                            : "" + displayH + ":" + displayW);
                }
                break;
            }
            case SURFACE_16_9:
                mMediaPlayer.setAspectRatio("16:9");
                mMediaPlayer.setScale(0);
                break;
            case SURFACE_4_3:
                mMediaPlayer.setAspectRatio("4:3");
                mMediaPlayer.setScale(0);
                break;
            case SURFACE_ORIGINAL:
                mMediaPlayer.setAspectRatio(null);
                mMediaPlayer.setScale(1);
                break;
        }
    }

    private void updateVideoSurfaces() {
        int sw = getWindow().getDecorView().getWidth();
        int sh = getWindow().getDecorView().getHeight();

        // sanity check
        if (sw * sh == 0) {
            Log.e(TAG, "Invalid surface size");
            return;
        }

        mMediaPlayer.getVLCVout().setWindowSize(sw, sh);

        ViewGroup.LayoutParams lp = mVideoView.getLayoutParams();
        if (mVideoWidth * mVideoHeight == 0) {
            /* Case of OpenGL vouts: handles the placement of the video using MediaPlayer API */
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoView.setLayoutParams(lp);
            lp = mVideoSurfaceFrame.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoSurfaceFrame.setLayoutParams(lp);
            changeMediaPlayerLayout(sw, sh);
            return;
        }

        if (lp.width == lp.height && lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            /* We handle the placement of the video using Android View LayoutParams */
            mMediaPlayer.setAspectRatio(null);
            mMediaPlayer.setScale(0);
        }

        double dw = sw, dh = sh;
        final boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        if (sw > sh && isPortrait || sw < sh && !isPortrait) {
            dw = sh;
            dh = sw;
        }

        // compute the aspect ratio
        double ar, vw;
        if (mVideoSarDen == mVideoSarNum) {
            /* No indication about the density, assuming 1:1 */
            vw = mVideoVisibleWidth;
            ar = (double) mVideoVisibleWidth / (double) mVideoVisibleHeight;
        } else {
            /* Use the specified aspect ratio */
            vw = mVideoVisibleWidth * (double) mVideoSarNum / mVideoSarDen;
            ar = vw / mVideoVisibleHeight;
        }

        // compute the display aspect ratio
        double dar = dw / dh;

        switch (CURRENT_SIZE) {
            case SURFACE_BEST_FIT:
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_FIT_SCREEN:
                if (dar >= ar)
                    dh = dw / ar; /* horizontal */
                else
                    dw = dh * ar; /* vertical */
                break;
            case SURFACE_FILL:
                break;
            case SURFACE_16_9:
                ar = 16.0 / 9.0;
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_4_3:
                ar = 4.0 / 3.0;
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_ORIGINAL:
                dh = mVideoVisibleHeight;
                dw = vw;
                break;
        }

        // set display size
        lp.width = (int) Math.ceil(dw * mVideoWidth / mVideoVisibleWidth);
        lp.height = (int) Math.ceil(dh * mVideoHeight / mVideoVisibleHeight);
        mVideoView.setLayoutParams(lp);
        if (mSubtitlesSurface != null)
            mSubtitlesSurface.setLayoutParams(lp);

        // set frame size (crop if necessary)
        lp = mVideoSurfaceFrame.getLayoutParams();
        lp.width = (int) Math.floor(dw);
        lp.height = (int) Math.floor(dh);
        mVideoSurfaceFrame.setLayoutParams(lp);

        mVideoView.invalidate();
        if (mSubtitlesSurface != null)
            mSubtitlesSurface.invalidate();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onNewVideoLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
        mVideoWidth = width;
        mVideoHeight = height;
        mVideoVisibleWidth = visibleWidth;
        mVideoVisibleHeight = visibleHeight;
        mVideoSarNum = sarNum;
        mVideoSarDen = sarDen;
        updateVideoSurfaces();
    }

    public void setEPgData(){
       Constants.epgModels=new ArrayList<>();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        String url=URLs.url +"/player_api.php?username="+UserAccount.userAccount.getUserName()+"&password="+UserAccount.userAccount.getPassword()+"&action=get_short_epg&stream_id="+ com.kamel.tivi.epg.model.constants.Constants.streamid;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object=new JSONObject(new String(responseBody));
                    JSONArray array=object.getJSONArray("epg_listings");

                    for(int i=0;i<array.length();i++){
                        String id= array.getJSONObject(i).getString("id");
                        String epgid=array.getJSONObject(i).getString("epg_id");
                        String title=array.getJSONObject(i).getString("title");
                        String start=array.getJSONObject(i).getString("start");
                        String end=array.getJSONObject(i).getString("end");
                        String description=array.getJSONObject(i).getString("description");

                        String start_time=array.getJSONObject(i).getString("start_timestamp");
                        String stop_time=array.getJSONObject(i).getString("stop_timestamp");
                        int duration=Integer.parseInt(start_time) - Integer.parseInt(stop_time);
                        String settime=simpleDateFormat.format(new Date((Long.parseLong(start_time) * 1000) + 3600000));
                        long parseLong = Long.parseLong(stop_time);
                        Long.signum(parseLong);
                        String timeto=simpleDateFormat.format(new Date((parseLong * 1000) + 3600000));

                        EpgModel epgModel=new EpgModel(id,epgid,title,start,end,description,start_time,stop_time,settime,timeto,""+duration);
                           Constants.epgModels.add(epgModel);


                        }





                    showCPanle();
                    initView(null);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }
     public void showCPanle(){

         if(cpanel.getVisibility()==View.GONE &&!cPanelFlag)
         {
             cPanelFlag=true;
             cpanel.setVisibility(View.VISIBLE);


         }

     }
    public void initView(View view) {

        try {
            currentchannel.setText("Now: " + new String(Base64.decode(Constants.epgModels.get(0).getTitle(), 0)));
            nextp.setText("Next: " + new String(Base64.decode(Constants.epgModels.get(1).getTitle(), 0)));
            desc.setText("Description: " + new String(Base64.decode(Constants.epgModels.get(0).getDescription(), 0)));
            time.setText("Started at " + Constants.epgModels.get(0).getSettime() + " End at " + Constants.epgModels.get(0).getTimeto()+"Channel: "+Constants.channels.get(staticpostion).name);
        } catch (Exception ex) {

            currentchannel.setText("Now: NA");
            nextp.setText("Next: NA");
            desc.setText("Description: NA");
         //   time.setText("Time: NA Channel: "+Constants.channels.get(CURRENT_CHANNEL_COUNTER--).name);


        }
        System.out.println("CLICK");
      //  if(linearLayout.getVisibility()==View.GONE) {
            linearLayout.setVisibility(View.VISIBLE);
          //  channelGrid.setVisibility(View.VISIBLE);
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            linearLayout.setVisibility(View.GONE);

                           // channelGrid.setVisibility(View.GONE);
                        }
                    });
                }
            };


       // }
        timer.schedule(timerTask, 15000);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

/*
        if(cpanel.getVisibility()==View.GONE){
*/


            if(KeyEvent.KEYCODE_DPAD_CENTER==keyCode || KeyEvent.KEYCODE_ENTER==keyCode &&!cPanelFlag) {
                cPanelFlag = true;
                //initView(null);
                   showCPanle();

            }
             else if(KeyEvent.KEYCODE_DPAD_UP==keyCode &&!cPanelFlag){

                System.out.println("UP CHANNEL "+CURRENT_CHANNEL_COUNTER);
                if(Constants.size==CURRENT_CHANNEL_COUNTER){
                    CURRENT_CHANNEL_COUNTER=0;

                }/*else {
                    CURRENT_CHANNEL_COUNTER++;


                }*/
                onItemClick(null,null,CURRENT_CHANNEL_COUNTER,0);
                CURRENT_CHANNEL_COUNTER++;


            }else if(KeyEvent.KEYCODE_DPAD_DOWN==keyCode &&!cPanelFlag){

                System.out.println("DOWN CHANNEL "+CURRENT_CHANNEL_COUNTER);
                if(CURRENT_CHANNEL_COUNTER==0){
                    CURRENT_CHANNEL_COUNTER=Constants.size-1;

                }/*else {
                    CURRENT_CHANNEL_COUNTER--;
                }*/
                onItemClick(null,null,CURRENT_CHANNEL_COUNTER,0);

                CURRENT_CHANNEL_COUNTER--;
            }

        //}
        return super.onKeyDown(keyCode, event);

    }

    public void close(View view){
        cPanelFlag=false;
        timerTask.cancel();
        linearLayout.setVisibility(View.GONE);
        cpanel.setVisibility(View.GONE);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        channelGrid.setSelection(position);
        staticpostion=position;
        Constants.streamid=Constants.channelAdapters.getItem(position).getStreamId();
        setEPgData();
        String url=URLs.url +"/live/"+UserAccount.userAccount.getUserName()+"/"+UserAccount.userAccount.getPassword()+"/"+Constants.channelAdapters.getItem(position).getStreamId()+".ts";
        LiveTvActivity.SAMPLE_URL=url;

        if (mOnLayoutChangeListener != null) {
            mVideoSurfaceFrame.removeOnLayoutChangeListener(mOnLayoutChangeListener);
            mOnLayoutChangeListener = null;

        }

        mMediaPlayer.stop();

        mMediaPlayer.getVLCVout().detachViews();
        final IVLCVout vlcVout = mMediaPlayer.getVLCVout();
        if (mVideoSurface != null) {
            vlcVout.setVideoView(mVideoSurface);
            if (mSubtitlesSurface != null)
                vlcVout.setSubtitlesView(mSubtitlesSurface);
        } else
            vlcVout.setVideoView(mVideoTexture);
        vlcVout.attachViews(VLCActivity.this);

        Media media = new Media(mLibVLC, Uri.parse(LiveTvActivity.SAMPLE_URL));
        mMediaPlayer.setMedia(media);
        media.release();
        mMediaPlayer.play();

        if (mOnLayoutChangeListener == null) {
            mOnLayoutChangeListener = new View.OnLayoutChangeListener() {
                private final Runnable mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        updateVideoSurfaces();
                    }
                };

                @Override
                public void onLayoutChange(View v, int left, int top, int right,
                                           int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom) {
                        mHandler.removeCallbacks(mRunnable);
                        mHandler.post(mRunnable);
                    }
                }
            };
        }
        mVideoSurfaceFrame.addOnLayoutChangeListener(mOnLayoutChangeListener);


        mMediaPlayer.setVolume(60);
    }

    public void stopVlc(){




            if (mOnLayoutChangeListener != null) {
                mVideoSurfaceFrame.removeOnLayoutChangeListener(mOnLayoutChangeListener);
                mOnLayoutChangeListener = null;
            }

            mMediaPlayer.stop();

            mMediaPlayer.getVLCVout().detachViews();

        }


    @Override
    public void onBackPressed() {
       // onBackPressed();
        mMediaPlayer.stop();

        super.onBackPressed();
    }
}