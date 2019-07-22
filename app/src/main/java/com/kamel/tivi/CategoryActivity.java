package com.kamel.tivi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;
import com.kamel.tivi.category.CategoryAdapter;
import com.kamel.tivi.category.CategoryModel;
import com.kamel.tivi.channels.favouritechannels.FavouriteChannels;
import com.kamel.tivi.multiplayer.mode.MultiPlayerModel;
import com.kamel.tivi.network.URLs;
import com.kamel.tivi.series.constants.Contants;
import com.kamel.tivi.series.model.SeriesCategoryModel;
import com.kamel.tivi.useaccount.UserAccount;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CategoryActivity extends AppCompatActivity {

    TextView cinfo;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_category);
        final GridView gridView=findViewById(R.id.catgrid);
       // gridView.setLayoutParams(new GridView.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT));
      cinfo=findViewById(R.id.cinfo);
      try {
          Tovuti.from(this).monitor(new Monitor.ConnectivityListener() {
              @Override
              public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast) {
                  // TODO: Handle the connection...
                  String info;

                  if (isFast) {
                      info = "Network is Fast ";


                  } else {
                      info = "Network is Slow ";

                  }

                  switch (connectionType) {

                      case 0:
                          info += "Network type Mobile";
                          break;


                      case 1:
                          info += "Network type Wifi";
                          break;
                      default:
                         // info += "Network type Unknown";
                          break;

                  }

                  cinfo.setText(info);


              }
          });

      }catch (Exception ex){


      }
        final List<CategoryModel> catnames=new ArrayList<>();
      if(URLs.serverName.equalsIgnoreCase("HoursTv")){

          FavouriteChannels.initializeDatabaseHours(this);

      }else if(URLs.serverName.equalsIgnoreCase("PiTv")){

          FavouriteChannels.initializeDatabase(this);

      }else {
          FavouriteChannels.initializeDatabaseSway(this);


      }
        catnames.add(new CategoryModel(R.drawable.livetv_90x90,"Live Tv"));
        catnames.add(new CategoryModel(R.drawable.favchannelicon_90x90,"Favourites"));
        catnames.add(new CategoryModel(R.drawable.vod_90x90,"Vod"));
        catnames.add(new CategoryModel(R.drawable.series_90x90,"Series"));
        catnames.add(new CategoryModel(R.drawable.multiview90x90,"MultiView"));
        catnames.add(new CategoryModel(R.drawable.refresh_90x90,"Refresh"));

        catnames.add(new CategoryModel(R.drawable.speedtest,"Speed Test"));

        catnames.add(new CategoryModel(R.drawable.cleaner_90x90,"Cleaner"));
        catnames.add(new CategoryModel(R.drawable.youtube_90x90,"Youtube"));


        catnames.add(new CategoryModel(R.drawable.appstore90x90,"Appstore"));
        catnames.add(new CategoryModel(R.drawable.vpn_90x90,"VPN"));

        catnames.add(new CategoryModel(R.drawable.ondemand,"On Demand"));

        catnames.add(new CategoryModel(R.drawable.account_90x90,"Account"));

        catnames.add(new CategoryModel(R.drawable.settings_90x90,"Settings"));

       // catnames.add(new CategoryModel(R.drawable.tvguide,"TvGuide"));
        final CategoryAdapter adapter=new CategoryAdapter(this,catnames);
        gridView.setNumColumns(2);
        gridView.setSelection(1);
        gridView.setAdapter(adapter);
        gridView.setDrawSelectorOnTop(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                CategoryModel model=catnames.get(position);
                String name=model.getName();
                switch (name){
                    case "On Demand":
                        if(appInstalledOrNot("com.royaltytv.ondemand")){
                            Intent intent =getPackageManager().getLaunchIntentForPackage("com.royaltytv.ondemand");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_MAIN);
                            startActivity(intent);
                        }else{
                            String url = "http://thetivi.com/royaltytv/vod.apk";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);

                        }break;
                    case "Live Tv":
                         startActivity(new Intent(CategoryActivity.this,LiveTvActivity.class));
                          break;
                    case "Series":




                        ArrayList<MultiPlayerModel> catelist=new ArrayList<>();
                        catelist.add(new MultiPlayerModel("UnCategorized"));

                        catelist.add(new MultiPlayerModel("Categories"));
                        com.kamel.tivi.multiplayer.adapter.CategoryAdapter adapter2=new com.kamel.tivi.multiplayer.adapter.CategoryAdapter(CategoryActivity.this,catelist);

                        final Dialog dialog1=new Dialog(CategoryActivity.this);
                        dialog1.setContentView(R.layout.multiviewchanneldialog);
                        GridView gridView2=dialog1.findViewById(R.id.channelgrid);
                        gridView2.setAdapter(adapter2);
                        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if(position==0){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showProgress();
                                        }
                                    });

