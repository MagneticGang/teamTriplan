package com.hanul.team1.triplan.ysh.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hanul.team1.triplan.ysh.dtos.DayListDTO;

import java.util.ArrayList;

public class DayListAdapter extends BaseAdapter {
    ArrayList<DayListDTO> dtos;
    Context context;

    public DayListAdapter(ArrayList<DayListDTO> dtos, Context context) {
        this.dtos = dtos;
        this.context = context;
    }

    public void addDTO(DayListDTO dto){
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
        DayListView dayListView = null;

        if(convertView == null){
            dayListView = new DayListView(context);
        } else {
            dayListView = (DayListView) convertView;
        }

        DayListDTO dto = dtos.get(position);
        dayListView.setTvDay(dto.getDay());
        dayListView.setTvDate1(dto.getDates());
        dayListView.setTvDate2(dto.getDays());
        dayListView.setTvPlaceName2(dto.getDestName());
        dayListView.setTvCntPlace2(dto.getCntSite());

        return dayListView;
    }
}
