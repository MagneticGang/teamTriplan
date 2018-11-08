package com.hanul.team1.triplan.ysh.listview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanul.team1.triplan.ysh.GoogleMethods;
import com.hanul.team1.triplan.R;

public class PlanListView extends LinearLayout {
    TextView tvPlanName, tvPlanPeriod, tvCntDestination, tvCntPlace, tvTotDistance;
    ImageView imageView;

    public PlanListView(Context context) {
        super(context);
        init(context);
    }

    public PlanListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sh_listview_plan, this, true);

        tvCntDestination = findViewById(R.id.tvCntDestination);
        tvCntPlace = findViewById(R.id.tvCntPlace);
        tvPlanName = findViewById(R.id.tvPlanName);
        tvPlanPeriod = findViewById(R.id.tvPlanPeriod);
        tvTotDistance = findViewById(R.id.tvTotDistance);
        imageView = findViewById(R.id.imageView);
    }

    public void setTvPlanName(String planName) {
        tvPlanName.setText(planName);
    }

    public void setTvPlanPeriod(String planPeriod) {
        tvPlanPeriod.setText(planPeriod);
    }

    public void setTvCntDestination(int cntDestination) {
        tvCntDestination.setText(cntDestination+"개 여행지");
    }

    public void setTvCtnPlace(int cntPlace) {
        tvCntPlace.setText(cntPlace+"개 장소");
    }

    public void setTvTotDistance(double totDistance) {
        tvTotDistance.setText(totDistance+"km");
    }

    public void setImageView(String placeId, Context context) {
        GoogleMethods googleMethods = new GoogleMethods(context);
        googleMethods.getPhotoById(placeId, imageView);
    }
}
