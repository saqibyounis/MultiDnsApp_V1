package com.kamel.tivi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashActvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View easySplashScreenView = new EasySplashScreen(SplashActvity.this)

                .withTargetActivity(LoginActivity.class)
                .withSplashTimeOut(2000)
                .withBackgroundResource(android.R.color.black)
                .withHeaderText("TiVi")
                .withFooterText("Copyright 2019")

                .withLogo(R.drawable.newlogofull_300x300)
                .create();

        setContentView(easySplashScreenView);
    }
}
