package com.hanul.team1.triplan.ysh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ysh.dtos.PlanListDTO;
import com.hanul.team1.triplan.ysh.listview.PlanListRecyclerAdapter;
import com.hanul.team1.triplan.ysh.util.PlanListAsyncTask;

import java.util.ArrayList;

public class PlanListFragment extends Fragment {

    RecyclerView RV;
    TextView planTvNull;
    ArrayList<PlanListDTO> dtos;
    PlanListRecyclerAdapter planListRecyclerAdapter;
    Context context;
    ProgressDialog dialog;

    FloatingActionButton fab, fab1, fab2;
    Animation fab_open, fab_close;
    Boolean isFabOpen = false;

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



        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);

        fab=rootView.findViewById(R.id.fab);
        fab1=rootView.findViewById(R.id.fab1);
        fab2=rootView.findViewById(R.id.fab2);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
                Toast.makeText(context, "Floating", Toast.LENGTH_SHORT).show();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
                Toast.makeText(context, "fab1", Toast.LENGTH_SHORT).show();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
                Toast.makeText(context, "fab2", Toast.LENGTH_SHORT).show();
            }
        });




        return rootView;
    }

    public void anim(){
        if(isFabOpen){
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen=true;
        }
    }


}
