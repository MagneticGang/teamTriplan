package com.hanul.team1.triplan.ysh.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hanul.team1.triplan.ysh.dtos.LatLngSiteVO;
import com.hanul.team1.triplan.ysh.dtos.PlanListDTO;
import com.hanul.team1.triplan.ysh.listview.PlanListRecyclerAdapter;
import com.hanul.team1.triplan.ysh.retrofit.PlanInterface;
import com.hanul.team1.triplan.ysh.retrofit.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class PlanListAsyncTask extends AsyncTask<Void, Void, ArrayList<PlanListDTO>> {
    ArrayList<PlanListDTO> dtos;
    String userid;
    PlanListRecyclerAdapter planListRecyclerAdapter;
    RecyclerView RV;
    Context context;
    ProgressDialog dialog;
    TextView planTvNull;

    public PlanListAsyncTask(ArrayList<PlanListDTO> dtos, String userid, PlanListRecyclerAdapter planListRecyclerAdapter, RecyclerView RV, Context context, ProgressDialog dialog, TextView planTvNull) {
        this.dtos = dtos;
        this.userid = userid;
        this.planListRecyclerAdapter = planListRecyclerAdapter;
        this.RV = RV;
        this.context = context;
        this.dialog = dialog;
        this.planTvNull = planTvNull;
    }

    @Override
    protected ArrayList<PlanListDTO> doInBackground(Void... voids) {
        double totDist=0.0;
        Distance dist = new Distance();
        Call<List<PlanListDTO>> call = RetrofitClient.getRetrofit().create(PlanInterface.class).getPlanList(userid);
        try {
            dtos = (ArrayList<PlanListDTO>) call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(dtos.size() >0 ) {
            for (PlanListDTO pvo : dtos) {
                Call<List<LatLngSiteVO>> call2 = RetrofitClient.getRetrofit().create(PlanInterface.class).getLatlngQuery(pvo.getPlanid());
                try {
                    ArrayList<LatLngSiteVO> sarr = (ArrayList<LatLngSiteVO>) call2.execute().body();
                    for (int i = 0; i < sarr.size(); i++) {
                        if (sarr.get(i).seq > 1) {
                            totDist += dist.calcDistance(sarr.get(i).lat, sarr.get(i).lng, sarr.get(i - 1).lat, sarr.get(i - 1).lng);
                        }
                    }
                    pvo.setTotDistance(totDist);
                    totDist = 0.0;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        test();
        return dtos;
    }

    @Override
    protected void onPostExecute(ArrayList<PlanListDTO> dtos) {
        super.onPostExecute(dtos);

        if(dtos.size() >0 ) {
            planTvNull.setVisibility(View.GONE);
        } else {
            planTvNull.setVisibility(View.VISIBLE);
        }

        planListRecyclerAdapter.setDtos(dtos);
        planListRecyclerAdapter.notifyDataSetChanged();
        ItemTouchHelper.Callback callback = new TouchCallback(planListRecyclerAdapter,context);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(RV);
        RV.setAdapter(planListRecyclerAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 2000);
    }


    public void test(){

    }
}
