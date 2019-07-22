package com.kamel.tivi.vod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kamel.tivi.R;
import com.kamel.tivi.vod.model.VodCategoryModel;

import java.util.List;

public class VodAdapter extends BaseAdapter {
    Context context;
    List<VodCategoryModel> channelCategories;
    public VodAdapter(Context context, List<VodCategoryModel> channelCategories){
        this.context=context;
        this.channelCategories=channelCategories;

    }
    @Override
    public int getCount() {
        return channelCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return channelCategories.get(position);
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
            convertView = layoutInflater.inflate(R.layout.channelcatcard, null);
        }
        TextView textView=convertView.findViewById(R.id.textView2);
        textView.setText(channelCategories.get(position).getVategoryName());
        return convertView;    }
}
