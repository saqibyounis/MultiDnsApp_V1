package com.kamel.tivi.epg.epgadapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kamel.tivi.R;
import com.kamel.tivi.epg.domain.EPGEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EpgAdapterForLive extends BaseAdapter {
    Context context;
    List<EPGEvent> channelCategories;
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm a");

    public EpgAdapterForLive(Context context, List<EPGEvent> channelCategories){
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
        if(position==0){
            textView.setTextColor(Color.YELLOW);


        }
        String time="";
        if(position==0)
        {

            Calendar cal = Calendar.getInstance();
            Date date=cal.getTime();
            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String formattedDate=dateFormat.format(date);
            time=formattedDate;

        }else{

            time="";
        }
        textView.setText(time+" "+channelCategories.get(position).getTitle());
           textView.setTextSize(14);
        return convertView;


    }
}
