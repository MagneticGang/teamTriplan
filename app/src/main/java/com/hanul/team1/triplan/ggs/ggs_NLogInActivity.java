package com.hanul.team1.triplan.ggs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ggs.gnuserdto.GNUserDTO;
import com.hanul.team1.triplan.ggs.retrofit.GNUserClient;
import com.hanul.team1.triplan.jiyoon.StartActivity;
import com.hanul.team1.triplan.jiyoon.SuccessActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ggs_NLogInActivity extends AppCompatActivity {

    //선언
    EditText NLEmailET, NLPwdET;
    Button NLBtn, NSBtn;
    byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ggs_activity_nlog_in);

        //찾
        NLEmailET = findViewById(R.id.NLEmailET);//이멜
        NLPwdET = findViewById(R.id.NLPwdET);//비번
        NLBtn = findViewById(R.id.NLBtn);//로긴
        NSBtn = findViewById(R.id.NSBtn);//회갑

        //일반로긴 버튼
        NLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execNLogin();
            }
        });

        //일반회갑 버튼
        NSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toNSignIpIntent = new Intent(ggs_NLogInActivity.this, ggs_NSignUpActivity.class);
                startActivity(toNSignIpIntent);
            }
        });

    }//onCreate

    //로긴 성공 시 SP에 저장 및 화면 전환
    private void saveToSP(String nickname){
        SharedPreferences sp = getSharedPreferences("userProfile", MODE_PRIVATE);
        SharedPreferences.Editor e= sp.edit();
        e.clear();
        e.putString("userid", NLEmailET.getText().toString().trim());
        e.putString("nickname", nickname);
        e.commit();

        //로그인 성공 시 이동.
        Intent intent = new Intent(getApplicationContext(), SuccessActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }//saveToSP

    //일반 로긴 ASYNCTASK
    private class ANUserLogin extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call... calls) {
            try {
                Response<ResponseBody> res = calls[0].execute();
                return res.body().string().trim();
            } catch (Exception e) {
                Log.e(StartActivity.TAG, "네트워킹 오류: "+e.getLocalizedMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            //일치하면 닉네임 리턴, 비번이나 아디가 틀리면 null
            switch (s != null ? 1 : 0){
                case 1:
                    Toast.makeText(ggs_NLogInActivity.this, "반갑습니다, "+ s + " 님!", Toast.LENGTH_SHORT).show();
                    saveToSP(s);//SP에 저장 및 화면 전환
                    break;
                default:
                    Toast.makeText(ggs_NLogInActivity.this, "일치하는 아이디 및\n비밀번호가 없어요ㅠㅠ", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    //입력값 > DTO > JSON String > byte[]
    private byte[] inputToBytes(){
        String sId = NLEmailET.getText().toString().trim();
        String sPwd = NLPwdET.getText().toString().trim();
        GNUserDTO dto = new GNUserDTO();
        dto.setUserid(sId);
        dto.setUserpwd(sPwd);
        Gson g = new GsonBuilder().create();
        try{
            bytes = g.toJson(dto).getBytes("utf-8");
            Log.d(StartActivity.TAG, "파싱/인코딩 결과: "+ bytes);
            return bytes;
        }catch (Exception e){
            Log.e(StartActivity.TAG, "파싱 실패: " + e.getLocalizedMessage());
            Toast.makeText(this, "에러 발생! 관리자에게 문의하세요!", Toast.LENGTH_SHORT).show();
            return null;
        }
    }//inputToBytes

    //일반 로긴
    private void execNLogin(){

        bytes = inputToBytes();
        GNUserClient uc = GNUserClient.retrofit.create(GNUserClient.class);
        Call<ResponseBody> call = uc.NUserLogIn(bytes);
        try{
            new ANUserLogin().execute(call);
        }catch(Exception e){
            Log.e(StartActivity.TAG, "AsyncTask: "+e.getLocalizedMessage());
        }

    }//execNLogin

}//c
