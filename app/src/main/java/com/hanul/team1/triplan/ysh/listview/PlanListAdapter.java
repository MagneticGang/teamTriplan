package com.hanul.team1.triplan.ysh.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hanul.team1.triplan.ysh.dtos.PlanListDTO;

import java.util.ArrayList;

public class PlanListAdapter extends BaseAdapter {
    ArrayList<PlanListDTO> dtos;
    Context context;
    public PlanListAdapter(ArrayList<PlanListDTO> dtos, Context context) {
        this.dtos = dtos;
        this.context = context;
    }
    public void addDTO(PlanListDTO dto){
        dtos.add(dto);
    }

    @Override
    public int getCount() {
        return dtos.size();
    }

    @Override
    public Object getItem(int position) {
        return dtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlanListView planListView = null;

        if(convertView == null){
            planListView = new PlanListView(context);
        } else {
            planListView = (PlanListView) convertView;
        }

        PlanListDTO dto = dtos.get(position);
        planListView.setTvCntDestination(dto.getCntDay());
        planListView.setImageView(dto.getPlaceid(), context);
        planListView.setTvCtnPlace(dto.getCntSite());
        planListView.setTvPlanPeriod(dto.getDates());
        planListView.setTvTotDistance(dto.getTotDistance());
        planListView.setTvPlanName(dto.getName());

        return planListView;
    }
}
