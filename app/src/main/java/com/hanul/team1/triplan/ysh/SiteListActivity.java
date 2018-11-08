package com.hanul.team1.triplan.ysh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ysh.dtos.DayListDTO;
import com.hanul.team1.triplan.ysh.dtos.SiteListDTO;
import com.hanul.team1.triplan.ysh.listview.SiteListAdapter;
import com.hanul.team1.triplan.ysh.query.SiteListSelect;

import java.util.ArrayList;

public class SiteListActivity extends AppCompatActivity {
    TextView siteHeader;
    ImageView btnSiteBack, btnMap;
    ListView siteListView;

    SiteListAdapter adapter;
    ArrayList<SiteListDTO> dtos;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sh_activity_site_list);

        Intent intent = getIntent();
        DayListDTO dayDto = (DayListDTO) intent.getSerializableExtra("dayDto");

        siteHeader = findViewById(R.id.siteHeader);
        siteHeader.setText("DAY "+dayDto.getDay());

        siteListView = findViewById(R.id.siteListView);
        dtos= new ArrayList<>();
        adapter=new SiteListAdapter(dtos, this);
        siteListView.setAdapter(adapter);

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("데이터 로딩 중입니다.");
        dialog.show();

        SiteListSelect siteListSelect = new SiteListSelect(dtos,adapter,dayDto, dialog);
        siteListSelect.execute();

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
