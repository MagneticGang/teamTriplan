package com.hanul.team1.triplan.ysh.util;

import android.app.Activity;
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
import com.hanul.team1.triplan.ysh.objectbox.App;
import com.hanul.team1.triplan.ysh.objectbox.PlanListEntity;
import com.hanul.team1.triplan.ysh.objectbox.PlanListEntity_;
import com.hanul.team1.triplan.ysh.objectbox.UserEntity;
import com.hanul.team1.triplan.ysh.retrofit.PlanInterface;
import com.hanul.team1.triplan.ysh.retrofit.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import retrofit2.Call;

public class PlanListAsyncTask extends AsyncTask<Void, Void, ArrayList<PlanListDTO>> {
    ArrayList<PlanListDTO> dtos;
    String userid;
    PlanListRecyclerAdapter planListRecyclerAdapter;
    RecyclerView RV;
    Context context;
    ProgressDialog dialog;
    TextView planTvNull;
    Activity activity;

    private Box<PlanListEntity> plansBox;
    private Box<UserEntity> userBox;
    private List<PlanListEntity> box_planList;
    private List<UserEntity> box_user;
    private int maxSeq;

    public PlanListAsyncTask(ArrayList<PlanListDTO> dtos, String userid, PlanListRecyclerAdapter planListRecyclerAdapter, RecyclerView RV, Context context, ProgressDialog dialog, TextView planTvNull, Activity activity) {
        this.dtos = dtos;
        this.userid = userid;
        this.planListRecyclerAdapter = planListRecyclerAdapter;
        this.RV = RV;
        this.context = context;
        this.dialog = dialog;
        this.planTvNull = planTvNull;
        this.activity = activity;
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

        BoxStore boxStore = ((App)activity.getApplication()).getBoxStore();

        plansBox = boxStore.boxFor(PlanListEntity.class);
        userBox = boxStore.boxFor(UserEntity.class);
        //plansBox.removeAll();
        box_planList = plansBox.query().order(PlanListEntity_.seq).build().find();
        box_user = userBox.query().build().find();
        if(box_user.size()==0){
            UserEntity user_vo = new UserEntity(0, userid);
            userBox.put(user_vo);
        } else {
            UserEntity user_vo = box_user.get(0);
            if(!user_vo.getUserid().equals(userid)){
                plansBox.removeAll();
                userBox.removeAll();
                UserEntity user_vo2 = new UserEntity(0, userid);
                userBox.put(user_vo2);
            }
        }
        syncToDB(dtos);

        for(PlanListEntity e :box_planList){
            Log.d("yangbob","id="+e.id+" // planid="+e.getPlanid() + " // seq="+e.getSeq() );
        }

        if(dtos.size()>0){
            ArrayList<PlanListDTO> dtos2 = new ArrayList<>();
            dtos2.addAll(dtos);
            for(int i=0; i<dtos.size(); i++){
                int seq = plansBox.query().equal(PlanListEntity_.planid,dtos.get(i).getPlanid()).build().find().get(0).getSeq();
                dtos2.set(dtos.size()-seq, dtos.get(i));
            }
            dtos=dtos2;
        }

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

    private void syncToDB(ArrayList<PlanListDTO> dtos){
        if(dtos.size()==0 && box_planList.size()>0){
            plansBox.removeAll();
        }
        if(dtos.size()>0&&box_planList.size()>0){
            removeAfterChk(dtos);
            addAfterChk(dtos);
        }
        if(dtos.size()>0 && box_planList.size()==0){
            addAll(dtos);
        }
    }

    private void addAll(ArrayList<PlanListDTO> dtos){
        for(int i=0; i<dtos.size(); i++){
            PlanListEntity box_vo = new PlanListEntity(i,dtos.get(i).getPlanid(),i+1);
            plansBox.put(box_vo);
        }
        box_planList = plansBox.query().order(PlanListEntity_.seq).build().find();
    }
    private void addAfterChk(ArrayList<PlanListDTO> dtos){
        if(box_planList.size()>0){
            ArrayList<Integer> planids = new ArrayList<>();
            for(PlanListEntity e : box_planList){
                planids.add(e.getPlanid());
            }

            for(int i=0; i<dtos.size(); i++){
                if(!planids.contains(dtos.get(i).getPlanid())){
                    maxSeq = plansBox.query().orderDesc(PlanListEntity_.seq).build().find().get(0).getSeq();
                    PlanListEntity box_vo = new PlanListEntity(maxSeq+1, dtos.get(i).getPlanid(),maxSeq+1);
                    plansBox.put(box_vo);
                }
            }
        } else {
            addAll(dtos);
        }

        box_planList = plansBox.query().order(PlanListEntity_.seq).build().find();
    }
    private void removeAfterChk(ArrayList<PlanListDTO> dtos){
        ArrayList<Integer> planids = new ArrayList<>();
        for(PlanListDTO e : dtos){
            planids.add(e.getPlanid());
        }

        for(int i =0; i<box_planList.size(); i++){
            if(!planids.contains(box_planList.get(i).getPlanid())){
                plansBox.remove(box_planList.get(i).id);
                for(int j=i+1; j<box_planList.size(); j++){
                    PlanListEntity box_vo = box_planList.get(j);
                    box_vo.setSeq(box_vo.getSeq()-1);
                    plansBox.put(box_vo);
                }
            }
        }

        box_planList = plansBox.query().order(PlanListEntity_.seq).build().find();
    }
}
