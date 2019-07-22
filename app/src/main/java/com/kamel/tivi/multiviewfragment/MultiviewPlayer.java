package com.kamel.tivi.multiviewfragment;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

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


public class MultiviewPlayer extends Fragment implements IVLCVout.OnNewVideoLayoutListener, AdapterView.OnItemClickListener {
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
    public String SAMPLE_URL="http://cuthecord.ddns.net:25461/live/Test0987/Apk1234/124124.ts";
    private LibVLC mLibVLC = null;
    public MediaPlayer mMediaPlayer = null;
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
    public int scalx=2;
    public int scaly=2;

    public MultiviewPlayer() {


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_multiview_player,container,false);

    //    desc=view.findViewById(R.id.cdes);
       // currentchannel=view.findViewById(R.id.currentchannel);
       // nextp=view.findViewById(R.id.nextchannel);
       // time=view.findViewById(R.id.time);
        frameLayout=view.findViewById(R.id.video_surface_frame);
        frameLayout.requestFocus();
        linearLayout=view.findViewById(R.id.epgpanel);
     //   channelGrid=view.findViewById(R.id.channelgrid);
       // channelGrid.setAdapter(Constants.channelAdapters);
     //   cpanel=view.findViewById(R.id.cpanel);
        //String url=getIntent().getExtras().getString("url");
       // LiveTvActivity.SAMPLE_URL=url;
        /*timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Your database code here
            linearLayout.setVisibility(View.GONE);

            }
        }, 2*60*1000);*/
// Since Java-8


       /* setEPgData();
        channelGrid.setSelection(Constants.position);
        channelGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                channelGrid.setSelection(position);
                CURRENT_CHANNEL_COUNTER=position+1;
                Constants.streamid=Constants.channelAdapters.getItem(position).getStreamId();
                setEPgData();
                String url="http://cuthecord.ddns.net:25461/live/"+ UserAccount.userAccount.getUserName()+"/"+UserAccount.userAccount.getPassword()+"/"+Constants.channelAdapters.getItem(position).getStreamId()+".ts";
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
                vlcVout.attachViews(MultiviewPlayer.this);

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

            }
        });*/
        final ArrayList<String> args = new ArrayList<>();
      //  args.add("--vout=android-display");  // Add this line!

        args.add("-vvv");
        mLibVLC = new LibVLC(getContext(), args);
        mMediaPlayer = new MediaPlayer(mLibVLC);

        mVideoSurfaceFrame = (FrameLayout) view.findViewById(R.id.video_surface_frame);
        if (USE_SURFACE_VIEW) {
            ViewStub stub = (ViewStub) view.findViewById(R.id.surface_stub);
            mVideoSurface = (SurfaceView) stub.inflate();
            if (ENABLE_SUBTITLES) {
                stub = (ViewStub) view.findViewById(R.id.subtitles_surface_stub);
                mSubtitlesSurface = (SurfaceView) stub.inflate();
                mSubtitlesSurface.setZOrderMediaOverlay(true);
                mSubtitlesSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            }
            mVideoView = mVideoSurface;
        } else {
            ViewStub stub = (ViewStub) view.findViewById(R.id.texture_stub);


            mVideoTexture = (TextureView) stub.inflate();
            mVideoView = mVideoTexture;
        }


        mVideoSurfaceFrame = (FrameLayout) view.findViewById(R.id.video_surface_frame);


fragStart();
        return view;
    }

    public void fragStart(){
        final IVLCVout vlcVout = mMediaPlayer.getVLCVout();
        if (mVideoSurface != null) {
            vlcVout.setVideoView(mVideoSurface);
            if (mSubtitlesSurface != null)
                vlcVout.setSubtitlesView(mSubtitlesSurface);
        } else
            vlcVout.setVideoView(mVideoTexture);
        vlcVout.attachViews(this);

        Media media = new Media(mLibVLC, Uri.parse(SAMPLE_URL));
        media.setHWDecoderEnabled(true, false);
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
        int sw = getActivity().getWindow().getDecorView().getWidth()/scalx;
        int sh = getActivity().getWindow().getDecorView().getHeight()/scaly;

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

        String url= URLs.url+"/player_api.php?username="+UserAccount.userAccount.getUserName()+"&password="+UserAccount.userAccount.getPassword()+"&action=get_short_epg&stream_id="+ com.kamel.tivi.epg.model.constants.Constants.streamid;
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

    public void initView(View view) {
        if(cpanel.getVisibility()==View.GONE && cPanelFlag)
            cpanel.setVisibility(View.VISIBLE);

        try {
            currentchannel.setText("Now: " + new String(Base64.decode(Constants.epgModels.get(0).getTitle(), 0)));
            nextp.setText("Next: " + new String(Base64.decode(Constants.epgModels.get(1).getTitle(), 0)));
            desc.setText("Description: " + new String(Base64.decode(Constants.epgModels.get(0).getDescription(), 0)));
            time.setText("Started at " + Constants.epgModels.get(0).getSettime() + " End at " + Constants.epgModels.get(0).getTimeto());
        } catch (Exception ex) {

            currentchannel.setText("Now: NA");
            nextp.setText("Next: NA");
            desc.setText("Description: NA");

        }
        System.out.println("CLICK");
        //  if(linearLayout.getVisibility()==View.GONE) {
        linearLayout.setVisibility(View.VISIBLE);
        //  channelGrid.setVisibility(View.VISIBLE);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
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




    public void close(View view){
        timerTask.cancel();
        linearLayout.setVisibility(View.GONE);
        cpanel.setVisibility(View.GONE);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        channelGrid.setSelection(position);
        Constants.streamid=Constants.channelAdapters.getItem(position).getStreamId();
        setEPgData();
        String url="http://cuthecord.ddns.net:25461/live/"+UserAccount.userAccount.getUserName()+"/"+UserAccount.userAccount.getPassword()+"/"+Constants.channelAdapters.getItem(position).getStreamId()+".ts";
        SAMPLE_URL=url;
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
        vlcVout.attachViews(MultiviewPlayer.this);

        Media media = new Media(mLibVLC, Uri.parse(SAMPLE_URL));
        mMediaPlayer.setMedia(media);
        media.release();
        mMediaPlayer.play();
    // mMediaPlayer.getVLCVout().setWindowSize(300,200);
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


    public void stop() {

        if (mOnLayoutChangeListener != null) {
            mVideoSurfaceFrame.removeOnLayoutChangeListener(mOnLayoutChangeListener);
            mOnLayoutChangeListener = null;
        }

        mMediaPlayer.stop();

        mMediaPlayer.getVLCVout().detachViews();

    }


}
