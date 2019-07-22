package com.kamel.tivi.series.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kamel.tivi.R;
import com.kamel.tivi.series.model.EpisodeModel;

import java.util.List;

public class EpisodeAdapter extends BaseAdapter {
    List<EpisodeModel> list;
    Context context;

    public EpisodeAdapter(Context context, List<EpisodeModel> list){

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
            convertView = layoutInflater.inflate(R.layout.episodecard, null);
        }
        TextView textView=convertView.findViewById(R.id.epname);


        textView.setText(list.get(position).getTitle());



        return convertView;

    }
}
