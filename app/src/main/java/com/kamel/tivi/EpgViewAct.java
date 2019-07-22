package com.kamel.tivi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.common.collect.Maps;
import com.kamel.tivi.channelcategory.constants.Constants;
import com.kamel.tivi.channels.ChannelsModel;
import com.kamel.tivi.epg.EPG;
import com.kamel.tivi.epg.EPGClickListener;
import com.kamel.tivi.epg.EPGData;
import com.kamel.tivi.epg.domain.EPGChannel;
import com.kamel.tivi.epg.domain.EPGEvent;
import com.kamel.tivi.epg.misc.EPGDataImpl;
import com.kamel.tivi.network.URLs;
import com.kamel.tivi.useaccount.UserAccount;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cz.msebera.android.httpclient.Header;

public class EpgViewAct extends AppCompatActivity {

    private static EPG epg;

   static HashMap<EPGChannel, List<EPGEvent>> result;
    static List<EPGEvent> epgEvents=new ArrayList<>();
    private static Activity context;
    private ProgressDialog progressDialog;
    static ExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epg_view);
        context=this;

        executorService = Executors.newSingleThreadExecutor();

        showProgress();
        String url = URLs.url +"/player_api.php?username=" + UserAccount.userAccount.getUserName() + "&password=" + UserAccount.userAccount.getPassword() + "&action=get_live_streams";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    JSONArray array = new JSONArray(new String(responseBody));
                    for (int i = 0; i < array.length(); i++) {
                        String name = array.getJSONObject(i).getString("name");
                        String streamId = array.getJSONObject(i).getString("stream_id");
                        String streamIcon = array.getJSONObject(i).getString("stream_icon");
                        String epgChannelId = array.getJSONObject(i).getString("epg_channel_id");
                        String categoryId = array.getJSONObject(i).getString("category_id");
                        System.out.println("Stream id "+streamId);
                        for (int j = 0; j < Constants.channelCategories.size(); j++) {
                            if (Constants.channelCategories.get(j).getCategoryId().equalsIgnoreCase(categoryId)) {
                                Constants.channelCategories.get(j).getChanneles().add(new ChannelsModel(name, streamId, epgChannelId, categoryId, "", streamIcon));


                            }


                        }
                        //initView();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgress();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        updateChannelList(Constants.channelCategories.get(0).getChanneles());

                    }
                });

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
               hideProgress();
            }
        });



        epg = (EPG) findViewById(R.id.epg);
        epg.setEPGClickListener(new EPGClickListener() {
            @Override
            public void onChannelClicked(int channelPosition, EPGChannel epgChannel) {
                Toast.makeText(EpgViewAct.this, epgChannel.getName() + " clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventClicked(int channelPosition, int programPosition, EPGEvent epgEvent) {
                Toast.makeText(EpgViewAct.this, epgEvent.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResetButtonClicked() {
                epg.recalculateAndRedraw(true);
            }
        });


    }

    @Override
    protected void onDestroy() {
        if (epg != null) {
            epg.clearEPGImageCache();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
          result.clear();
          result=null;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class AsyncLoadEPGData extends AsyncTask<Void, Void, EPGData> {

        EPG epg;

        public AsyncLoadEPGData(EPG epg) {
            this.epg = epg;
        }

        @Override
        protected EPGData doInBackground(Void... voids) {

            return new EPGDataImpl(result);
        }

        @Override
        protected void onPostExecute(EPGData epgData) {
            epg.setEPGData(epgData);
            epg.recalculateAndRedraw(false);


        }
    }



    public static void completeEpg(){


        new AsyncLoadEPGData(epg).execute();



    }





    public static void updateChannelList(final List<ChannelsModel> list){



     /*   if (epg != null) {
            epg.clearEPGImageCache();
        }*/
        final ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Loading");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        epgEvents=new ArrayList<>();
        result = Maps.newLinkedHashMap();


        System.out.println(list.size());
 
             executorService.execute(new Runnable() {
                 @Override
                 public void run() {
                     System.out.println("size "+list.size());
                     for (final ChannelsModel channelmodel : list) {

                         final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                         String url =URLs.url+"/player_api.php?username="+UserAccount.userAccount.getUserName()+"&password="+UserAccount.userAccount.getPassword()+"&action=get_short_epg&stream_id="+channelmodel.streamId;
                         System.out.println(url);

                         SyncHttpClient client = new SyncHttpClient();
                         client.get(url, new JsonHttpResponseHandler() {
                             @Override
                             public void onStart() {
                                 // you can do something here before request starts
                             }

                             @Override
                             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                 try {
                                     JSONObject object = response;
                                     JSONArray array = object.getJSONArray("epg_listings");

                                     for (int i = 0; i < array.length(); i++) {
                                         String id = array.getJSONObject(i).getString("id");
                                         String epgid = array.getJSONObject(i).getString("epg_id");
                                         String title = array.getJSONObject(i).getString("title");
                                         String start = array.getJSONObject(i).getString("start");
                                         String end = array.getJSONObject(i).getString("end");
                                         String description = array.getJSONObject(i).getString("description");

                                         String start_time = array.getJSONObject(i).getString("start_timestamp");
                                         String stop_time = array.getJSONObject(i).getString("stop_timestamp");
                                         int duration = Integer.parseInt(start_time) - Integer.parseInt(stop_time);
                                         String settime = simpleDateFormat.format(new Date((Long.parseLong(start_time) * 1000) + 3600000));
                                         long parseLong = Long.parseLong(stop_time);
                                         Long.signum(parseLong);
                                         String timeto = simpleDateFormat.format(new Date((parseLong * 1000) + 3600000));

                                         EPGEvent epgEvent = new EPGEvent((Long.parseLong(start_time) * 1000) + 3600000, (parseLong * 1000) + 3600000, new String(Base64.decode(title, 0)), settime);
                                         epgEvents.add(epgEvent);
                                         System.out.println(epgEvent.getTitle());


                                     }

                                 } catch (Exception ex) {

                                     ex.printStackTrace();
                                     progressDialog.dismiss();
                                 }

                                 result.put(new EPGChannel(channelmodel.getLogo(), channelmodel.getName(), ""), epgEvents);

                             }

                             @Override
                             public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                                 // handle failure here
                                 progressDialog.dismiss();
                             }

                         });

                     }

                     context.runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             progressDialog.dismiss();
                         }
                     });

                     completeEpg();


                 }
             });





        }

    public void showProgress() {
     try {
         progressDialog = new ProgressDialog(EpgViewAct.this);
         progressDialog = ProgressDialog.show(EpgViewAct.this, "Please Wait",
                 "Loading...", true);
     }catch (Exception ex){ex.printStackTrace();}
     }

    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


}
