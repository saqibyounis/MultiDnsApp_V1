package com.kamel.tivi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kamel.tivi.channelcategory.ChannelCategory;
import com.kamel.tivi.channels.favouritechannels.FavouriteChannels;
import com.kamel.tivi.network.URLs;
import com.kamel.tivi.series.constants.Contants;
import com.kamel.tivi.series.model.SeriesCategoryModel;
import com.kamel.tivi.useaccount.UserAccount;
import com.kamel.tivi.vod.Constats.Constants;
import com.kamel.tivi.vod.model.VodCategoryModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class RefreshActivity extends AppCompatActivity {
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);

         progressBar=findViewById(R.id.prog);
           login(null);

    }

    public void login(View view){

        progressBar.setVisibility(View.VISIBLE);
        String url= URLs.url+"/get.php?username="+UserAccount.userAccount.getUserName()+"&password="+UserAccount.userAccount.getPassword()+"&type=m3u_plus&output=ts";
        System.out.println(url);
      /*  AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"

                String string=new String(response);
                try {

                    M3UParser m3UParser=new M3UParser();
                   M3UListConstants.m3UItemsList= m3UParser.parseFile(string);
                    LinkedHashSet<String> hashSet = new LinkedHashSet<>(M3UListConstants.channelCategories);
                    ArrayList<String> newCategories=new ArrayList<>(hashSet);
                    M3UListConstants.channelCategories=newCategories;


                    FavouriteChannels.initializeDatabase(LoginActivity.this);
                    setUserData();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(LoginActivity.this, "Login Success full.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                login.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Login Faild!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });*/
        setUserData();

    }

    public void setUserData(){
        FavouriteChannels.initializeDatabase(RefreshActivity.this);

        String url= URLs.url+"/player_api.php?username="+UserAccount.userAccount.getUserName()+"&password="+UserAccount.userAccount.getPassword();

        AsyncHttpClient client =new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String resp=new String(responseBody);
                try {

                    JSONObject obj = new JSONObject(resp);

                    String mAuth=obj.getJSONObject("user_info").getString("auth");

                    if(mAuth.equalsIgnoreCase("1")){


                        String username=obj.getJSONObject("user_info").getString("username");
                        String trail=obj.getJSONObject("user_info").getString("is_trial");
                        String status=obj.getJSONObject("user_info").getString("status");
                        String active=obj.getJSONObject("user_info").getString("active_cons");
                        String expDate=obj.getJSONObject("user_info").getString("exp_date");
                        String maxConn=obj.getJSONObject("user_info").getString("max_connections");

                        UserAccount userAccount=UserAccount.userAccount;
                        System.out.println(userAccount.getExpirationDate());
                        setSeriesCategories();

                    }else{

                        Toast.makeText(RefreshActivity.this, "Login faild!", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });


    }
    public void setSeriesCategories(){
        String url= URLs.url+"/player_api.php?username="+UserAccount.userAccount.getUserName()+"&password="+UserAccount.userAccount.getPassword()+"&action=get_series_categories";

        AsyncHttpClient client =new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
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
                    setVodCategories();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }
    public void setVodCategories(){
        String url= URLs.url+"/player_api.php?username="+UserAccount.userAccount.getUserName()+"&password="+UserAccount.userAccount.getPassword()+"&action=get_vod_categories";

        AsyncHttpClient client =new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String resp=new String(responseBody);
                try {


                    Constants.vodCategory=new ArrayList<>();


                    JSONArray obj = new JSONArray(resp);
                    for(int i=0;i<obj.length();i++){
                        String categoryId=obj.getJSONObject(i).getString("category_id");
                        String categoryName=obj.getJSONObject(i).getString("category_name");
                        String parentId=obj.getJSONObject(i).getString("parent_id");
                        Constants.vodCategory.add(new VodCategoryModel(categoryId,categoryName,parentId));
                        System.out.println(obj.getJSONObject(i).getString("category_name"));
                    }
                    setLiveCategories();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }
    public void moveNext(){
        progressBar.setVisibility(View.GONE);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RefreshActivity.this.startActivity(new Intent(RefreshActivity.this,CategoryActivity.class));
                RefreshActivity.this.finish();

            }
        });

    }

    public void setLiveCategories(){
        com.kamel.tivi.channelcategory.constants.Constants.channelCategories=new ArrayList<>();

        String url= URLs.url+"/player_api.php?username="+UserAccount.userAccount.getUserName()+"&password="+UserAccount.userAccount.getPassword()+"&action=get_live_categories";
        System.out.println(url);
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String resp=new String(responseBody);
                try {
                    JSONArray array=new JSONArray(resp);
                    for(int i=0 ;i <array.length();i++){
                        String categoryid=array.getJSONObject(i).getString("category_id");
                        String categoryname=array.getJSONObject(i).getString("category_name" );
                        boolean locked=false;

                        if(categoryname.contains("ADULT")){
                            locked=true;
                        }

                        com.kamel.tivi.channelcategory.constants.Constants.channelCategories.add(new ChannelCategory(categoryname,categoryid,locked));





                    }
                    moveNext();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });



    }






}
