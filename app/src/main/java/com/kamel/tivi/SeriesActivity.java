package com.kamel.tivi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kamel.tivi.network.URLs;
import com.kamel.tivi.series.adapter.SeriesAdapter;
import com.kamel.tivi.series.constants.Contants;
import com.kamel.tivi.series.model.SeriesCategoryModel;
import com.kamel.tivi.series.model.SeriesModel;
import com.kamel.tivi.useaccount.UserAccount;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SeriesActivity extends AppCompatActivity {
private long exitTime = 0;
//TODO this is my todo
private ProgressDialog progressDialog;
static Context context;
List<SeriesModel> seriesModels;
static SeriesAdapter seriesAdapter;

    static GridView seriesGrid;
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           showProgress();
        setContentView(R.layout.activity_series);
        context=this;
      seriesModels=new ArrayList<>();
    seriesGrid=findViewById(R.id.seriesgrid);
      String url= URLs.url +"/player_api.php?username="+ UserAccount.userAccount.getUserName() +"&password="+UserAccount.userAccount.getPassword()+"&action=get_series";
    AsyncHttpClient client=new AsyncHttpClient();
    client.get(url, new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            try {

                String resp=new String(responseBody);
                JSONArray obj = new JSONArray(resp);
                for(int i=0;i<obj.length();i++){
                    String name=obj.getJSONObject(i).getString("name");
                    String category_id=obj.getJSONObject(i).getString("category_id");

                    String series_id=obj.getJSONObject(i).getString("series_id");
                    String cover=obj.getJSONObject(i).getString("cover");
                    String plot=obj.getJSONObject(i).getString("plot");
                    String genre=obj.getJSONObject(i).getString("genre");
                    seriesModels.add(new SeriesModel(name,series_id,cover,plot,genre));
                    for(int i2=0;i2<Contants.seriesCategory.size();i2++){
                        if(category_id.equalsIgnoreCase(Contants.seriesCategory.get(i2).getCategoryId())){
                            Contants.seriesCategory.get(i2).getSeriesModels().add(new SeriesModel(name,series_id,cover,plot,genre));
                        }


                    }
                }
                System.out.println(seriesModels.size());
                hideProgress();
             initView();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            hideProgress();
        }
    });



        }
@Override
protected void onPause() {
        super.onPause();
        }

@Override
protected void onResume() {
        super.onResume();
        }

public void onBackPressed() {
    for(SeriesCategoryModel seriesCategory: Contants.seriesCategory)
    {
        seriesCategory.getSeriesModels().clear();

    }

        super.onBackPressed();
        }



public void showProgress() {
        progressDialog = new ProgressDialog(SeriesActivity.this);
        progressDialog = ProgressDialog.show(SeriesActivity.this, "Please Wait",
        "Loading...", true);
        }

public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
        progressDialog.dismiss();
        }
        }
public void initView(){
seriesAdapter=new SeriesAdapter(SeriesActivity.this,Contants.seriesCategory.get(0).getSeriesModels());
seriesGrid.setAdapter(seriesAdapter);
seriesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SeriesModel seriesModel= (SeriesModel) seriesAdapter.getItem(position);
            Intent intent=new Intent(context,SeriesDetails.class);
             intent.putExtra("seriesid",seriesModel.getSeriesId());
             startActivity(intent);

    }
});
}
public static void update(List<SeriesModel> seriesModels){
    seriesAdapter=new SeriesAdapter(context,seriesModels);
    seriesGrid.setAdapter(seriesAdapter);
    seriesAdapter.notifyDataSetChanged();

}
 }
