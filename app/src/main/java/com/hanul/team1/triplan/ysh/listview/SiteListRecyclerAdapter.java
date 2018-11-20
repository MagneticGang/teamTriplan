package com.hanul.team1.triplan.ysh.listview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ysh.GoogleMethods;
import com.hanul.team1.triplan.ysh.MemoActivity;
import com.hanul.team1.triplan.ysh.dtos.SiteListDTO;

import java.util.ArrayList;

public class SiteListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static class SiteListHolder extends RecyclerView.ViewHolder{
        ImageView siteImg;
        TextView tvSiteName, tvSiteType, tvSeq, tvDistance;
        Button btnRoadSearch, btnMemo;

        public SiteListHolder(View itemView) {
            super(itemView);
            siteImg = itemView.findViewById(R.id.siteImg);
            tvSiteName = itemView.findViewById(R.id.tvSiteName);
            tvSiteType = itemView.findViewById(R.id.tvSiteType);
            tvSeq = itemView.findViewById(R.id.tvSeq);
            tvDistance =itemView.findViewById(R.id.tvDistance);
            btnRoadSearch =itemView.findViewById(R.id.btnRoadSearch);
            btnMemo =itemView.findViewById(R.id.btnMemo);
        }
    }

    private ArrayList<SiteListDTO> dtos;
    Context context;
    public SiteListRecyclerAdapter(ArrayList<SiteListDTO> dtos, Context context) {
        this.dtos = dtos;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sh_activity_site_list,parent,false);
        return new SiteListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SiteListHolder siteListHolder = (SiteListHolder) holder;
        SiteListDTO dto = dtos.get(position);

        GoogleMethods googleMethods = new GoogleMethods(context);
        googleMethods.getPhotoById(dto.placeid,siteListHolder.siteImg);
        siteListHolder.tvSiteName.setText(dto.siteName);
        siteListHolder.tvSiteType.setText(dto.siteType);
        siteListHolder.tvSeq.setText(dto.seq+"");
        if(position>0){
            SiteListDTO dto2 = dtos.get(position-1);
            siteListHolder.tvDistance.setText(calcDistance(dto.lng, dto2.lng, dto.lat, dto2.lat)+"km");
        } else {
            siteListHolder.tvDistance.setText("0km");
        }
        siteListHolder.btnRoadSearch.setOnClickListener(new customOnClickListener(dto) {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+dto.lat+","+dto.lng);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });
        siteListHolder.btnMemo.setOnClickListener(new customOnClickListener(dto) {
            @Override
            public void onClick(View v) {
                Intent dialogIntent = new Intent(context, MemoActivity.class);
                dialogIntent.putExtra("siteid", dto.siteid);
                dialogIntent.putExtra("siteName", dto.siteName);
                context.startActivity(dialogIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dtos.size();
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

    public abstract class customOnClickListener implements View.OnClickListener {

        protected SiteListDTO dto;
        public customOnClickListener(SiteListDTO dto) {
            this.dto = dto;
        }
    }
}
