package com.example.patientmonitoringsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimatedImageDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.patientmonitoringsystem.Utils.PreferenceUtils;

public class splashscreen extends AppCompatActivity {

    ImageView splahImg;
    Animation animation ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        splahImg = (ImageView) findViewById(R.id.splashimage);
        animation = AnimationUtils.loadAnimation(this, R.anim.image_slider);
        splahImg.setAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PreferenceUtils.getEmail(getApplicationContext()) != null && PreferenceUtils.getPassword(getApplicationContext()) != null){
                    Intent intent = new Intent(splashscreen.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();
                }
                else {
                    Intent intent = new Intent(splashscreen.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();
                }


            }
        }, 3000);
    }
}