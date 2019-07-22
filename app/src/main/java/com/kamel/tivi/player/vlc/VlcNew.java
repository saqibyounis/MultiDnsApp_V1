package com.kamel.tivi.player.vlc;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kamel.tivi.R;
import com.kamel.tivi.channels.ChannelsModel;
import com.kamel.tivi.epg.model.EpgModel;
import com.kamel.tivi.network.URLs;
import com.kamel.tivi.useaccount.UserAccount;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.Media;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class VlcNew extends AppCompatActivity implements IVLCVout.OnNewVideoLayoutListener ,MediaPlayer.EventListener{
private static final boolean USE_SURFACE_VIEW = true;
private static final boolean ENABLE_SUBTITLES = true;
private static final String TAG = "JavaActivity";
private static final String ASSET_FILENAME = "bbb.m4v";
private static final int SURFACE_BEST_FIT = 0;
private static final int SURFACE_FIT_SCREEN = 1;
private static final int SURFACE_FILL = 2;
private static final int SURFACE_16_9 = 3;
private static final int SURFACE_4_3 = 4;
private static final int SURFACE_ORIGINAL = 5;
private static int CURRENT_SIZE = SURFACE_BEST_FIT;

private FrameLayout mVideoSurfaceFrame = null;
private SurfaceView mVideoSurface = null;
private SurfaceView mSubtitlesSurface = null;
private TextureView mVideoTexture = null;
private View mVideoView = null;
TimerTask timerTask;
       public EpgModel epgModel;
        ProgressDialog  progressDialog;
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
String liveUr="";
ChannelsModel channelsModel;
TextView e1,e2,e4,e3;
LinearLayout epgPanel;
RelativeLayout channelPanel;
        private List<EpgModel> epgModels;
    private Timer timer=new Timer();
      Button button;
    @Override
        public void onBackPressed() {

                stopVlc();
                super.onBackPressed();
        }

        @Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vlc_new);
        e1=findViewById(R.id.cdes);
        e2=findViewById(R.id.time);
        e3=findViewById(R.id.currentchannel);
        e4=findViewById(R.id.nextchannel);
        button=findViewById(R.id.hide);
        button.requestFocus();
        epgPanel=findViewById(R.id.epgpanel);
        channelPanel=findViewById(R.id.channelpanel);
   channelsModel=VlcSetup.vlcSetup.chPlus();
   liveUr=channelsModel.getLink();


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
        }
        else
        {
        ViewStub stub = (ViewStub) findViewById(R.id.texture_stub);
        mVideoTexture = (TextureView) stub.inflate();
        mVideoView = mVideoTexture;
        }
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
        }
        else
        vlcVout.setVideoView(mVideoTexture);
        vlcVout.attachViews(this);
        liveUr=URLs.url +"/live/"+ UserAccount.userAccount.getUserName()+"/"+UserAccount.userAccount.getPassword()+"/"+channelsModel.getStreamId()+".ts";

