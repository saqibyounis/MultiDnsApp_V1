package com.kamel.tivi.channels;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kamel.tivi.LiveTvActivity;
import com.kamel.tivi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class   ChannelAdapter  extends BaseAdapter {
   List<ChannelsModel> channelList;
   List<ChannelsModel> searhList;
    Context context;
    public ChannelAdapter(Context context, List<ChannelsModel> list){
       this.channelList=list;
       this.context=context;
        searhList=new ArrayList<>();
        searhList.addAll(channelList);

   }

    @Override
    public int getCount() {
        return channelList.size();
    }

    @Override
    public ChannelsModel getItem(int position) {
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
            convertView = layoutInflater.inflate(R.layout.channelcard, null);
        }

        ImageView imageView=convertView.findViewById(R.id.channelimage);


        if(channelList.get(position).getLogo().isEmpty()){
            imageView.setImageDrawable(context.getResources().getDrawable( R.drawable.noimg_90x90 ));
            System.out.println("Empty!");

        }else {
            try {
                Picasso.with(context).load(channelList.get(position).logo).into(imageView);
            } catch (Exception ex) {
                ex.printStackTrace();
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.noimg_90x90));


            }
        }
        //fav.setImageDrawable(context.getResources().getDrawable(R.drawable.favicon));


        if(LiveTvActivity.postionForChannelAdapter==position && LiveTvActivity.postionForChannelAdapter!=-1){
            ImageView play=convertView.findViewById(R.id.playimg);
            play.setVisibility(View.VISIBLE);
            Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show();


        }else{
            ImageView play=convertView.findViewById(R.id.playimg);
            play.setVisibility(View.INVISIBLE);


        }
        TextView textView=convertView.findViewById(R.id.channelname);
        textView.setText("         "+channelList.get(position).getName()+"         ");
                /*mageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        Toast.makeText(context, "Long Cli eckeed", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });*/
        textView.setSelected(true);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine(true);
        return convertView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        channelList.clear();
        if (charText.length() == 0) {
         channelList.addAll(searhList);
        }
        else {
            for (ChannelsModel wp : searhList) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    channelList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void showFavHeart(int position){


    }

    public void hidHeart(int positoin){


    }
}
