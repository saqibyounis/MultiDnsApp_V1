package com.kamel.tivi.category;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kamel.tivi.R;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
       Context context;
       List<CategoryModel> categoryList;
    public CategoryAdapter(Context context,List<CategoryModel> list){

        this.context=context;
        this.categoryList=list;

    }


    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return  categoryList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.categorycard, null);
        }
        ImageView imageView=convertView.findViewById(R.id.imageView);
        Drawable myIcon = context.getResources().getDrawable( categoryList.get(position).imageId );
        imageView.setBackground(myIcon);
        return convertView;
    }
}
