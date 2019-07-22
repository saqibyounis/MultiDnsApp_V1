package com.kamel.tivi;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kamel.tivi.userpref.UserSharedPref;

public class ParentalControleActivity extends AppCompatActivity {
        EditText pw1,pw2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parental_controle);
         pw1=findViewById(R.id.pw1);
         pw2=findViewById(R.id.pw2);

           pw1.requestFocus();

    }


    public void save(View view){
        SharedPreferences pref= UserSharedPref.getSharedPref(this);
        String currentPassword=pref.getString("parentalpassword",null);


        if(pw1.getText().toString().equalsIgnoreCase(currentPassword)){

            SharedPreferences.Editor editor=UserSharedPref.getSharedPrefEditor(this);
            editor.putString("parentalpassword",pw2.getText().toString());
            editor.commit();
            Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT).show();

            onBackPressed();

        }else {
            Toast.makeText(this, "Current password is worng!", Toast.LENGTH_SHORT).show();

        }

    }
}
