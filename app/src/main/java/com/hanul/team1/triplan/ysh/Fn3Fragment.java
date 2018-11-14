package com.hanul.team1.triplan.ysh;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.jiyoon.PapagoActivity;


public class Fn3Fragment extends Fragment {

    Button btnPapago;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sh_fragment_fn3, container, false);

        btnPapago = (Button) rootView.findViewById(R.id.button1);

        btnPapago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //호출 (예시는 fragment)
                Intent i = new Intent( getActivity(), PapagoActivity.class);
                startActivity(i);
            }
        });

        return rootView;


    }//onCreateView

}
