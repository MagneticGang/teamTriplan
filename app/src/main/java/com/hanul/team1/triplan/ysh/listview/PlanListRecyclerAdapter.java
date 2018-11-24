package com.hanul.team1.triplan.ysh.listview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ysh.DayListActivity;
import com.hanul.team1.triplan.ysh.GoogleMethods;
import com.hanul.team1.triplan.ysh.dtos.PlanListDTO;
import com.hanul.team1.triplan.ysh.objectbox.App;
import com.hanul.team1.triplan.ysh.objectbox.PlanListEntity;
import com.hanul.team1.triplan.ysh.objectbox.PlanListEntity_;
import com.hanul.team1.triplan.ysh.retrofit.PlanInterface;
import com.hanul.team1.triplan.ysh.retrofit.RetrofitClient;
import com.hanul.team1.triplan.ysh.util.TouchInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements TouchInterface {

    public static class PlanListHolder extends RecyclerView.ViewHolder{
        private TextView tvPlanName, tvPlanPeriod, tvCntDestination, tvCntPlace, tvTotDistance;
        private ImageView imageView;

        public PlanListHolder(View itemView) {
            super(itemView);
            tvCntDestination = itemView.findViewById(R.id.tvCntDestination);
            tvCntPlace = itemView.findViewById(R.id.tvCntPlace);
            tvPlanName = itemView.findViewById(R.id.tvPlanName);
            tvPlanPeriod = itemView.findViewById(R.id.tvPlanPeriod);
            tvTotDistance = itemView.findViewById(R.id.tvTotDistance);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    private ArrayList<PlanListDTO> dtos;
    Context context;
    Activity activity;

    BoxStore boxStore;
    Box<PlanListEntity> plansBox;
    List<PlanListEntity> box_planList;

    public PlanListRecyclerAdapter(ArrayList<PlanListDTO> dtos, Context context, Activity activity) {
        this.dtos = dtos;
        this.context=context;
        this.activity = activity;
    }

    public void setDtos(ArrayList<PlanListDTO> dtos){
        this.dtos = dtos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sh_listview_plan,parent,false);
        return new PlanListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PlanListHolder planListHolder = (PlanListHolder) holder;
        PlanListDTO dto = dtos.get(position);

        GoogleMethods googleMethods = new GoogleMethods(context);
        googleMethods.getPhotoById(dto.getPlaceid(), planListHolder.imageView);
        planListHolder.tvPlanName.setText(dto.getName());
        planListHolder.tvPlanPeriod.setText(dto.getDates());
        planListHolder.tvCntDestination.setText(dto.getCntDay()+"개 여행지");
        planListHolder.tvCntPlace.setText(dto.getCntSite()+"개 장소");
        planListHolder.tvTotDistance.setText(dto.getTotDistance()+"km");

        holder.itemView.setOnClickListener(new customOnClickListener(dto) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DayListActivity.class);
                PlanListDTO pvo = dto;
                intent.putExtra("pvo",  pvo);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dtos.size();
    }

    public abstract class customOnClickListener implements View.OnClickListener {

        protected PlanListDTO dto;
        public customOnClickListener(PlanListDTO dto) {
            this.dto = dto;
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        boxStore = ((App)activity.getApplication()).getBoxStore();
        plansBox = boxStore.boxFor(PlanListEntity.class);
        box_planList  = plansBox.query().orderDesc(PlanListEntity_.seq).build().find();

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(dtos, i, i + 1);
                PlanListEntity box_vo = box_planList.get(i);
                PlanListEntity box_vo2 = box_planList.get(i+1);
                box_vo.setSeq(box_vo.getSeq()-1);
                box_vo2.setSeq(box_vo2.getSeq()+1);
                plansBox.put(box_vo);
                plansBox.put(box_vo2);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(dtos, i, i - 1);
                PlanListEntity box_vo = box_planList.get(i);
                PlanListEntity box_vo2 = box_planList.get(i-1);
                box_vo.setSeq(box_vo.getSeq()+1);
                box_vo2.setSeq(box_vo2.getSeq()-1);
                plansBox.put(box_vo);
                plansBox.put(box_vo2);
            }
        }
        notifyItemMoved(fromPosition, toPosition);

        return true;
    }


    @Override
    public void onItemDismiss(int position) {
        RetrofitClient.getRetrofit().create(PlanInterface.class).deletePlan(dtos.get(position).getPlanid()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                boolean result = false;
                try {
                    result = response.body().string().equals("true");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(result){
                    Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
            }
        });
        dtos.remove(position);
        notifyItemRemoved(position);
    }

}
