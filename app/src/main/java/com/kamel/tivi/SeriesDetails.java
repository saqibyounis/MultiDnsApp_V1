package com.kamel.tivi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kamel.tivi.network.URLs;
import com.kamel.tivi.series.adapter.SeasonAdapter;
import com.kamel.tivi.series.constants.Contants;
import com.kamel.tivi.series.model.EpisodeModel;
import com.kamel.tivi.series.model.SeasonModel;
import com.kamel.tivi.useaccount.UserAccount;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SeriesDetails extends AppCompatActivity {
        ProgressDialog progressDialog;
        GridView gridView;
        SeasonAdapter seasonAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_details);
        gridView=findViewById(R.id.seriesgrid);
           showProgress();
        String sereisId=getIntent().getExtras().getString("seriesid");
        String url= URLs.url +"/player_api.php?username="+ UserAccount.userAccount.getUserName() +"&password="+UserAccount.userAccount.getPassword()+"&action=get_series_info&series_id="+sereisId;
        System.out.println(url);
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    System.out.println("sucsses "+new String(responseBody));

                    String resp=new String(responseBody);
                    JSONObject mainObject=new JSONObject(resp);
                    JSONArray jsonArray=mainObject.getJSONArray("seasons");
                    Contants.seasonModels=new ArrayList<>();
                     int j=1;
                    for(int i=0;i<jsonArray.length();i++){

                    String airDate=jsonArray.getJSONObject(i).getString("air_date");
                        String episodeCount=jsonArray.getJSONObject(i).getString("episode_count");
                        String id=jsonArray.getJSONObject(i).getString("id");
                        String name=jsonArray.getJSONObject(i).getString("name");
                        String seasonNumber=jsonArray.getJSONObject(i).getString("season_number");
                        String cover=jsonArray.getJSONObject(i).getString("cover");

                        JSONArray jsonArray1;
                        try{
                         jsonArray1 = mainObject.getJSONObject("episodes").getJSONArray("" + i);

                     }catch (Exception ex){
                            ex.printStackTrace();
                            try {
                                jsonArray1 = mainObject.getJSONObject("episodes").getJSONArray("" + j);
                            }catch (Exception ex2){


                                j++;
                                continue;

                            }
                            j++;
                   continue;
                     }
                        j++;
                         List<EpisodeModel> episodeModels=new ArrayList<>();
                  //    if(i!=0) {
                          for (int k = 0; k < jsonArray1.length(); k++) {
                              String eid = jsonArray1.getJSONObject(k).getString("id");
                              String title = jsonArray1.getJSONObject(k).getString("title");
                              String extention = jsonArray1.getJSONObject(k).getString("container_extension");
                              episodeModels.add(new EpisodeModel(eid, title, extention));

                          }


                          Contants.seasonModels.add(new SeasonModel(airDate, episodeCount, id, cover, seasonNumber, episodeModels, name));
                          System.out.println(episodeModels.size());
                          j++;
                      }

                //}

                    System.out.println("SeRies size "+ Contants.seasonModels.size());
                     initVIew();

                    hideProgress();

                } catch (Exception e) {
                    e.printStackTrace();
                         initVIew();
                    hideProgress();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                hideProgress();
            }
        });

    }

    public void initVIew(){
        seasonAdapter=new SeasonAdapter(SeriesDetails.this,Contants.seasonModels);
        gridView.setAdapter(seasonAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(SeriesDetails.this,EpisodeDetails.class);
                SeasonModel seasonModel= (SeasonModel) seasonAdapter.getItem(position);
                Contants.episodeModels=seasonModel.getEpisodeModels();
                startActivity(intent);
            }
        });


    }
    public void showProgress() {
        progressDialog = new ProgressDialog(SeriesDetails.this);
        progressDialog = ProgressDialog.show(SeriesDetails.this, "Please Wait",
                "Loading...", true);
    }

    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
