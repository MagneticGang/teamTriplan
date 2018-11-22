package com.hanul.team1.triplan.ysh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ysh.dtos.PlanListDTO;
import com.hanul.team1.triplan.ysh.listview.PlanListRecyclerAdapter;
import com.hanul.team1.triplan.ysh.util.PlanListAsyncTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class PlanListFragment extends Fragment implements View.OnClickListener {

    RecyclerView RV;
    TextView planTvNull;
    ArrayList<PlanListDTO> dtos;
    PlanListRecyclerAdapter planListRecyclerAdapter;
    Context context;
    ProgressDialog dialog;

    FloatingActionButton fab, fab1, fab2, fab3;
    Animation fab_open, fab_close;
    Boolean isFabOpen = false;
    FrameLayout fl1, fl2, fl3;
    ArrayList<PlanListDTO> dtos_remain = new ArrayList<>();

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

        planListRecyclerAdapter = new PlanListRecyclerAdapter(dtos,getContext());

        PlanListAsyncTask planListAsyncTask = new PlanListAsyncTask(dtos,userid,planListRecyclerAdapter,RV,getContext(),dialog, planTvNull);
        try {
            dtos = planListAsyncTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);

        fab=rootView.findViewById(R.id.fab);
        fab1=rootView.findViewById(R.id.fab1);
        fab2=rootView.findViewById(R.id.fab2);
        fab3=rootView.findViewById(R.id.fab3);

        fl1 = rootView.findViewById(R.id.fl1);
        fl2 = rootView.findViewById(R.id.fl2);
        fl3 = rootView.findViewById(R.id.fl3);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);

        return rootView;
    }

    public void anim(){
        if(isFabOpen){
            fl1.startAnimation(fab_close);
            fl2.startAnimation(fab_close);
            fl3.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;
        } else {
            fl1.startAnimation(fab_open);
            fl2.startAnimation(fab_open);
            fl3.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isFabOpen=true;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        java.sql.Date date = new java.sql.Date(new Date().getTime());
        switch (id){
            case R.id.fab:
                anim();
                break;
            case R.id.fab1:
                dialog.show();
                anim();
                planListRecyclerAdapter.setDtos(dtos);
                planListRecyclerAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                },2000);
                break;
            case R.id.fab2:
                dialog.show();
                anim();
                dtos_remain.clear();
                for(PlanListDTO vo : dtos){
                    if(date.compareTo(new Date(vo.getStartdate().getTime()+(1000*60*60*24*(vo.getDays()-1))))<=0){
                        dtos_remain.add(vo);
                    }
                }
                planListRecyclerAdapter.setDtos(dtos_remain);
                planListRecyclerAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                },2000);
                break;
            case R.id.fab3:
                dialog.show();
                anim();
                dtos_remain.clear();
                for(PlanListDTO vo : dtos){
                    if(date.compareTo(new Date(vo.getStartdate().getTime()+(1000*60*60*24*(vo.getDays()-1))))<=0&&date.compareTo(vo.getStartdate())>=0){
                        dtos_remain.add(vo);
                    }
                }
                planListRecyclerAdapter.setDtos(dtos_remain);
                planListRecyclerAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                },2000);
        }
    }
}
