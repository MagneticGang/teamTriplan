package com.hanul.team1.triplan.ysh;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ggs.ggs_InfoReset;
import com.hanul.team1.triplan.ggs.retrofit.GNUserClient;
import com.hanul.team1.triplan.jiyoon.StartActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class MyPageFragment extends Fragment {      //implements Validator.ValidationListener

    //선언
    TextView TVnick;
    Button BtnSuggest, BtnPwdReset, BtnLogout, BtnSignout;
    SharedPreferences sp;
    final public static int REQUESTPOP = 1234;
    String nickname;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sh_fragment_mypage, container, false);

        //찾기
        TVnick = rootView.findViewById(R.id.TVnick);
        BtnSuggest = rootView.findViewById(R.id.BtnSuggest);
        BtnPwdReset = rootView.findViewById(R.id.BtnPwdReset);
        BtnLogout = rootView.findViewById(R.id.BtnLogout);
        BtnSignout = rootView.findViewById(R.id.BtnSignout);

        //sp로 로그인한 유저정보를 가져와 놓는다.
        //TV에 닉네임 출력
        sp = getActivity().getSharedPreferences("userProfile",Activity.MODE_PRIVATE);
        String nickname = sp.getString("nickname", "로그인 유저 없음!");
        TVnick.setText(nickname);
        TVnick.append(" 's\nMy Page");

        //회원탈퇴 버튼
        BtnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowConfirm();
            }
        });

        //내정보 관리 버튼
        BtnPwdReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //커스텀 액티비티
                Intent i = new Intent( getActivity(), ggs_InfoReset.class);
                startActivityForResult(i, REQUESTPOP);
            }
        });

        //로그아웃 버튼
        BtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DLconfirm();//dialog로 확인 취소
            }
        });

        return rootView;
    }//onCreateView

    //다시 화면이 호출될 때 이름이 변경되어 있도록!
    @Override
    public void onResume() {
        super.onResume();
        sp = getActivity().getSharedPreferences("userProfile",Activity.MODE_PRIVATE);
        nickname = sp.getString("nickname", "로그인 유저 없음!");
        TVnick.setText(nickname);
        TVnick.append(" 's\nMy Page");
    }

    //Intent 받는 곳!
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //InfoReset에서 보낸 인텐트를 받으면, 폭발을 예ㅔㅔㅔㅔㅔㅔㅔ술!
        //정보수정화면이 꺼지면 fragment를 새로고침한다!
        if(requestCode==REQUESTPOP){
           FragmentTransaction ft = getFragmentManager().beginTransaction();
           ft.detach(this).attach(this).commit();//땟다가 다시 붙여서 새로고침 효과?

        }
    }

    //로그인 화면으로 이동
    private void goStart(){
        Intent intent = new Intent(getActivity(), StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //sp 삭제
    private void clearSP(){
        SharedPreferences.Editor e = sp.edit();
        e.clear();
        e.commit();
    }

    //회원 탈퇴 네트워킹
    private class AUserSignout extends AsyncTask<Call, Void, String> {

        @Override
        protected String doInBackground(Call... calls) {
            try {
                Call<ResponseBody> call = calls[0];
                Response<ResponseBody> res = call.execute();
                if(res.isSuccessful()){
                    return res.body().string().trim();
                }else{
                    Log.e(StartActivity.TAG, "네트워킹 실패: "+ res.errorBody().string().trim());
                    return "";
                }
            } catch (Exception e) {
                Log.e(StartActivity.TAG, "네트워킹 실패: "+ e.getLocalizedMessage());
                Toast.makeText(getActivity(), "서버 응답이 없습니다! 관리자에게 문의하세요!", Toast.LENGTH_SHORT).show();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(StartActivity.TAG, "삭제여부: "+s);
            switch (Integer.parseInt(s)){
                case 1:
                    Toast.makeText(getActivity(), "지금까지 이용해주셔서\n감사힙니다!♡", Toast.LENGTH_LONG).show();
                    clearSP();//sp삭제
                    goStart();
                    break;
                default:
                    Toast.makeText(getActivity(), "탈퇴실패!\n관리자에게 문의해주세요!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    //회원 탈퇴 요청
    private void execSignout(){
        GNUserClient uc = GNUserClient.retrofit.create(GNUserClient.class);
        Call<ResponseBody> call = uc.UserSignOut(sp.getString("userid", "뭔가 잘못됐다"));
        try{
            new AUserSignout().execute(call);
        }catch (Exception e){Log.e(StartActivity.TAG, "탈퇴 AsyncTask 실패: "+e.getLocalizedMessage());}
    }

    //진짜 회원탈퇴 확인 dialog
    private void ShowRealConfirm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("안내")
                .setMessage("탈퇴를 위해서 다시 한번\n 확인을 눌러주세요! ")
                .setIcon(R.drawable.airplane)
                //예
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        execSignout();
                    }
                })
                //아니요
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();        //닫기
                    }
                }).show();
    }

    //회원탈퇴 확인 dialog
    private void ShowConfirm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("안내")
                .setMessage("Triplan에서 탈퇴 하시겠습니까?\n\n※주의: 저장된 정보가 전부 삭제됩니다!")
                .setIcon(R.drawable.airplane)
                //예
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShowRealConfirm();
                        dialog.dismiss();
                    }
                })
                //아니요
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();        //닫기
                    }
                }).show();
    }

    //로그아웃 버튼 누르면 로그아웃 할거냐고 물어보는 dialog창 띄우기
    private void DLconfirm(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
            .setTitle("안내")
            .setMessage("로그아웃 하시겠습니까?")
            .setIcon(R.drawable.airplane)
                //예
            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clearSP();//sp clear
                    Toast.makeText(getActivity(), "이용해 주셔서 고맙습니다 ^^♥", Toast.LENGTH_SHORT).show();
                    goStart();//로그인 화면으로
                }
            })
                //아니요
            .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();//닫기
                }
        }).show();

    }//DLconfirm


}//c
