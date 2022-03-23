package com.ismt.ismtproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    ImageView imageView2;
    TextView splashHeader,slogan;
    private Animation topAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

//        Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

//        hooks
        imageView2 = findViewById(R.id.imageView2);
        splashHeader = findViewById(R.id.splashHeader);
        slogan = findViewById(R.id.slogan);

        imageView2.setAnimation(topAnim);
        splashHeader.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

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