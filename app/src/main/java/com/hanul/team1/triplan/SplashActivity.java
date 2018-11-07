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

        //만약 전에 로그인한 경우가 있다면 그것을 불러오고 로그인 과정 스킵할 예정.
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

        //1.5초 후에 StartActivity로
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
