package com.hanul.team1.triplan.ggs;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.jiyoon.StartActivity;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    //customview 에서 Thread로 그리기
    CompassView compassView;
    Thread compassThread;
    CompassRunnable compassRunnable;

    private float mAzimuth;                       //각도(Degree)가 float로 계산된다. 방위각, 높낮이,

    // 자기 나침반을 위함. 남/북극에서는 사용불가. 바늘의 N극이 북쪽을 가르키는 그 나침반이다!
    private SensorManager mSensorManager;                //센서메니저
    private Sensor mAccerlerometer;                      //가속도 감지를 위한 센서
    private Sensor mMagneticField;                       //자기장 감지를 위한 센서.

    float[] mGravity;                       //가속도계 센서의 리턴값
    float[] mGeomagnetic;                       //자기장 센서의 리턴값



    //TV에 찍기
    TextView TVAzi;
    String azimuth;
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//title 제거
        setContentView(R.layout.activity_compass);

        compassView = findViewById(R.id.compassView);

        TVAzi= findViewById(R.id.TVAzi);

        //센서 서비스 호출, 센서 메니저 객체를 리턴
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        //센서 메니저 객체로 가속도계 센서 호출, 센터 객체를 리턴
        mAccerlerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //센서 메니저 객체로 자기장 센서 호출, 센서 객체를 리턴
        mMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //해상도 구해서 넘기기
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        compassView.setDwid((float) size.x);
        compassView.setDhie((float) size.y);

        //누르면 사용법이 뜨도록
        TVAzi.setTextSize(30);
        TVAzi.setY((size.y/5f));
        TVAzi.setText("사용법");
        TVAziClick();

        //Thread 시작
        StartCompassThread();

    }//onCreate


    //TV클릭하면 사용법 나오게
    private void TVAziClick(){
        TVAzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CompassActivity.this);
                builder
                        .setTitle("사용법")
                        .setMessage("\n▶ 먼저 핸드폰을 수평에 맞춰주세요." +
                                "\n      그 다음 붉은 색 바늘을 N 글자로" +
                                "\n      향하도록 하면 해당 방향이\n      북쪽입니다.\n" +
                                "\n▶ 이 나침반은 자기 나침반입니다." +
                                "\n      북극/남극에서는 사용할 수\n      없습니다!" +
                                "\n")
                        //예
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();        //닫기
                            }
                        }).show();
            }
        });
    }


    //------------------------방향 시작------------------------
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccerlerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {    }

    //리스너에 변화가 생기면 onSensorChanged가 호출된다
    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            //가속도 변화 시
            mGravity = event.values;//float[] 값 리턴
        }
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            //자기장 변화 시
            mGeomagnetic = event.values;//float[] 값 리턴
        }
        if(mGravity!= null && mGeomagnetic!= null){ //가속도와 자기장 변화 값이 동시에 들어오지 않으므로, 둘 다 들어왔을 때 계산된다. 따라서 딜레이가 있음.
            //가속도와 자기장이 변했을 때
            float R[]  = new float[9];
            //float I[]  = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, null, mGravity, mGeomagnetic);//계산하여
            if(success){
                float[] orientation = new float[3];
                //방향 (Azimuth, roll, pitch) 을 얻는다. radian값!
                SensorManager.getOrientation(R, orientation);

                //필드에 저장
                mAzimuth =orientation[0];

                //TV에 뿌리기
                azimuth = String.valueOf(mAzimuth < 0 ? (int) (Math.toDegrees(mAzimuth)+360) : (int) (Math.toDegrees(mAzimuth)));


            }
        }

    }//onSensorChanged()

    //------------------------방향 끝------------------------

    //------------------------Thread 시작------------------------

    //Thread
    public void StartCompassThread(){
        compassRunnable = new CompassRunnable();
        compassThread = new Thread(compassRunnable);
        compassThread.setDaemon(true);
        compassThread.start();
    }

    //Runnable
    public class CompassRunnable implements Runnable{
        @Override
        public void run() {
            while(compassView != null){
                try{
                    //Azimuth 라디안 값을 View에 갱신
                    compassView.setAzi(mAzimuth);

//                    cnt++;
//                    TVAzi.setText("방위각: "+ azimuth + "\nThread에서 View로 넘기는 값: " + String.valueOf(mAzimuth)
//                            + "\nThread실행 횟수: " + String.valueOf(cnt));

                    Message msg = Message.obtain();
                    msg.what = 0;
                    compassHandler.handleMessage(msg);//핸들러 내부 메소드와 이름 맞는가

                }catch (Exception e){
                    Log.e(StartActivity.TAG, "Runnable 예외: "+ e.getLocalizedMessage());
                }
                try{
                    Thread.sleep(50);
                }catch (Exception e){
                    Log.e(StartActivity.TAG, "Runnable 예외: "+ e.getLocalizedMessage());
                }

            }
        }
    }

    //compass handler
    Handler compassHandler = new Handler(){
        public void handleMessage(Message msg){ //왜 ?? 될 때가 있고 안될 때가 있는가?
            if(msg.what == 0){
                compassView.invalidate();
            }
        }
    };

    //------------------------Thread 끝------------------------


}//c












