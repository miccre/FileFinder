package com.dominikgames.filefinder.filefinder;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by Dominik on 02.06.2017.
 */

public class Welcome extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        /* Zablokovanie pretacania obrazovky */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();

        /* Prepojenie komponentov a spustenie animacie */
        ImageView Imgvw_spsoa= (ImageView)findViewById(R.id.image);
        Animation Anim_spsoalogo = AnimationUtils.loadAnimation(this, R.anim.welcome);
        Imgvw_spsoa.startAnimation(Anim_spsoalogo);

        /* Uspanie vlakna na 4.5 sekundy kvoli animacii */
        Thread Thrd_wlcmscrndelay = new Thread() {
            public void run() {
                try {
                    sleep(4500);
                    Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(myIntent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                }
            }
        };
        Thrd_wlcmscrndelay.start();
    }
}