final Media media = new Media(mLibVLC, Uri.parse(liveUr));

        mMediaPlayer.setMedia(media);
        media.release();

        mMediaPlayer.play();
    mMediaPlayer.setVolume(60);

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

        showEpg();
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
        mMediaPlayer.setAspectRatio(!videoSwapped ? ""+displayW+":"+displayH
        : ""+displayH+":"+displayW);
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
        lp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mVideoView.setLayoutParams(lp);
        lp = mVideoSurfaceFrame.getLayoutParams();
        lp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
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
        ar = (double)mVideoVisibleWidth / (double)mVideoVisibleHeight;
        } else {
        /* Use the specified aspect ratio */
        vw = mVideoVisibleWidth * (double)mVideoSarNum / mVideoSarDen;
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
        lp.width  = (int) Math.ceil(dw * mVideoWidth / mVideoVisibleWidth);
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


        public void chPlus(View view){

       this.channelsModel= VlcSetup.vlcSetup.chPlus();
       stopVlc();
        playVlc();
        }


        public void chMinus(View view){

                this.channelsModel= VlcSetup.vlcSetup.chMinus();
                stopVlc();
                playVlc();


        }

        public void playVlc(){
showEpg();

final IVLCVout vlcVout = mMediaPlayer.getVLCVout();
                if (mVideoSurface != null) {
                        vlcVout.setVideoView(mVideoSurface);
                        if (mSubtitlesSurface != null)
                                vlcVout.setSubtitlesView(mSubtitlesSurface);
                }
                else
                        vlcVout.setVideoView(mVideoTexture);
                vlcVout.attachViews(this);

                liveUr=URLs.url +"/live/"+ UserAccount.userAccount.getUserName()+"/"+UserAccount.userAccount.getPassword()+"/"+channelsModel.getStreamId()+".ts";
                final Media media = new Media(mLibVLC, Uri.parse(liveUr));
                mMediaPlayer.setMedia(media);
                media.release();

                mMediaPlayer.play();
            mMediaPlayer.setVolume(60);

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
        }







          public void stopVlc(){

                  if (mOnLayoutChangeListener != null) {
                          mVideoSurfaceFrame.removeOnLayoutChangeListener(mOnLayoutChangeListener);
                          mOnLayoutChangeListener = null;
                  }

                  mMediaPlayer.stop();

                  mMediaPlayer.getVLCVout().detachViews();



          }
        public void volPlus(View view){}

        public void volMinus(View view){



        }
       public void  hide(View view){
                channelPanel.setVisibility(View.INVISIBLE);
                epgPanel.setVisibility(View.INVISIBLE);

       }
       public void showChAndEpg(View view){
               channelPanel.setVisibility(View.VISIBLE);
               epgPanel.setVisibility(View.VISIBLE);
        button.requestFocus();
           Calendar cal = Calendar.getInstance();
           Date date=cal.getTime();
           DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
           String formattedDate=dateFormat.format(date);

         try {
             e1.setText(epgModels.get(0).getDescription());
             e2.setText(formattedDate + " Channel: " + channelsModel.getName());
             e3.setText(epgModels.get(0).getTitle());
             e4.setText(epgModels.get(1).getTitle());
         }catch (Exception ex){

             e1.setText("NA");
             e2.setText(formattedDate + " Channel: " + channelsModel.getName());
             e3.setText("NA");
             e4.setText("NA");

         }

       }

      public void  showEpg(){
showProgress();
button.requestFocus();
                epgModels=new ArrayList<>();
                epgModels.clear();
              final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

              String url=URLs.url +"/player_api.php?username="+UserAccount.userAccount.getUserName()+"&password="+UserAccount.userAccount.getPassword()+"&action=get_short_epg&stream_id="+ channelsModel.streamId;
              AsyncHttpClient client = new AsyncHttpClient();
              client.get(url, new AsyncHttpResponseHandler() {
                      @Override
                      public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                              try {
                                      JSONObject object=new JSONObject(new String(responseBody));
                                      JSONArray array=object.getJSONArray("epg_listings");

                                      StringBuilder startTime;
                                      for(int i=0;i<array.length();i++){
                                              String id= array.getJSONObject(i).getString("id");
                                              String epgid=array.getJSONObject(i).getString("epg_id");
                                              String title=new String(Base64.decode(array.getJSONObject(i).getString("title"),0));
                                              String start=array.getJSONObject(i).getString("start");
                                              String end=array.getJSONObject(i).getString("end");
                                              String description=new String(Base64.decode(array.getJSONObject(i).getString("description"),0));

                                              String start_time=array.getJSONObject(i).getString("start_timestamp");
                                              String stop_time=array.getJSONObject(i).getString("stop_timestamp");
                                              int duration=Integer.parseInt(start_time) - Integer.parseInt(stop_time);
                                              String settime=simpleDateFormat.format(new Date((Long.parseLong(start_time) * 1000) + 3600000));
                                              long parseLong = Long.parseLong(stop_time);
                                              Long.signum(parseLong);
                                              String timeto=simpleDateFormat.format(new Date((parseLong * 1000) + 3600000));

                                              startTime=new StringBuilder(start);
                                              startTime=startTime.delete(0,start.indexOf(" "));
                                              epgModel = new EpgModel(id,epgid,title,start,end,description,start_time,stop_time,settime,startTime.toString(),""+duration);
                                              epgModels.add(epgModel);


                                      }



                                          runOnUiThread(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                          try {

                                                              Calendar cal = Calendar.getInstance();
                                                              Date date=cal.getTime();
                                                              DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                                                              String formattedDate=dateFormat.format(date);
                                                                  e1.setText(epgModels.get(0).getDescription());
                                                                  e2.setText(formattedDate+" Channel: "+channelsModel.getName());
                                                                  e3.setText(epgModels.get(0).getTitle());
                                                                  e4.setText(epgModels.get(1).getTitle());
                                                          }catch (Exception ex){

                                                                  e1.setText("NA");
                                                                  e2.setText("NA Channel: "+channelsModel.getName());
                                                                  e3.setText("NA");
                                                                  e4.setText("NA");


                                                          }

                                                          hideProgress();
                                                  }
                                          });


                              } catch (JSONException e) {
                                      e.printStackTrace();
                              }


                      }

                      @Override
                      public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                      }
              });

          timerTask = new TimerTask() {
              @Override
              public void run() {
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {

                          hide(null);
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


                if(KeyEvent.KEYCODE_DPAD_CENTER==keyCode || KeyEvent.KEYCODE_ENTER==keyCode) {
                    button.requestFocus();
                    showChAndEpg(null);
                }
                else if(KeyEvent.KEYCODE_DPAD_UP==keyCode ){
                       chPlus(null);

                       button.requestFocus();

                }else if(KeyEvent.KEYCODE_DPAD_DOWN==keyCode ){
                         button.requestFocus();
                        chMinus(null);
                }

                //}
                return super.onKeyDown(keyCode, event);

        }


        @Override
        public void onEvent(MediaPlayer.Event event) {
                if(event.type==MediaPlayer.Event.Buffering)
                {

                        System.out.println("BUffering");
                        if(event.getBuffering()<100){
                                if(progressDialog!=null && progressDialog.isShowing())
                                showProgress();

                        }else{
                                hideProgress();
                        }



                }else if(event.type==MediaPlayer.Event.Playing){
                        hideProgress();

                        System.out.println("Playing");


                }else if(event.type==MediaPlayer.Event.EncounteredError){
                  stopVlc();
                  playVlc();


                        System.out.println("ERROR");

                }


        }
public void showProgress(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading..");
        progressDialog.setMessage("Please Wait.");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


}
public void hideProgress(){
                if(progressDialog!=null)
                progressDialog.dismiss();

}
}
