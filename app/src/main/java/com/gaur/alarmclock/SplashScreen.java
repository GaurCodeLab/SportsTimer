package com.gaur.alarmclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {


    Animation frombottom, fromtop;
    ImageView logo;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        title = findViewById(R.id.logoText);
        logo = findViewById(R.id.logoImage);
        fromtop = AnimationUtils.loadAnimation(this,R.anim.alpha);
        frombottom = AnimationUtils.loadAnimation(this,R.anim.translate);
        title.setAnimation(frombottom);
        logo.setAnimation(fromtop);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent  = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, 2500);
    }



}
