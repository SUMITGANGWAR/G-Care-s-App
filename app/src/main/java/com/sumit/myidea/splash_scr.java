package com.sumit.myidea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.lang.annotation.Annotation;

public class splash_scr extends AppCompatActivity {
    ImageView mainlogo,sublogo;
    Animation top,bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_scr);
        mainlogo=(ImageView) findViewById(R.id.wateringplants);
        sublogo=(ImageView) findViewById(R.id.gcare);
        top= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.imagelogo);
        bottom=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.textanimation);
        mainlogo.setAnimation((Animation) top);
        sublogo.setAnimation((Animation) bottom);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),login.class));
                finish();
            }
        }, 1200);
    }
}