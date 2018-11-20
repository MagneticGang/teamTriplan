package com.hanul.team1.triplan.ysh.listview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ysh.SiteListActivity;
import com.hanul.team1.triplan.ysh.dtos.DayListDTO;

import java.util.ArrayList;

public class DayListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static class DayListHolder extends RecyclerView.ViewHolder{
        private TextView tvDay, tvDate1, tvDate2, tvPlaceName2, tvCntPlace2;

        public DayListHolder(View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvDate1 = itemView.findViewById(R.id.tvDate1);
            tvDate2 = itemView.findViewById(R.id.tvDate2);
            tvPlaceName2 = itemView.findViewById(R.id.tvPlaceName2);
            tvCntPlace2 = itemView.findViewById(R.id.tvCntPlace2);
        }
    }

    private ArrayList<DayListDTO> dtos;
    Context context;
    public DayListRecyclerAdapter(ArrayList<DayListDTO> dtos, Context context) {
        this.dtos = dtos;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sh_listview_day,parent,false);
        return new DayListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DayListHolder dayListHolder = (DayListHolder) holder;
        DayListDTO dto = dtos.get(position);

        dayListHolder.tvDay.setText("DAY"+dto.getDay());
        dayListHolder.tvDate1.setText(dto.getDates());
        dayListHolder.tvDate2.setText(dto.getDays());
        dayListHolder.tvPlaceName2.setText(dto.getDestName());
        dayListHolder.tvCntPlace2.setText(""+dto.getCntSite());

        holder.itemView.setOnClickListener(new customOnClickListener(dto) {
            @Override
            public void onClick(View v) {
                DayListDTO dvo = dto;
                Intent intent = new Intent(context, SiteListActivity.class);
                intent.putExtra("dayDto", dvo);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dtos.size();
    }

    public abstract class customOnClickListener implements View.OnClickListener {

        protected DayListDTO dto;
        public customOnClickListener(DayListDTO dto) {
            this.dto = dto;
        }
    }
}
