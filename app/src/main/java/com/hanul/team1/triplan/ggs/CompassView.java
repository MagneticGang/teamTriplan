package com.hanul.team1.triplan.ggs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hanul.team1.triplan.jiyoon.StartActivity;


public class CompassView extends View {

    //삼각형
    Path s_path;

    //기기 해상도
    float Dwid, Dhie;

    //Activity에서 넘어오는 Azimuth (방위각) (단위는 radian) (1 radian= 약 114.65º) (º = 도, degree).
    float azi;                      //넘어오는 방위값은 radian= -3.14 ~ 3.14 (= -π ~ π) (degree= -180 ~ 180)
    //sin(azi)= -1~1. cos(azi)= -1~1.
    //기기의 중앙
    float halfDwid = Dwid / 2f;
    float halfDhie = Dhie / 2f;

    //setter
    public void setDwid(float dwid) {        Dwid = dwid;    }
    public void setDhie(float dhie) {        Dhie = dhie;    }
    public void setAzi(float azi) {        this.azi = azi;    }

    public CompassView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        s_path = new Path();

        //-------------------------배경-------------------------
        halfDwid = Dwid / 2f;
        halfDhie = Dhie / 2f;
        float compassRadius = Dwid * 0.35f;//나침반 반지름
        Log.d(StartActivity.TAG, "나침반 반지름: " + String.valueOf(compassRadius));

        //나침반 원
        Paint paint = new Paint();
        paint.setStrokeWidth(10f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        Log.d(StartActivity.TAG, "배경 원의 값: " + String.valueOf(halfDwid + ", " + halfDhie + ", " + compassRadius));
        canvas.drawCircle(halfDwid, halfDhie, compassRadius, paint);

        //N
        paint.setTextSize(80);
        paint.setColor(Color.RED);
        canvas.drawText("N", halfDwid - (paint.measureText("N") / 2f), halfDhie - 280, paint);

        //방위각 (radian 값) 이 있을 때
        //Sensor로부터 얻은 Azimuth는 북극과 기기의 y축 사이의 방위각의 radian값. (-π radian ~ π radian)
        if (azi != 0.0f) {

            //-------------------------south를 가르킬 하얀색 삼각형을 만든다-------------------------
            //Path로 삼각형을 그린다.


            //시작점
            //라디안 값으로 시작점의 x y 값을 구한다.
            //Math.sin(double radian), cos(double radian)
            //sinθ * radius = x좌표
            //cosθ * radius = y좌표
            //radian을 코딩에 사용하는 이유는 도를 표현하는 º가 없는게 수식을 계산하는데 편하기 때문이라고? 한다.
            float x = halfDwid + (float) (Math.sin(azi) * (compassRadius - 50));//
            float y = halfDhie + (float) (Math.cos(azi) * (compassRadius - 50));

            //south path의 시작점 설정
            s_path.moveTo(x, y);

            //왼쪽 꼭지점의 radian
            //왼쪽으로 90도 돌리고 (PI/2 = 90º) , 360도 (2PI = 360º) 로 제한.
            float left_radian = (float) (azi + Math.PI / 2) % (float) (2 * Math.PI);//Math.sin(left_radian) 결과값 -0.16 ~ 0.16

            //데이터의 위치를 좌표로 반환
            //radius 는 50
            x = halfDwid + (float) Math.sin(left_radian) * 50;//결과값 -8 ~ 8
            y = halfDhie + (float) Math.cos(left_radian) * 50;

            //path를 왼쪽 꼭지점으로 이동
            s_path.lineTo(x, y);

            //오른쪽 꼭지점의 radian
            //왼쪽으로 270(PI/2 * 3)도 돌리고 360도로 제한.
            float right_radian = (float) (azi + Math.PI * 3 / 2) % (float) (2 * Math.PI);

            //데이터의 위치를 좌표로 반환
            x = halfDwid + (float) (Math.sin(right_radian) * 50);
            y = halfDhie + (float) (Math.cos(right_radian) * 50);

            //path 오른쪽 꼭지점으로 이동
            s_path.lineTo(x, y);

            //path 닫음
            s_path.close();

            //path로 그린 삼각형을 흰색으로
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPath(s_path, paint);

            //-------------------------north 를 가르킬 빨간색 삼각형을 만든다-------------------------

            //north path
            Path n_path = new Path();

            //위치를 반대로 뒤집는다
            //왼쪽으로 PI ( 180도 ) 만큼 돌림
            float reverse_radian = (float) (azi + Math.PI) % (float) (2 * Math.PI);

            //데이터의 위치를 좌표로 반환
            x = halfDwid + (float) (Math.sin(reverse_radian) * (compassRadius - 50));
            y = halfDhie + (float) (Math.cos(reverse_radian) * (compassRadius - 50));

            //north path 의 시작점
            n_path.moveTo(x, y);

            //나머지 삼각형의 각을 좌표로 변환
            x = halfDwid + (float) (Math.sin(left_radian) * 50);
            y = halfDhie + (float) (Math.cos(left_radian) * 50);
            //path 이동
            n_path.lineTo(x, y);

            //나머지 삼각형의 각을 좌표로 변환
            x = halfDwid + (float) (Math.sin(right_radian) * 50);
            y = halfDhie + (float) (Math.cos(right_radian) * 50);
            //path 이동
            n_path.lineTo(x, y);
            //path 닫기
            n_path.close();

            //빨간색
            paint.setColor(Color.RED);
            canvas.drawPath(n_path, paint);
        }
    }
}