/*

                                    dialog1.dismiss();
                                    startActivity(new Intent(CategoryActivity.this, MultiviewAcr.class));
*/
                                    AsyncHttpClient client2=new AsyncHttpClient();
                                    String uncaturl=URLs.url +"/player_api.php?username="+UserAccount.userAccount.getUserName() +"&password="+UserAccount.userAccount.getPassword()+"&action=get_series";
                                    ;
                                    client2.get(uncaturl, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            String resp=new String(responseBody);
                                            try {


                                                Contants.seriesCategory=new ArrayList<>();


                                                JSONArray obj = new JSONArray(resp);
                                                for(int i=0;i<obj.length();i++){
                                                    String category_id=obj.getJSONObject(i).getString("category_id");
                                                    String category_name=obj.getJSONObject(i).getString("name");;
                                                    String parent_id="";//obj.getJSONObject(i).getString("");

                                                    Contants.seriesCategory.add(new SeriesCategoryModel(category_id,category_name,parent_id));
                                                    System.out.println(obj.getJSONObject(i).getString("category_id"));


                                                }
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        hideProgress();
                                                    }
                                                });
                                                dialog1.dismiss();
                                                startActivity(new Intent(CategoryActivity.this,SeriesActivity.class));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            //    showLgoinpanel();
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        hideProgress();
                                                    }
                                                });
                                                dialog1.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    hideProgress();
                                                    dialog1.dismiss();
                                                }
                                            });
                                        }
                                    });


                                }else {

                                    /*dialog1.dismiss();
                                    startActivity(new Intent(CategoryActivity.this,SeriesActivity.class));
*/
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showProgress();
                                        }
                                    });
                                    AsyncHttpClient client2=new AsyncHttpClient();
                                    String uncaturl=URLs.url +"/player_api.php?username="+UserAccount.userAccount.getUserName() +"&password="+UserAccount.userAccount.getPassword()+"&action=get_series_categories";;
                                    client2.get(uncaturl, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            String resp=new String(responseBody);
                                            try {


                                                Contants.seriesCategory=new ArrayList<>();


                                                JSONArray obj = new JSONArray(resp);
                                                for(int i=0;i<obj.length();i++){
                                                    String category_id=obj.getJSONObject(i).getString("category_id");
                                                    String category_name=obj.getJSONObject(i).getString("category_name");;
                                                    String parent_id=obj.getJSONObject(i).getString("parent_id");

                                                    Contants.seriesCategory.add(new SeriesCategoryModel(category_id,category_name,parent_id));
                                                    System.out.println(obj.getJSONObject(i).getString("category_id"));


                                                }
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        hideProgress();
                                                    }
                                                });
                                                dialog1.dismiss();
                                                startActivity(new Intent(CategoryActivity.this,SeriesActivity.class));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                //    showLgoinpanel();
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        hideProgress();
                                                    }
                                                });
                                                dialog1.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    hideProgress();
                                                    dialog1.dismiss();
                                                }
                                            });
                                        }
                                    });


                                }
                            }
                        });

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog1.show();

                            }
                        });
                        break;
                    case "Vod":

                        startActivity(new Intent(CategoryActivity.this,VODActvity.class));
                        break;

                    case "Account":

                        startActivity(new Intent(CategoryActivity.this,AccountActvity.class));
                        break;

                    case "Speed Test":

                        if(appInstalledOrNot("com.netflix.Speedtest")){
                            Intent intent =getPackageManager().getLaunchIntentForPackage("com.netflix.Speedtest");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_MAIN);
                            startActivity(intent);
                        }else{
                            String url = "http://thetivi.com/kensapks/speedtest.apk";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);

                        }
                        break;

                    case "Settings":
                        startActivity(new Intent(CategoryActivity.this,SettingsActivity.class));

                        break;
                    case "MultiView":
                        ArrayList<MultiPlayerModel> list=new ArrayList<>();
                        list.add(new MultiPlayerModel("Exo Player"));
                        list.add(new MultiPlayerModel("VLC Player"));
                        com.kamel.tivi.multiplayer.adapter.CategoryAdapter adapter1=new com.kamel.tivi.multiplayer.adapter.CategoryAdapter(CategoryActivity.this,list);

                        final Dialog dialog=new Dialog(CategoryActivity.this);
                        dialog.setContentView(R.layout.multiviewchanneldialog);
                        GridView gridView1=dialog.findViewById(R.id.channelgrid);
                        gridView1.setAdapter(adapter1);
                        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if(position==0){


                                    dialog.dismiss();
                                    startActivity(new Intent(CategoryActivity.this, MultiviewAcr.class));


                                }else {

                                    dialog.dismiss();
                                    startActivity(new Intent(CategoryActivity.this, MultiviewAcr2.class));



                                }
                            }
                        });

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();

                            }
                        });
                        break;
                    case "Favourites":
                        startActivity(new Intent(CategoryActivity.this,FavouriteActivity.class));


                         break;
                    case "Refresh":
                        startActivity(new Intent(CategoryActivity.this,RefreshActivity.class));
                           finish();

                        break;
                    case "Cleaner":
                        if(appInstalledOrNot("com.yunos.tv.defensor")){
                            Intent intent =getPackageManager().getLaunchIntentForPackage("com.yunos.tv.defensor");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_MAIN);
                            startActivity(intent);
                        }else{
                            String url = "http://thetivi.com/kensapks/rocketclean.apk";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);

                        }


                        break;
                    case "Youtube":
                         if(appInstalledOrNot("com.google.android.youtube.tv")){
                             Intent intent =getPackageManager().getLaunchIntentForPackage("com.google.android.youtube.tv");
                             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                             intent.setAction(Intent.ACTION_MAIN);

                             startActivity(intent);

                         }else{
                             String url = "http://thetivi.com/kensapks/youtubetv.apk";
                             Intent i = new Intent(Intent.ACTION_VIEW);
                             i.setData(Uri.parse(url));
                             startActivity(i);

                         }

                        break;
                    case "Appstore":
                        if(appInstalledOrNot("cm.aptoidetv.pt")){
                            Intent intent =getPackageManager().getLaunchIntentForPackage("cm.aptoidetv.pt");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_MAIN);

                            startActivity(intent);

                        }else{
                            String url = "http://thetivi.com/kensapks/aptoidetv.apk";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);

                        }

                        break;

                    case "VPN":

                        if(appInstalledOrNot("com.simplona.vpn.proxy.server.fire.tablets.tv")){
                            Intent intent =getPackageManager().getLaunchIntentForPackage("com.simplona.vpn.proxy.server.fire.tablets.tv");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_MAIN);

                            startActivity(intent);

                        }else{
                            String url = "http://thetivi.com/kensapks/freevpn.apk";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);

                        }
                        break;


                }

                adapter.notifyDataSetChanged();
            }
        });

    }
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    @Override
    protected void onStop() {
     //   Tovuti.from(this).stop();
        super.onStop();
    }


    public void showProgress(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading.");
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


    }

    public void hideProgress(){
        progressDialog.hide();

    }
}
