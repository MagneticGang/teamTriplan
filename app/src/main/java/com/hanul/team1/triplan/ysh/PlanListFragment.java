package com.hanul.team1.triplan.ysh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ysh.dtos.PlanListDTO;
import com.hanul.team1.triplan.ysh.listview.PlanListAdapter;
import com.hanul.team1.triplan.ysh.query.PlanListSelect;

import java.util.ArrayList;

public class PlanListFragment extends Fragment {

    ListView listView;
    TextView planTvNull;

    PlanListAdapter adapter;
    ArrayList<PlanListDTO> dtos;
    PlanListSelect planListSelect;
    ProgressDialog dialog;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sh_fragment_planlist, container, false);

        listView = rootView.findViewById(R.id.planListView);
        planTvNull = rootView.findViewById(R.id.planTvNull);

        dtos = new ArrayList<>();
        adapter = new PlanListAdapter(dtos,getActivity());
        listView.setAdapter(adapter);

        SharedPreferences sp = getActivity().getSharedPreferences("userProfile", Activity.MODE_PRIVATE);
        String userid = sp.getString("userid","");

        dialog = new ProgressDialog(getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("데이터 로딩 중입니다.");
        dialog.show();

        planListSelect = new PlanListSelect(dtos,adapter,dialog,userid,planTvNull);
        planListSelect.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DayListActivity.class);
                PlanListDTO dto = (PlanListDTO) adapter.getItem(position);
                intent.putExtra("PlanListDTO", dto);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
