package com.kamel.tivi.series.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kamel.tivi.R;

import android.app.ProgressDialog;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kamel.tivi.SeriesActivity;
import com.kamel.tivi.series.SeriesCategoryAdapter;
import com.kamel.tivi.series.constants.Contants;
import com.kamel.tivi.series.model.SeriesModel;

import java.util.ArrayList;
import java.util.List;

public class SeriesCategoryFragment extends Fragment {
    ProgressDialog progressDialog;
    List<SeriesModel> channelList=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Wait..");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        View view =inflater.inflate(R.layout.fragment_channel_category,container,false);

        final GridView gridView=view.findViewById(R.id.grid);
        gridView.setNumColumns(1);
        gridView.setDrawSelectorOnTop(true);
        SeriesCategoryAdapter adapter=new SeriesCategoryAdapter(getActivity(),Contants.seriesCategory);
        gridView.setAdapter(adapter);
        gridView.setDrawSelectorOnTop(true);
       // gridView.setSelection(M3UListConstants.channelCategories.indexOf("USA CINEMA"));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                progressDialog=new ProgressDialog(getActivity());
                progressDialog.setMessage("Wait..");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                channelList=new ArrayList<>();
                System.out.println("SEries size "+channelList.size());
                channelList=Contants.seriesCategory.get(position).getSeriesModels();



               SeriesActivity.update(channelList);

                progressDialog.dismiss();
            }
        });
        progressDialog.dismiss();
        return view;

    }


}
