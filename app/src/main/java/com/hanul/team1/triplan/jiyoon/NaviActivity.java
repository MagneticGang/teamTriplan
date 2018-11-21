package com.hanul.team1.triplan.jiyoon;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hanul.team1.triplan.R;
import com.kakao.kakaonavi.KakaoNaviParams;
import com.kakao.kakaonavi.KakaoNaviService;
import com.kakao.kakaonavi.Location;
import com.kakao.kakaonavi.NaviOptions;
import com.kakao.kakaonavi.options.CoordType;
import com.kakao.kakaonavi.options.RpOption;
import com.kakao.kakaonavi.options.VehicleType;

public class NaviActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jy_navi);

        // Location.Builder를 사용하여 Location 객체를 만든다.
        Location destination = Location.newBuilder("카카오 판교 오피스", 127.10821222694533, 37.40205604363057).build();
        NaviOptions options = NaviOptions.newBuilder().setCoordType(CoordType.WGS84).setVehicleType(VehicleType.FIRST).setRpOption(RpOption.SHORTEST).build();

        KakaoNaviParams.Builder builder = KakaoNaviParams.newBuilder(destination).setNaviOptions(options);
        KakaoNaviParams params = builder.build();


        KakaoNaviService.navigate(NaviActivity.this, builder.build());


    }


    private void checkDangerousPermissions() {
        String[] permissions = {

                //AndroidManifest.xml에서 처리한 권한을 여기에 추가해줘야 한다.
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        //권한을 승낙하겠는가 체크하기 위해 창을 띄움.
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}

