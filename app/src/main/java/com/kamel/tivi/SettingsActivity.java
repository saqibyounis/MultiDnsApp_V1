package com.kamel.tivi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kamel.tivi.userpref.UserSharedPref;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void logout(View view){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
     public void remove(View view){
         SharedPreferences.Editor editor=UserSharedPref.getSharedPrefEditor(this);
        editor.remove("username");
        editor.remove("password");
        editor.commit();

        startActivity(new Intent(this,SplashActvity.class));
        finish();


     }
     public void parentalContorle(View view){

        startActivity(new Intent(this,ParentalControleActivity.class));

     }
}
