package com.hanul.team1.triplan.jiyoon;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ysh.MainPageAdapter;

public class SuccessActivity extends AppCompatActivity {

    TabLayout tabs;
    ViewPager mainVP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sh_activity_main);

        SharedPreferences sp = getSharedPreferences("userProfile", Activity.MODE_PRIVATE);

        mainVP = findViewById(R.id.mainVP);
        mainVP.setAdapter(new MainPageAdapter(getSupportFragmentManager()));
        mainVP.setCurrentItem(0);

        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(mainVP);

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainVP.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {            }
        });

    }
}
