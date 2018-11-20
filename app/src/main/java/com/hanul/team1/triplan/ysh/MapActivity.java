package com.hanul.team1.triplan.ysh;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ysh.dtos.SiteListDTO;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ImageView backArrow;
    ArrayList<SiteListDTO> dtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sh_activity_map);

        Intent intent = getIntent();
        dtos = (ArrayList<SiteListDTO>) intent.getSerializableExtra("dtos");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        backArrow = findViewById(R.id.btnMapBack);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int[] icons = new int[]{R.drawable.one, R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.seven,
            R.drawable.eight, R.drawable.nine};

        LatLng center = new LatLng(dtos.get(0).lat, dtos.get(0).lng);
        for(int i=0; i<dtos.size(); i++){
            SiteListDTO dto = dtos.get(i);
            LatLng latLng = new LatLng(dto.lat, dto.lng);
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(dto.siteName)
                    .icon(BitmapDescriptorFactory.fromResource(icons[i]))
            );
            if(i > 0){
                SiteListDTO dto2 = dtos.get(i-1);
                mMap.addPolyline(new PolylineOptions().add(
                        new LatLng(dto2.lat, dto2.lng),
                        new LatLng(dto.lat, dto.lng)
                ).width(2).color(Color.DKGRAY));
            }
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center,12));
    }
   /* @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int[] icons = new int[]{R.drawable.one, R.drawable.two, R.drawable.three,
                R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.seven,
                R.drawable.eight, R.drawable.nine};

        LatLng center = new LatLng(dtos.get(0).getLat(), dtos.get(0).getLng());
        for(int i=0; i<dtos.size(); i++){
            SiteListDTO dto = dtos.get(i);
            LatLng latLng = new LatLng(dto.getLat(), dto.getLng());
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(dto.getSiteName())
                    .icon(BitmapDescriptorFactory.fromResource(icons[i]))
            );
            if(i > 0){
                SiteListDTO dto2 = dtos.get(i-1);
                mMap.addPolyline(new PolylineOptions().add(
                        new LatLng(dto2.getLat(), dto2.getLng()),
                        new LatLng(dto.getLat(), dto.getLng())
                ).width(2).color(Color.DKGRAY));
            }
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center,12));
    }*/
}
