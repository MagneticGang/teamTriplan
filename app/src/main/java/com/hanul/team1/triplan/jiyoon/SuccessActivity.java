package com.hanul.team1.triplan.jiyoon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hanul.team1.triplan.R;

public class SuccessActivity extends AppCompatActivity {

    Button getLoggedUserBtn, logoutBtn;
    TextView infoTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jy_activity_success);

        getLoggedUserBtn = findViewById(R.id.getLoggedUserBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        infoTV = findViewById(R.id.infoTV);

        //버튼 누르면 현재 유저 정보를 출력
        getLoggedUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            SharedPreferences sp = getSharedPreferences("userProfile", MODE_PRIVATE);
            infoTV.setText("아이디: " + sp.getString("userid", "없음") + ", "
                    +"닉네임: " + sp.getString("nickname", "없음") );
            }
        });

        //버튼 누르면 현재 유저 정보를 SP로부터 삭제 후 StartActivity로.
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("userProfile", MODE_PRIVATE);
                SharedPreferences.Editor e = sp.edit();
                e.clear();
                e.commit();
                Intent toStart = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(toStart);
            }
        });


    }
}
