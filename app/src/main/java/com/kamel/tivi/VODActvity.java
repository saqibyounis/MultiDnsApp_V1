package com.kamel.tivi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.VideoView;

import com.kamel.tivi.channels.ChannelsModel;
import com.kamel.tivi.channels.favouritechannels.FavouriteChannels;
import com.kamel.tivi.network.URLs;
import com.kamel.tivi.player.exoplayer.ExoPlayerActivity;
//import com.kamel.kameltv.player.xplyer.XVideoPlayer;
//import com.kamel.kameltv.player.ijk.IJKVidoPlayear;
import com.kamel.tivi.useaccount.UserAccount;
import com.kamel.tivi.vod.Constats.Constants;
import com.kamel.tivi.vod.adapter.MovieAdapter;
import com.kamel.tivi.vod.model.MovieModel;
import com.kamel.tivi.vod.model.VodCategoryModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class VODActvity extends AppCompatActivity{
static GridView channelGrid;
private static List<MovieModel> channelList;
private long exitTime = 0;
//TODO this is my todo
private VideoView videoView;
private ProgressDialog progressDialog;
        String url;
static Context context;
SearchView channelSearch;
static MovieAdapter adapter;


@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vodactvity);
        showProgress();
        context=this;

        AsyncHttpClient client=new AsyncHttpClient();
        String url= URLs.url +"/player_api.php?username="+UserAccount.userAccount.getUserName()+"&password="+ UserAccount.userAccount.getPassword() +"&action=get_vod_streams";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
               String response=new String(responseBody);
                try {
                    JSONArray obj=new JSONArray(response);
                    for(int i=0;i<obj.length();i++){
                        String name=obj.getJSONObject(i).getString("name");
                        String streamId=obj.getJSONObject(i).getString("stream_id");
                        String icon=obj.getJSONObject(i).getString("stream_icon");
                        String categoryid=obj.getJSONObject(i).getString("category_id");
                        String extension=obj.getJSONObject(i).getString("container_extension");
                        for (int j=0;j<Constants.vodCategory.size();j++) {
                            if(Constants.vodCategory.get(j).getCategoryId().equalsIgnoreCase(categoryid)){
                                Constants.vodCategory.get(j).getMoviles().add(new MovieModel(name,streamId,icon,categoryid,extension));

                            }
                        }





                    }

                    initView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

           hideProgress();

            }
        });



        }

    private void initView() {
        channelSearch=findViewById(R.id.channelsearch);

        channelGrid=findViewById(R.id.moviegrid);
        if(Constants.vodCategory.size()>0) {

            adapter = new MovieAdapter(context, Constants.vodCategory.get(0).getMoviles());
            channelList=Constants.vodCategory.get(0).getMoviles();
            channelGrid.setAdapter(adapter);
            channelGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String url = URLs.url +"/movie/" + UserAccount.userAccount.getUserName() + "/" + UserAccount.userAccount.getPassword() + "/" + channelList.get(position).getStreaamId() + "." + channelList.get(position).getExtension();
                    Intent intent = new Intent(VODActvity.this, ExoPlayerActivity.class);
                    intent.putExtra("url", url);

                    startActivity(intent);

                }
            });

            channelSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    adapter.filter(newText);


                    return false;
                }
            });

            hideProgress();
        }else{
            Toast.makeText(context, "Category Emplty", Toast.LENGTH_SHORT).show();
            onBackPressed();

        }
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

    for(VodCategoryModel vodCategory: Constants.vodCategory){
        vodCategory.getMoviles().clear();


    }

    super.onBackPressed();
        }



    public static void updateChannelList(List<MovieModel> list){
        channelList=list;
        adapter=new MovieAdapter(context,list);
        channelGrid.setAdapter(adapter);
        System.out.println("INUPDATE"+list.size());
        adapter.notifyDataSetChanged();



    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chanelcontextmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // below variable info contains clicked item info and it can be null; scroll down to see a fix for it
        //  ChannelAdapter info = (ChannelAdapter) item.getMenuInfo();
        try {

            switch (item.getItemId()) {
                case R.id.addfavorite:
                    //hidTesteItem(info.position);
                    ChannelsModel m3UItem = (ChannelsModel) channelGrid.getSelectedItem();
                    FavouriteChannels.databaseHelper.addChannel(m3UItem);
                    Toast.makeText(context, m3UItem.getName() + " Favourite Added!" + channelGrid.getCheckedItemPosition(), Toast.LENGTH_SHORT).show();


                    return true;
                case R.id.removefavorite:
                    ChannelsModel m3UItemr = (ChannelsModel) channelGrid.getSelectedItem();
                    FavouriteChannels.databaseHelper.deletechannel(m3UItemr);
                    Toast.makeText(context, m3UItemr.getName() + " Removed from favourite!", Toast.LENGTH_SHORT).show();
                    return true;

                default:
                    return super.onContextItemSelected(item);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(context, "Not able to add this into favourite", Toast.LENGTH_SHORT).show();

        }
        return super.onContextItemSelected(item);

    }
    public void showProgress() {
        progressDialog = new ProgressDialog(VODActvity.this);
        progressDialog = ProgressDialog.show(VODActvity.this, "Please Wait",
                "Loading...", true);
    }

    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
