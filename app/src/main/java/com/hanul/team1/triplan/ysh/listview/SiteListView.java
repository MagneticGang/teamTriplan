package com.hanul.team1.triplan.ysh.listview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanul.team1.triplan.ysh.GoogleMethods;
import com.hanul.team1.triplan.ysh.MemoActivity;
import com.hanul.team1.triplan.R;

public class SiteListView extends LinearLayout {

    ImageView siteImg;
    TextView tvSiteName, tvSiteType, tvSeq, tvDistance;
    Button btnRoadSearch, btnMemo;

    public SiteListView(Context context) {
        super(context);
        init(context);
    }

    public SiteListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sh_listview_site, this, true);

        siteImg = findViewById(R.id.siteImg);
        tvSiteName = findViewById(R.id.tvSiteName);
        tvSiteType = findViewById(R.id.tvSiteType);
        tvSeq = findViewById(R.id.tvSeq);
        tvDistance = findViewById(R.id.tvDistance);
        btnRoadSearch = findViewById(R.id.btnRoadSearch);
        btnMemo = findViewById(R.id.btnMemo);
    }

    public void setSiteImg(String placeId, Context context) {
        GoogleMethods googleMethods = new GoogleMethods(context);
        googleMethods.getPhotoById(placeId,siteImg);
    }

    public void setTvSiteName(String siteName) {
        tvSiteName.setText(siteName);
    }

    public void setTvSiteType(String siteType) {
        tvSiteType.setText(siteType);
    }

    public void setTvSeq(int seq) {
        tvSeq.setText(""+seq);
    }

    public void setTvDistance(double distance) {
        tvDistance.setText(distance+"km");
    }

    public void setBtnRoadSearchClick(final Context context, final double lat, final double lng){
        btnRoadSearch.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+","+lng);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });
    }

    public void setBtnMemoClick(final Context context, final int siteid, final String siteName){
        btnMemo.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent dialogIntent = new Intent(context, MemoActivity.class);
                dialogIntent.putExtra("siteid", siteid);
                dialogIntent.putExtra("siteName", siteName);
                context.startActivity(dialogIntent);
            }
        });
    }


}
