package com.hanul.team1.triplan.ysh;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    ImageView backArrow;
    ArrayList<SiteListDTO> dtos;
    Double lat, lng;
    ArrayList<String> mLikelyPlaceNames;
    ArrayList<String> mLikelyAddress;
    ArrayList<String> mLikelyAttributions;
    ArrayList<LatLng> mLikelyLatLngs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sh_activity_map);

        Intent intent = getIntent();
        dtos = (ArrayList<SiteListDTO>) intent.getSerializableExtra("dtos");
        lat = intent.getDoubleExtra("lat",dtos.get(0).lng);
        lng = intent.getDoubleExtra("lng", dtos.get(0).lat);

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    Toast.makeText(this, permissions[i] + "시스템 관리자에게 문의 : 실제 권한 요청하고 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText( this, "현재 위치 기반으로 제공되는 서비스를 이용하실 수 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
            mMap.setMyLocationEnabled(true);
        }
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        int[] icons = new int[]{R.drawable.one, R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.seven,
            R.drawable.eight, R.drawable.nine};

        LatLng center = new LatLng(lat, lng);
        for(int i=0; i<dtos.size(); i++){
            SiteListDTO dto = dtos.get(i);
            LatLng latLng = new LatLng(dto.lat, dto.lng);
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(dto.siteName)
                    .snippet(dto.siteType)
                    .icon(BitmapDescriptorFactory.fromResource(icons[i])
                    )
            );
            if(i > 0){
                SiteListDTO dto2 = dtos.get(i-1);
                mMap.addPolyline(new PolylineOptions().add(
                        new LatLng(dto2.lat, dto2.lng),
                        new LatLng(dto.lat, dto.lng)
                ).width(2).color(Color.RED));
            }
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center,12));
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infowindow = getLayoutInflater().inflate(R.layout.sh_custom_info_contents,null);
                TextView title = infowindow.findViewById(R.id.title);
                title.setText(marker.getTitle());
                TextView snippet = infowindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infowindow;
            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        List<Integer> filters = new ArrayList<>();
        filters.add(Place.TYPE_FOOD);

        GoogleMethods gm = new GoogleMethods(this);
        Task<PlaceLikelihoodBufferResponse> placeResult = gm.mPlaceDetectionClient.getCurrentPlace(null);
        placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                    mLikelyPlaceNames = new ArrayList<>();
                    mLikelyAddress = new ArrayList<>();
                    mLikelyAttributions = new ArrayList<>();
                    mLikelyLatLngs = new ArrayList<>();

                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        if(placeLikelihood.getPlace().getPlaceTypes().contains(Place.TYPE_FOOD)){
                            mLikelyPlaceNames.add((String) placeLikelihood.getPlace().getName());
                            mLikelyAddress.add(placeLikelihood.getPlace().getAddress()+"\n"+placeLikelihood.getPlace().getPhoneNumber());
                            mLikelyAttributions.add((String) placeLikelihood.getPlace().getAttributions());
                            mLikelyLatLngs.add(placeLikelihood.getPlace().getLatLng());
                        }
                    }
                    likelyPlaces.release();
                    openPlacesDialog();
                } else {
                    Toast.makeText(MapActivity.this, "주변 검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openPlacesDialog(){
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LatLng markerLatlng = mLikelyLatLngs.get(which);
                String markerSnippet = mLikelyAddress.get(which);
                if(mLikelyAttributions.get(which) != null){
                    markerSnippet = markerSnippet + "\n" + mLikelyAttributions.get(which) ;
                }

                Marker marker = mMap.addMarker(new MarkerOptions()
                    .title(mLikelyPlaceNames.get(which))
                    .position(markerLatlng)
                    .snippet(markerSnippet));
                marker.setTag(1);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatlng, 14));
            }
        };

        View dialogTitle = getLayoutInflater().inflate(R.layout.sh_custom_alert_title, null);

        String[] dd = new String[mLikelyPlaceNames.size()];
        mLikelyPlaceNames.toArray(dd);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCustomTitle(dialogTitle)
                .setItems(dd, listener)
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
