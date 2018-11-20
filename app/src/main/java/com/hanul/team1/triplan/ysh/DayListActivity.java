package com.hanul.team1.triplan.ysh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ysh.dtos.DayListDTO;
import com.hanul.team1.triplan.ysh.dtos.PlanListDTO;
import com.hanul.team1.triplan.ysh.listview.DayListAdapter;
import com.hanul.team1.triplan.ysh.retrofit.PlanInterface;
import com.hanul.team1.triplan.ysh.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DayListActivity extends AppCompatActivity {

    ImageView imageView2, btnDayBack;
    TextView tvPlanName2, tvPlanPeriod2, tvUserNickname;
    ListView dayListView;

    DayListAdapter adapter;
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

        //받고
        Intent intent = getIntent();
        PlanListDTO pvo = intent.getParcelableExtra("pvo");
        tvPlanName2.setText(pvo.getName());
        tvPlanPeriod2.setText(pvo.getDates());
        GoogleMethods googleMethods = new GoogleMethods(this);
        googleMethods.getPhotoById(pvo.getPlaceid(),imageView2);

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("데이터 로딩 중입니다.");
        dialog.show();

        dayListView = findViewById(R.id.dayListView);
        RetrofitClient.getRetrofit().create(PlanInterface.class).getDayList(pvo.getPlanid()).enqueue(new Callback<List<DayListDTO>>() {
            @Override
            public void onResponse(Call<List<DayListDTO>> call, Response<List<DayListDTO>> response) {
                dtos = (ArrayList<DayListDTO>) response.body();
                adapter = new DayListAdapter(dtos,getApplicationContext());
                dayListView.setAdapter(adapter);

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<DayListDTO>> call, Throwable t) {
                call.cancel();
            }
        });

        dayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DayListDTO dto = (DayListDTO) adapter.getItem(position);
                Intent intent2 = new Intent(getApplicationContext(), SiteListActivity.class);
                intent2.putExtra("dayDto", dto);
                startActivity(intent2);
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
