package com.hanul.team1.triplan.ysh;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ggs.CompassActivity;
import com.hanul.team1.triplan.jiyoon.NaviActivity;
import com.hanul.team1.triplan.jiyoon.PapagoActivity;


public class Fn3Fragment extends Fragment {

    Button btnPapago;
    Button btnCompass;
    Button btnNavi;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sh_fragment_fn3, container, false);

        //번역기 시작
        btnPapago = (Button) rootView.findViewById(R.id.button1);

        btnPapago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //호출 (예시는 fragment)
                Intent i = new Intent( getActivity(), PapagoActivity.class);
                startActivity(i);
            }
        });
        //번역기 끝

        //나침반 버튼 시작
        btnCompass = rootView.findViewById(R.id.btnCompass);
        btnCompass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tocompas = new Intent(getActivity(), CompassActivity.class);
                startActivity(tocompas);
            }
        });
        //나침반 버튼 끝

        btnNavi = rootView.findViewById(R.id.btnNavi);
        btnNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navi = new Intent(getActivity(), NaviActivity.class);
                startActivity(navi);
            }
        });


        return rootView;


    }//onCreateView

}
