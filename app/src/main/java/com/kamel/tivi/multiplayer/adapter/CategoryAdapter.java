package com.kamel.tivi.multiplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kamel.tivi.R;
import com.kamel.tivi.multiplayer.mode.MultiPlayerModel;

import java.util.List;

public class   CategoryAdapter  extends BaseAdapter {
    List<MultiPlayerModel> channelList;
    Context context;
    public CategoryAdapter(Context context, List<MultiPlayerModel> list){
        this.channelList=list;
        this.context=context;

    }

    @Override
    public int getCount() {
        return channelList.size();
    }

    @Override
    public MultiPlayerModel getItem(int position) {
        return channelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.playeroptiondlg, null);
        }

        TextView textView=convertView.findViewById(R.id.pid);
        textView.setText(channelList.get(position).getName());

        //ImageView fav=convertView.findViewById(R.id.fav);

        /*for (M3UItem channel : FavouriteChannels.favList
             ) {
            if(channel.getItemName().equalsIgnoreCase(channelList.get(position).itemName))
            {
                System.out.println("in favourite item");
                fav.setVisibility(View.VISIBLE);
                fav.setImageDrawable(context.getResources().getDrawable(R.drawable.heart));



            }else{
                System.out.println("in not favourite ");
                fav.setVisibility(View.GONE);

            }

        }
        */


        return convertView;
    }




}
