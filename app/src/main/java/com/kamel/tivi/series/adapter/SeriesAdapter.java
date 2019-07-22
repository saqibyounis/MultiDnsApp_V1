package com.kamel.tivi.series.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kamel.tivi.R;
import com.kamel.tivi.series.model.SeriesModel;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class SeriesAdapter extends BaseAdapter {
    List<SeriesModel> seriesModels;
    Context context;

    public SeriesAdapter(Context context, List<SeriesModel> seriesModels){
        this.context=context;
        this.seriesModels=seriesModels;


    }

    @Override
    public int getCount() {
        return seriesModels.size();
    }

    @Override
    public Object getItem(int position) {
        return seriesModels.get(position);
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

        if(seriesModels.get(position).getCover().isEmpty()){

            imageView.setBackground(context.getResources().getDrawable(R.drawable.imagefound_90x90));


        }else {
         try {
             Picasso.with(context).load(seriesModels.get(position).getCover()).into(imageView);
         }catch (Exception ex){
             ex.printStackTrace();
             imageView.setBackground(context.getResources().getDrawable(R.drawable.imagefound_90x90));

         }



        }

        URL url = null;

        if(imageView.getDrawable()==null){


            try {
                url = new URL(seriesModels.get(position).getCover());

                InputStream is = url.openConnection().getInputStream();

            } catch (Exception e) {
                e.printStackTrace();

                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.noimg_90x90));
            }
        }
        textView.setText(seriesModels.get(position).getName());
return convertView;

    }
}
