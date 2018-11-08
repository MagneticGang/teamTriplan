package com.hanul.team1.triplan.ysh.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hanul.team1.triplan.ysh.GoogleMethods;
import com.hanul.team1.triplan.ysh.dtos.SiteListDTO;

import java.util.ArrayList;


public class SiteListAdapter extends BaseAdapter {

    ArrayList<SiteListDTO> dtos;
    Context context;

    public SiteListAdapter(ArrayList<SiteListDTO> dtos, Context context) {
        this.dtos = dtos;
        this.context = context;

    }

    public void addDTO(SiteListDTO dto){
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

        SiteListView siteListView = null;

        if(convertView==null){
            siteListView = new SiteListView(context);
        } else {
            siteListView = (SiteListView) convertView;
        }

        SiteListDTO dto = dtos.get(position);
        siteListView.setSiteImg(dto.getPlaceid(), context);
        siteListView.setTvSeq(dto.getSeq());

        GoogleMethods google = new GoogleMethods(context);
        siteListView.setTvSiteName( dto.getSiteName() );
        siteListView.setTvSiteType( dto.getSiteType() );
        if(position > 0){
            SiteListDTO dto2 = dtos.get(position-1);
            siteListView.setTvDistance( calcDistance(dto.getLng(), dto2.getLng(), dto.getLat(), dto2.getLat()) );
        } else {
            siteListView.setTvDistance(0.0);
        }

        siteListView.setBtnRoadSearchClick(parent.getContext(), dto.getLat(), dto.getLng());
        siteListView.setBtnMemoClick(parent.getContext(), dto.getSiteid(), dto.getSiteName());
        return siteListView;
    }

    private double calcDistance(double x1, double x2, double y1, double y2){
        double theta, dist;
        theta = x1 - x2;
        dist = Math.sin(deg2rad(y1)) * Math.sin(deg2rad(y2)) + Math.cos(deg2rad(y1))
                * Math.cos(deg2rad(y2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;    // 단위 mile 에서 km 변환.

        dist = Math.round(dist*100)/100.0;
        return dist;
    }
    private double deg2rad(double deg){
        return (double)(deg * Math.PI / (double)180d);
    }
    private double rad2deg(double rad){
        return (double)(rad * (double)180d / Math.PI);
    }
}
