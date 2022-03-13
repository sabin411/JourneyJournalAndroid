package com.ismt.ismtproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences = getSharedPreferences("journal_prefs", Context.MODE_PRIVATE);
        boolean check = sharedPreferences.getBoolean("user_login", false);

        new Handler().postDelayed(() -> {
//            check whether user is loggedin or not
            if(!check){
                startActivity(new Intent(this, SignupActivity.class));
            }
            else{
//            intent to login screen
                startActivity(new Intent(this, DashboardJourneyJournal.class));
            }
            finish();
        }, 3000);


    }
}