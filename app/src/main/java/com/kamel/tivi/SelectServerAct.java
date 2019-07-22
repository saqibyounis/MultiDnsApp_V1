package com.kamel.tivi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kamel.tivi.category.CategoryAdapter;
import com.kamel.tivi.category.CategoryModel;
import com.kamel.tivi.network.URLs;

import java.util.ArrayList;
import java.util.List;

public class SelectServerAct extends AppCompatActivity {
  GridView sgrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_select_server);

        final List<CategoryModel> catnames=new ArrayList<>();
        catnames.add(new CategoryModel(R.drawable.newpiserver,"third"));
        catnames.add(new CategoryModel(R.drawable.sway,"seccond"));

        catnames.add(new CategoryModel(R.drawable.horustv,"first"));


        final CategoryAdapter adapter=new CategoryAdapter(this,catnames);

        sgrid=findViewById(R.id.sgrid);
        sgrid.setNumColumns(3);
        sgrid.setSelection(1);
        sgrid.setAdapter(adapter);
        sgrid.setDrawSelectorOnTop(true);
        sgrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);

                switch (position){
                    case 2:
                        URLs.url="http://hourstv.net:7766";
                        URLs.serverName="HoursTv";
                        startActivity(new Intent(SelectServerAct.this,LoginActivity.class));

                        break;
                    case 1:
                        URLs.url="http://swaytv.hostingteam.net:25461";
                        URLs.serverName="SwayTv";
                        startActivity(new Intent(SelectServerAct.this,LoginActivity.class));

                        break;
                    case 0:
                        URLs.url="http://cuthecord.ddns.net:25461";
                        URLs.serverName="PiTv";
                        startActivity(new Intent(SelectServerAct.this,LoginActivity.class));

                        break;

                }

            }
        });


    }
}
