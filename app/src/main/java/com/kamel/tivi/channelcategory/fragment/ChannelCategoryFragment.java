package com.kamel.tivi.channelcategory.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.kamel.tivi.LiveTvActivity;
import com.kamel.tivi.R;
import com.kamel.tivi.channelcategory.ChannelCategory;
import com.kamel.tivi.channelcategory.ChannelCategoryAdapter;
import com.kamel.tivi.channelcategory.constants.Constants;
import com.kamel.tivi.channels.ChannelsModel;
import com.kamel.tivi.userpref.UserSharedPref;

import java.util.ArrayList;
import java.util.List;

public class ChannelCategoryFragment extends Fragment {
    ProgressDialog progressDialog;
     List<ChannelsModel> channelList=null;
    ChannelCategory channelCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */

         /* progressDialog=new ProgressDialog(getActivity());
          progressDialog.setMessage("Wait..");
          progressDialog.setCancelable(false);
          progressDialog.setCanceledOnTouchOutside(false);
          progressDialog.show();*/
        View view =inflater.inflate(R.layout.fragment_channel_category,container,false);

        final GridView gridView=view.findViewById(R.id.grid);
         gridView.setNumColumns(1);
         gridView.setDrawSelectorOnTop(true);
        ChannelCategoryAdapter adapter=new ChannelCategoryAdapter(getActivity(), Constants.channelCategories);
        gridView.setAdapter(adapter);
         gridView.setDrawSelectorOnTop(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                channelList=new ArrayList<>();
                channelCategory = Constants.channelCategories.get(position);
                if(channelCategory.isLocked())
               {

                   SharedPreferences pref= UserSharedPref.getSharedPref(getContext());
                   final String currentPw=pref.getString("parentalpassword",null);
                   final Dialog dialog=new Dialog(getContext());
                   dialog.setContentView(R.layout.parentalcontrolepw);
                   final TextView pw=dialog.findViewById(R.id.pw2);
                   pw.requestFocus();
                   Button button=dialog.findViewById(R.id.login);
                   button.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           if(pw.getText().toString().equalsIgnoreCase(currentPw)){

                     /*          for (M3UItem m3UItem : M3UListConstants.m3UItemsList) {


                                   if (channelCategory.getName().equals(m3UItem.getGroup())) {
                                       //channelList.add(m3UItem);


                                   }

                               }
*/

                     dialog.dismiss();
                               LiveTvActivity.updateChannelList(channelCategory.getChanneles());


                           }else{
                               Toast.makeText(getContext(), "Password wrong!", Toast.LENGTH_SHORT).show();

                           }
                       }
                   });

               dialog.show();


               }else {
                    /*
                   for (M3UItem m3UItem : M3UListConstants.m3UItemsList) {


                       if (channelCategory.getName().equals(m3UItem.getGroup())) {
                           //channelList.add(m3UItem);

                       }

                   }
*/
                   LiveTvActivity.updateChannelList(channelCategory.getChanneles());

                 //  progressDialog.dismiss();
               }
               }
        });
     //   progressDialog.dismiss();
        return view;

    }
    /*private  List<ChannelCategory> getChannelCategoris() {
       List<ChannelCategory> list=new ArrayList<>();
       for(String string: M3UListConstants.channelCategories){


           list.add(new ChannelCategory(string));


       }
return list;
    }*/

}
