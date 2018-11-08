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
import com.hanul.team1.triplan.ysh.query.DayListSelect;

import java.util.ArrayList;

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
        PlanListDTO planListDTO = (PlanListDTO) intent.getSerializableExtra("PlanListDTO");
        //넣고
        tvPlanName2.setText(planListDTO.getName());
        tvPlanPeriod2.setText(planListDTO.getDates());
        GoogleMethods googleMethods = new GoogleMethods(this);
        googleMethods.getPhotoById(planListDTO.getPlaceid(),imageView2);

        dayListView = findViewById(R.id.dayListView);
        dtos = new ArrayList<>();
        adapter = new DayListAdapter(dtos,this);
        dayListView.setAdapter(adapter);

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("데이터 로딩 중입니다.");
        dialog.show();

        DayListSelect dayListSelect = new DayListSelect(dtos, adapter, planListDTO.getPlanid(), dialog);
        dayListSelect.execute();


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
