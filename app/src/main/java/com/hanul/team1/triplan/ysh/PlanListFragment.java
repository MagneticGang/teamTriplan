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
import com.hanul.team1.triplan.ysh.retrofit.PlanInterface;
import com.hanul.team1.triplan.ysh.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanListFragment extends Fragment {

    ListView listView;
    TextView planTvNull;

    PlanListAdapter adapter;
    ArrayList<PlanListDTO> dtos;
    ProgressDialog dialog;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sh_fragment_planlist, container, false);

        listView = rootView.findViewById(R.id.planListView);
        planTvNull = rootView.findViewById(R.id.planTvNull);

        dialog = new ProgressDialog(getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("데이터 로딩 중입니다.");
        dialog.show();

        SharedPreferences sp = getActivity().getSharedPreferences("userProfile", Activity.MODE_PRIVATE);
        String userid = sp.getString("userid","");

        Call<List<PlanListDTO>> call = RetrofitClient.getRetrofit().create(PlanInterface.class).getPlanList(userid);
        call.enqueue(new Callback<List<PlanListDTO>>() {
            @Override
            public void onResponse(Call<List<PlanListDTO>> call, Response<List<PlanListDTO>> response) {
                dtos = (ArrayList<PlanListDTO>) response.body();
                adapter = new PlanListAdapter(dtos,getActivity());
                listView.setAdapter(adapter);

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<PlanListDTO>> call, Throwable t) {
                call.cancel();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DayListActivity.class);
                PlanListDTO pvo = (PlanListDTO) adapter.getItem(position);
                intent.putExtra("pvo",  pvo);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
