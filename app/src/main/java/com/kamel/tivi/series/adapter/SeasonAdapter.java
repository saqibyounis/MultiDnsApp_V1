package com.kamel.tivi.series.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kamel.tivi.R;
import com.kamel.tivi.series.model.SeasonModel;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SeasonAdapter extends BaseAdapter {
     List<SeasonModel> list;
     Context context;
    ExecutorService executorService;
    public SeasonAdapter(Context context, List<SeasonModel> list){
        executorService = Executors.newSingleThreadExecutor();
        this.context=context;
        this.list=list;
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
            convertView = layoutInflater.inflate(R.layout.seasoncard, null);
        }
        ImageView imageView=convertView.findViewById(R.id.seriesimage);
        TextView textView=convertView.findViewById(R.id.seriesname);

        System.out.println(list.get(position).getCover());



        if(list.get(position).getCover().isEmpty()){

            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.imagefound_90x90));


        }else {
            try {
                Picasso.with(context).load(list.get(position).getCover()).into(imageView);
            }catch (Exception ex){
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.noimg_90x90));

            }

        }
        URL url = null;

        if(imageView.getDrawable()==null){


            try {
                url = new URL(list.get(position).getCover());

                InputStream is = url.openConnection().getInputStream();

            } catch (Exception e) {
                e.printStackTrace();

                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.noimg_90x90));
            }
        }

        textView.setText(list.get(position).getName());



        return convertView;

    }
}
