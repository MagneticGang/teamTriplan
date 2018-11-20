package com.hanul.team1.triplan.ysh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ysh.dtos.DayListDTO;
import com.hanul.team1.triplan.ysh.dtos.PlanListDTO;
import com.hanul.team1.triplan.ysh.listview.DayListRecyclerAdapter;
import com.hanul.team1.triplan.ysh.retrofit.PlanInterface;
import com.hanul.team1.triplan.ysh.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DayListActivity extends AppCompatActivity {

    ImageView imageView2, btnDayBack;
    TextView tvPlanName2, tvPlanPeriod2, tvUserNickname,dayTvNull;
    RecyclerView dayRV;

    DayListRecyclerAdapter mAdapter;
    ArrayList<DayListDTO> dtos;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sh_activity_day_list);

        //찾고
        tvPlanName2 = findViewById(R.id.tvPlanName2);
        tvPlanPeriod2 = findViewById(R.id.tvPlanPeriod2);
        imageView2 = findViewById(R.id.imageView2);
        dayTvNull = findViewById(R.id.dayTvNull);

        //받고
        Intent intent = getIntent();
        PlanListDTO pvo = (PlanListDTO) intent.getSerializableExtra("pvo");
        tvPlanName2.setText(pvo.getName());
        tvPlanPeriod2.setText(pvo.getDates());
        GoogleMethods googleMethods = new GoogleMethods(this);
        googleMethods.getPhotoById(pvo.getPlaceid(),imageView2);

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("데이터 로딩 중입니다.");
        dialog.show();

        dayRV = findViewById(R.id.dayRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        dayRV.setLayoutManager(layoutManager);
        RetrofitClient.getRetrofit().create(PlanInterface.class).getDayList(pvo.getPlanid()).enqueue(new Callback<List<DayListDTO>>() {
            @Override
            public void onResponse(Call<List<DayListDTO>> call, Response<List<DayListDTO>> response) {
                dtos = (ArrayList<DayListDTO>) response.body();
                if(dtos != null){
                    dayTvNull.setVisibility(View.GONE);
                    mAdapter = new DayListRecyclerAdapter(dtos, getApplicationContext());
                    dayRV.setAdapter(mAdapter);
                } else {
                    dayTvNull.setVisibility(View.VISIBLE);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<DayListDTO>> call, Throwable t) {
                call.cancel();
            }
        });

        btnDayBack = findViewById(R.id.btnDayBack);
        btnDayBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvUserNickname = findViewById(R.id.tvUserNickname);
        SharedPreferences sp = getSharedPreferences("userProfile",Activity.MODE_PRIVATE);
        tvUserNickname.setText(sp.getString("nickname","No NickName"));
    }
}
