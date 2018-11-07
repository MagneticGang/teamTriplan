package com.hanul.team1.triplan.ggs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ggs.gnuserdto.GNUserDTO;
import com.hanul.team1.triplan.ggs.retrofit.GNUserClient;
import com.hanul.team1.triplan.jiyoon.StartActivity;
import com.hanul.team1.triplan.jiyoon.SuccessActivity;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmEmail;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ggs_NSignUpActivity extends AppCompatActivity implements Validator.ValidationListener {

    //선언 및 유효성 검사
    byte[] bytes;

    @NotEmpty(message = "이메일을 입력해주세요.")
    @Email(message = "유효하지 않은 이메일입니다.")
    @Length(min = 5, max = 30, message = "이메일은 최소 5자,\n최대 30자로 적어주세요.")
    EditText NSEmailET;

    @ConfirmEmail(message = "이메일이 일치하지 않습니다.")
    EditText NSEmailCFET;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS, message = "비밀번호는 6자 이상, 알파벳 대·소문자,\n 숫자, 특수기호를 포함해야됩니다.")
    EditText NSPwdET;

    @ConfirmPassword(message = "비밀번호가 일치하지 않습니다.")
    EditText NSPwdCFET;

    @NotEmpty(message = "별명을 입력해주세요.")
    @Length(min = 2, max = 20, message = "별명은 최소 2자, 최대 20자 입니다.")
    EditText NSNicknameET;

    Button NSSubmitBtn;

    @Checked(message = "이용약관 및 개인정보 보호정책에\n동의해 주세요.")
    CheckBox NSCKB;

    private Validator validator;

    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ggs_activity_nsign_up);

        //찾
        NSEmailET = findViewById(R.id.NSEmailET);
        NSEmailCFET = findViewById(R.id.NSEmailCFET);
        NSPwdET = findViewById(R.id.NSPwdET);
        NSPwdCFET = findViewById(R.id.NSPwdCFET);
        NSNicknameET = findViewById(R.id.NSNicknameET);
        NSSubmitBtn = findViewById(R.id.NSSubmitBtn);
        NSCKB = findViewById(R.id.NSCKB);

        //회갑 유효성 검사
        validator = new Validator(this);
        validator.setValidationListener(this);
        NSSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

    }//onCreate

    //로긴 성공 시 SP에 저장
    private void saveToSP(){
        SharedPreferences sp = getSharedPreferences("userProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putString("userid", NSEmailCFET.getText().toString().trim());
        editor.putString("nickname", NSNicknameET.getText().toString().trim());
        editor.commit();

        //로그인 성공 시 이동.
        Intent intent = new Intent(getApplicationContext(), SuccessActivity.class);
        startActivity(intent);
        finish();
    }//saveToSP

    //일반회갑 AsyncTask
    private class ANUserSignUp extends AsyncTask<Call, Void, String> {

        @Override
        protected String doInBackground(Call... calls) {
            try {
                Call<ResponseBody> call = calls[0];
                Response<ResponseBody> res = call.execute();
                return res.body().string().trim();
            } catch (Exception e) {
                Log.e(StartActivity.TAG, "네트워킹 실패: "+ e.getLocalizedMessage());
                Toast.makeText(ggs_NSignUpActivity.this, "서버 응답이 없습니다! 관리자에게 문의하세요!", Toast.LENGTH_SHORT).show();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {

            Log.d(StartActivity.TAG, "서버 응답: " + s);

            switch (Integer.parseInt(s)){
                case 1://1이면 가입 성공
                    String id = NSEmailCFET.getText().toString();
                    String name = NSNicknameET.getText().toString();
                    Toast.makeText(ggs_NSignUpActivity.this, "회원가입을 축하드립니다, "+ name + " 님!", Toast.LENGTH_SHORT).show();
                    //SP에 저장 후 다음 화면으로
                    saveToSP();
                    break;
                case -1://-1은 중복된 아이디
                    Toast.makeText(ggs_NSignUpActivity.this, "중복된 이메일 입니다.", Toast.LENGTH_SHORT).show();
                    break;
                default://0일 때 서버에서 처리 실패
                    Toast.makeText(ggs_NSignUpActivity.this, "서버 응답이 없습니다! 관리자에게 문의하세요!", Toast.LENGTH_SHORT).show();
                    break;
            }



        }
    }//AsyncTask


    //입력값 > DTO > JSON String > byte[]
    private byte[] inputToBytes(){
        String sId = NSEmailCFET.getText().toString().trim();
        String sPwd = NSPwdCFET.getText().toString().trim();
        String sName = NSNicknameET.getText().toString().trim();
        GNUserDTO dto = new GNUserDTO(sId, sPwd, sName);
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

    //성공시
    @Override
    public void onValidationSucceeded() {
        Log.d(StartActivity.TAG, "유효성 검사 통과");
        bytes = inputToBytes();
        GNUserClient uc = GNUserClient.retrofit.create(GNUserClient.class);
        Call<ResponseBody> call = uc.NUserSignUp(bytes);
        try{
            new ANUserSignUp().execute(call);
        }catch (Exception e){
            Log.e(StartActivity.TAG, "일반회갑 AsyncTask 오류: "+e.getLocalizedMessage());
            Toast.makeText(this, "서버 응답이 없습니다! 관리자에게 문의해주세요!", Toast.LENGTH_SHORT).show();

        }
        Toast.makeText(this, NSNicknameET.getText().toString()+" 님, 회원가입을 축하드립니다!", Toast.LENGTH_SHORT).show();
        saveToSP();

    }

    //실패 시
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors){
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if(view instanceof EditText){((EditText)view).setError(message);}
            else{Toast.makeText(this, message, Toast.LENGTH_SHORT).show();}
        }
    }

}//c
