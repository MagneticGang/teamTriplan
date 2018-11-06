package com.hanul.team1.triplan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.hanul.team1.triplan.jiyoon.StartActivity;

public class SplashActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sh_activity_splash);

        SharedPreferences sp = getSharedPreferences("userProfile", Activity.MODE_PRIVATE);
        if(sp != null){
            if(sp.getBoolean("login_pass", false)){
                intent = new Intent(this, StartActivity.class);
            } else {
                intent = new Intent(this, StartActivity.class);
            }
        } else {
            intent = new Intent(this, StartActivity.class);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
