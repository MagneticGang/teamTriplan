package com.hanul.team1.triplan.ysh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ysh.dtos.LatLngSiteVO;
import com.hanul.team1.triplan.ysh.dtos.PlanListDTO;
import com.hanul.team1.triplan.ysh.listview.PlanListRecyclerAdapter;
import com.hanul.team1.triplan.ysh.retrofit.PlanInterface;
import com.hanul.team1.triplan.ysh.retrofit.RetrofitClient;
import com.hanul.team1.triplan.ysh.util.Distance;
import com.hanul.team1.triplan.ysh.util.PlanListAsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanListFragment extends Fragment {

    RecyclerView RV;
    TextView planTvNull;
    ArrayList<PlanListDTO> dtos;
    PlanListRecyclerAdapter planListRecyclerAdapter;
    Context context;
    ProgressDialog dialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sh_fragment_planlist, container, false);

        dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("데이터 로딩 중입니다.");
        dialog.show();

        context = getContext();

        planTvNull = rootView.findViewById(R.id.planTvNull);
        RV = rootView.findViewById(R.id.planRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        RV.setLayoutManager(layoutManager);

        SharedPreferences sp = getActivity().getSharedPreferences("userProfile", Activity.MODE_PRIVATE);
        String userid = sp.getString("userid","");


        PlanListAsyncTask planListAsyncTask = new PlanListAsyncTask(dtos,userid,planListRecyclerAdapter,RV,getContext(),dialog, planTvNull);
        planListAsyncTask.execute();

        return rootView;
    }
}
