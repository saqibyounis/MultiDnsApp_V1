package com.kamel.tivi;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.kamel.tivi.userpref.UserSharedPref.getSharedPref;
import static com.kamel.tivi.userpref.UserSharedPref.getSharedPrefEditor;

public class LoginActivity extends AppCompatActivity {
      EditText username,password;
        Button login;
        ProgressBar progressBar;
        CheckBox rmb;

      @Override
    protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          requestWindowFeature(Window.FEATURE_NO_TITLE);

          getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                  WindowManager.LayoutParams.FLAG_FULLSCREEN);
          setContentView(R.layout.activity_login);
          SharedPreferences pref = getSharedPref(this);
          if (pref.getString("parentalpassword", null) != null) {


          } else {

              SharedPreferences.Editor editor = getSharedPrefEditor(LoginActivity.this);
              editor.putString("parentalpassword", "0000");
              editor.commit();

          }

          Dexter.withActivity(this)
                  .withPermissions(
                          Manifest.permission.CHANGE_NETWORK_STATE,
                          Manifest.permission.ACCESS_NETWORK_STATE
                  ).withListener(new MultiplePermissionsListener() {
              @Override
              public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

              @Override
              public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
          }).check();

          username = findViewById(R.id.username);
          password = findViewById(R.id.password);
          login = findViewById(R.id.login);

          rmb = findViewById(R.id.rmb);

          username.requestFocus();

          rmb.setChecked(true);
          rmb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                  if (isChecked) {

                      if (!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                          if (URLs.serverName.equalsIgnoreCase("PiTv")) {
                              SharedPreferences.Editor editor = getSharedPrefEditor(LoginActivity.this);
                              editor.putString("username", username.getText().toString());
                              editor.putString("password", password.getText().toString());
                              editor.commit();
                          } else if (URLs.serverName.equalsIgnoreCase("SwayTv")) {
                              SharedPreferences.Editor editor = getSharedPrefEditor(LoginActivity.this);
                              editor.putString("swaytvusername", username.getText().toString());
                              editor.putString("swaytvpassword", password.getText().toString());
                              editor.commit();


                          } else if (URLs.serverName.equalsIgnoreCase("HoursTv")) {
                              SharedPreferences.Editor editor = getSharedPrefEditor(LoginActivity.this);
                              editor.putString("hourstvusername", username.getText().toString());
                              editor.putString("hourstvpassword", password.getText().toString());
                              editor.commit();

                          }
                      } else {
                          rmb.setChecked(false);
                          Toast.makeText(LoginActivity.this, "Enter User name and password first.", Toast.LENGTH_SHORT).show();

                      }
                  }
              }
          });
          progressBar = findViewById(R.id.progressBar);

          if (URLs.serverName.equalsIgnoreCase("PiTv")) {
              Toast.makeText(this, "PiTv", Toast.LENGTH_SHORT).show();

              if (getSharedPref(this).getString("username", null) != null) {

                  username.setText(getSharedPref(this).getString("username", null));
                  password.setText(getSharedPref(this).getString("password", null));
                  rmb.setChecked(true);
                  login(null);

              }
          } else if (URLs.serverName.equalsIgnoreCase("SwayTv")) {
              Toast.makeText(this, "SwayTv", Toast.LENGTH_SHORT).show();

              if (getSharedPref(this).getString("swaytvusername", null) != null) {

                  username.setText(getSharedPref(this).getString("swaytvusername", null));

              password.setText(getSharedPref(this).getString("swaytvpassword", null));
              rmb.setChecked(true);
              login(null);
              }
                                    } else if (URLs.serverName.equalsIgnoreCase("HoursTv")) {
              Toast.makeText(this, "HoursTv", Toast.LENGTH_SHORT).show();
              if (getSharedPref(this).getString("hourstvusername", null) != null) {

              username.setText(getSharedPref(this).getString("hourstvusername", null));

              password.setText(getSharedPref(this).getString("hourstvpassword", null));
              rmb.setChecked(true);
              login(null);
              }
          }
    }
    public void login(View view){

         login.setEnabled(false);
          progressBar.setVisibility(View.VISIBLE);
        String username=this.username.getText().toString();
        String password=this.password.getText().toString();
     String url= URLs.url +"/get.php?username="+username+"&password="+password+"&type=m3u_plus&output=ts";
        System.out.println(url);

        if(rmb.isChecked() && URLs.serverName.equalsIgnoreCase("PiTv")) {
            SharedPreferences.Editor editor = getSharedPrefEditor(LoginActivity.this);
            editor.putString("username", this.username.getText().toString());
            editor.putString("password", this.password.getText().toString());
            editor.commit();
        } else  if(rmb.isChecked() && URLs.serverName.equalsIgnoreCase("HoursTv")) {
            SharedPreferences.Editor editor = getSharedPrefEditor(LoginActivity.this);
            editor.putString("hourstvusername", this.username.getText().toString());
            editor.putString("hourstvpassword", this.password.getText().toString());
            editor.commit();
        }else  if(rmb.isChecked() && URLs.serverName.equalsIgnoreCase("SwayTv")) {
            SharedPreferences.Editor editor = getSharedPrefEditor(LoginActivity.this);
            editor.putString("swaytvusername", this.username.getText().toString());
            editor.putString("swaytvpassword", this.password.getText().toString());
            editor.commit();
        }
     
        setUserData();

    }

public void setUserData(){
    FavouriteChannels.initializeDatabase(LoginActivity.this);

    String url=URLs.url+"/player_api.php?username="+username.getText().toString()+"&password="+password.getText().toString();

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

                UserAccount userAccount=new UserAccount(username,expDate,status,active,trail,maxConn,password.getText().toString());
                System.out.println(userAccount.getExpirationDate());
                setSeriesCategories();

            }else{

                Toast.makeText(LoginActivity.this, "Login faild!", Toast.LENGTH_SHORT).show();
                 showLgoinpanel();
            }


        } catch (JSONException e) {
            e.printStackTrace();
              showLgoinpanel();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
    showLgoinpanel();

    }
});


}
public void setSeriesCategories(){


    String url=URLs.url +"/player_api.php?username="+username.getText().toString()+"&password="+password.getText().toString()+"&action=get_series_categories";
    System.out.println(url);
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
                     showLgoinpanel();

            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            showLgoinpanel();
        }
    });


}
    public void setVodCategories(){
        String url=URLs.url +"/player_api.php?username="+username.getText().toString()+"&password="+password.getText().toString()+"&action=get_vod_categories";

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
                showLgoinpanel();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
             showLgoinpanel();
            }
        });


    }
    public void moveNext(){
        progressBar.setVisibility(View.GONE);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this,CategoryActivity.class));
                LoginActivity.this.finish();

            }
        });

    }

    public void setLiveCategories(){
        com.kamel.tivi.channelcategory.constants.Constants.channelCategories=new ArrayList<>();

          String url=URLs.url +"/player_api.php?username="+UserAccount.userAccount.getUserName()+"&password="+UserAccount.userAccount.getPassword()+"&action=get_live_categories";
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
        showLgoinpanel();

        }


    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
showLgoinpanel();
    }
});



      }

    public void showLgoinpanel(){


        Toast.makeText(LoginActivity.this, "Login faild!", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        login.setEnabled(true);
        username.setEnabled(true);
        password.setEnabled(true);

    }




}
