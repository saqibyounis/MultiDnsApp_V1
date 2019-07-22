package com.kamel.tivi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.kamel.tivi.useaccount.UserAccount;

public class AccountActvity extends AppCompatActivity {
        TextView username,exdate,acative,maxaccount,trail,status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_actvity);
        username=findViewById(R.id.username);
        exdate=findViewById(R.id.exdate);
        acative=findViewById(R.id.activeaccount);
        maxaccount=findViewById(R.id.mxaccounte);
        trail=findViewById(R.id.trail);
        status=findViewById(R.id.status);
        username.setText(UserAccount.userAccount.getUserName());
        trail.setText(UserAccount.userAccount.getTrail());
          maxaccount.setText(UserAccount.userAccount.getMaxConnection());
          acative.setText(UserAccount.userAccount.getActiveAccounts());
          exdate.setText(UserAccount.userAccount.getExpirationDate());
            status.setText(UserAccount.userAccount.getStatus());

    }
}
