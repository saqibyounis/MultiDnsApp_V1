package com.kamel.tivi.channelcategory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kamel.tivi.R;

import java.util.List;

public class ChannelCategoryAdapter  extends BaseAdapter {
Context context;
List<ChannelCategory> channelCategories;
public ChannelCategoryAdapter(Context context, List<ChannelCategory> channelCategories){
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
        RelativeLayout relativeLayout=convertView.findViewById(R.id.parent);
        TextView textView=convertView.findViewById(R.id.textView2);
        textView.setText(channelCategories.get(position).getName());

        return convertView;


}
}
