package com.kamel.tivi.category;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kamel.tivi.R;

import java.util.List;

public class CategoryAdapterRecycler extends RecyclerView.Adapter<CategoryAdapterRecycler.MyViewHolder> {
    private List<CategoryModel> categoryModels;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView catimage, year, genre;

        public MyViewHolder(View view) {
            super(view);
            catimage=(ImageView)view.findViewById(R.id.imageView);

        }
    }


    public CategoryAdapterRecycler(List<CategoryModel> clist,Context context) {
        this.categoryModels = clist;
this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categorycard, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
   /*     Movie movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getYear());*/

        Drawable myIcon = context.getResources().getDrawable( categoryModels.get(position).imageId );
        holder.catimage.setImageDrawable(myIcon);
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }
}
