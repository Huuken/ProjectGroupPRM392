package com.example.freshfoldlaundrycare.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.freshfoldlaundrycare.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //IF LOGGED IN CHECK SHOULD BE DONE HERE

                Intent login = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(login);
                finish();

            }
        },3000);

    }
}