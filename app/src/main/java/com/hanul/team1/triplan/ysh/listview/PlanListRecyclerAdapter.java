package com.hanul.team1.triplan.ysh.listview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ysh.DayListActivity;
import com.hanul.team1.triplan.ysh.GoogleMethods;
import com.hanul.team1.triplan.ysh.dtos.PlanListDTO;
import com.hanul.team1.triplan.ysh.util.TouchInterface;

import java.util.ArrayList;
import java.util.Collections;

public class PlanListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements TouchInterface {

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(dtos, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(dtos, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);

        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        dtos.remove(position);
        notifyItemRemoved(position);
    }

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
    public PlanListRecyclerAdapter(ArrayList<PlanListDTO> dtos, Context context) {
        this.dtos = dtos;
        this.context=context;
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

}
