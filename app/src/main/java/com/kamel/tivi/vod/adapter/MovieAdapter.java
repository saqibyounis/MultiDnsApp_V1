package com.kamel.tivi.vod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kamel.tivi.R;
import com.kamel.tivi.vod.model.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MovieAdapter extends BaseAdapter {
    Context context;
    List<MovieModel> list;
    List<MovieModel> list2;
    public MovieAdapter(Context context, List<MovieModel> channelCategories){
        this.context=context;
        this.list=channelCategories;
         this.list2=new ArrayList<>();
         this.list2.addAll(list);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.seriescard, null);
        }

        ImageView imageView=convertView.findViewById(R.id.seriesimage);
        TextView textView=convertView.findViewById(R.id.seriesname);


        if(list.get(position).getIcon().isEmpty()){

            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.imagefound_90x90));


        }else {
            try {
                Picasso.with(context).load(list.get(position).getIcon()).into(imageView);
            }catch (Exception ex){
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.imagefound_90x90));

            }

        }
        textView.setText(list.get(position).getName());



        return convertView;


    }
    // Filter Class
    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        System.out.println(charText);
        list.clear();
        if (charText.length() == 0) {
            list.addAll(list2);
        }
        else {
            for (MovieModel wp : list2) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    list.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
