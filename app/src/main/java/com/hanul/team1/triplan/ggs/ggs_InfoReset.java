package com.hanul.team1.triplan.ggs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ggs.gnuserdto.GNUserDTO;
import com.hanul.team1.triplan.ggs.retrofit.GNUserClient;
import com.hanul.team1.triplan.jiyoon.StartActivity;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ggs_InfoReset extends AppCompatActivity implements Validator.ValidationListener {

    SharedPreferences sp;

    @NotEmpty(message = "별명을 입력해주세요.")
    EditText ETnickreset;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS, message = "비밀번호는 6자 이상, 알파벳 대·소문자,\n 숫자, 특수기호를 포함해야됩니다.")
    EditText ETpwdreset;

    Button BTNcommit;
    Button BTNbackmyp;

    byte[] bytes;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//상태바 제거. 전체화면
        setContentView(R.layout.ggs_activity_info_reset);

        //찾기
        ETnickreset = findViewById(R.id.ETnickreset);
        ETpwdreset = findViewById(R.id.ETpwdreset);
        BTNcommit = findViewById(R.id.BTNcommit);
        BTNbackmyp = findViewById(R.id.BTNbackmyp);

        validator = new Validator(this);
        validator.setValidationListener(this);

        //변경 버튼에 유효성 검사 이벤트
        BTNcommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        //닉 뿌려주기
        sp = getSharedPreferences("userProfile", MODE_PRIVATE);
        ETnickreset.setText(sp.getString("nickname", "서버연결실패! 관리자에게 문의해주세요!"));

        //돌아가기
        BTNbackmyp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }//onCreate

    //sp에 저장
    private void savesp(){
        SharedPreferences.Editor e = sp.edit();
        e.putString("nickname", ETnickreset.getText().toString());
        e.commit();
        Log.d(StartActivity.TAG, "수정된 닉값: " + sp.getString("nickname", "머임??"));
    }

    //사용자 정보 수정 요청 네트워킹 AsyncTask
    private class AsyncAndUserUpdate extends AsyncTask<Call, Void, String> {

        @Override
        protected String doInBackground(Call... calls) {
            try{
                Call<ResponseBody> call = calls[0];
                Response<ResponseBody> res = call.execute();
                return res.body().string().trim();
            }catch (Exception e){
                Log.e(StartActivity.TAG, "네트워킹 실패: "+ e.getLocalizedMessage());
                Toast.makeText(ggs_InfoReset.this, "서버 응답이 없습니다! 관리자에게 문의하세요!: "+ e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s) {
            Log.d(StartActivity.TAG, "서버 응답: " + s);

            switch(Integer.parseInt(s)){
                case 1:

                    //수정 성공
                    savesp();//sp에 저장하고

                    //fragment 새로고침을 위한 시점을 만들기 위해, setResult().
                    // 반대편에서는 onActivityResult()로 지금 보내는 intent를 받으면, 그게 정보수정이 완료된 시점이며 새로고침해야하는 시점!
                    Intent i = new Intent();
                    setResult(RESULT_OK, i);

                    Toast.makeText(ggs_InfoReset.this, "정보수정 성공!", Toast.LENGTH_LONG).show();
                    finish();//Activity닫음
                    break;
                default:
                    Toast.makeText(ggs_InfoReset.this, "정보수정 실패! 관리자에게 문의하세요! error: 1001", Toast.LENGTH_LONG).show();
                    break;
            }

        }
    }

    //입력값 > byte[]
    private byte[] inputToBytes(){
        String afterid = sp.getString("userid", "");
        String afternick = ETnickreset.getText().toString().trim();
        String afterpwd = ETpwdreset.getText().toString().trim();
        GNUserDTO dto = new GNUserDTO(afterid, afterpwd, afternick);
        Gson g = new GsonBuilder().create();
        try{
            return g.toJson(dto).getBytes("utf-8");
        }catch (Exception e){
            Log.e(StartActivity.TAG, "파싱 실패: "+e.getLocalizedMessage());
            Toast.makeText(this, "오류 발생! 관리자에게 문의하세요! : \n"+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            ActivityCompat.finishAffinity(this);
            return null;
        }
    }

    //유효
    @Override
    public void onValidationSucceeded() {
        Log.d(StartActivity.TAG, "유효성 검사 통과");
        bytes = inputToBytes();//입력 > byte[]
        Log.d(StartActivity.TAG, "파싱/인코딩 결과: "+ bytes);
        GNUserClient uc = GNUserClient.retrofit.create(GNUserClient.class);//retrofit interface 구체화
        Call<ResponseBody> call = uc.UserUpdate(bytes);//method 실행
        try {
            new AsyncAndUserUpdate().execute(call);
        }catch (Exception e){
            Log.e(StartActivity.TAG, "사용자 정보 수정 실패: "+e.getLocalizedMessage());
            Toast.makeText(this, "오류 발생! 관리자에게 문의하세요! : \n"+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            ActivityCompat.finishAffinity(this);
        }



//        Toast.makeText(this, "변경되었습니다.", Toast.LENGTH_SHORT).show();
    }

    //무효
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors){
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if(view instanceof EditText){((EditText)view).setError(message);}
            else{Toast.makeText(this, message, Toast.LENGTH_SHORT).show();}
        }
    }

    //activity 바깥은 터치 불가
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    //백버튼 불가
    @Override
    public void onBackPressed() {
        return;
    }


}//c
