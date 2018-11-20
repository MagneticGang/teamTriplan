package com.hanul.team1.triplan.ysh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ysh.dtos.DayListDTO;
import com.hanul.team1.triplan.ysh.dtos.SiteListDTO;
import com.hanul.team1.triplan.ysh.listview.SiteListRecyclerAdapter;
import com.hanul.team1.triplan.ysh.retrofit.PlanInterface;
import com.hanul.team1.triplan.ysh.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteListActivity extends AppCompatActivity {
    TextView siteHeader, siteTvNull;
    ImageView btnSiteBack, btnMap;
    RecyclerView siteRV;

    SiteListRecyclerAdapter mAdapter;
    ArrayList<SiteListDTO> dtos;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sh_activity_site_list);

        Intent intent = getIntent();
        DayListDTO dayDto = (DayListDTO) intent.getSerializableExtra("dayDto");

        siteTvNull = findViewById(R.id.siteTvNull);
        siteHeader = findViewById(R.id.siteHeader);
        siteHeader.setText("DAY "+dayDto.getDay());

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("데이터 로딩 중입니다.");
        dialog.show();

        siteRV = findViewById(R.id.siteRV);
        siteRV.setLayoutManager(new LinearLayoutManager(this));
        HashMap<String,Integer> map = new HashMap<>();
        map.put("planid", dayDto.getPlanid());
        map.put("dayid", dayDto.getDayid());
        RetrofitClient.getRetrofit().create(PlanInterface.class).getSiteList(map).enqueue(new Callback<List<SiteListDTO>>() {
            @Override
            public void onResponse(Call<List<SiteListDTO>> call, Response<List<SiteListDTO>> response) {
                dtos= (ArrayList<SiteListDTO>) response.body();
                if(dtos.size() == 0){
                    siteTvNull.setVisibility(View.VISIBLE);
                } else {
                    siteTvNull.setVisibility(View.GONE);
                    mAdapter = new SiteListRecyclerAdapter(dtos,getApplicationContext());
                    siteRV.setAdapter(mAdapter);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<SiteListDTO>> call, Throwable t) {
                call.cancel();
            }
        });

        btnMap = findViewById(R.id.btnMap);
        btnSiteBack = findViewById(R.id.btnSiteBack);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), MapActivity.class);
                intent2.putExtra("dtos", dtos);
                startActivity(intent2);
            }
        });

        btnSiteBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
