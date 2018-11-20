package com.hanul.team1.triplan.ysh;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hanul.team1.triplan.R;
import com.hanul.team1.triplan.ysh.dtos.MemoDTO;
import com.hanul.team1.triplan.ysh.retrofit.PlanInterface;
import com.hanul.team1.triplan.ysh.retrofit.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemoActivity extends AppCompatActivity {

    Button btnMemoClose, btnMemoModify, btnMemoModify_m;
    TextView memoTitle, memoContent, memoContent_m;
    int siteid;
    PlanInterface planInterface = RetrofitClient.getRetrofit().create(PlanInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sh_activity_memo);

        Intent intent = getIntent();
        siteid = intent.getIntExtra("siteid", 0);
        String siteName = intent.getStringExtra("siteName");

        memoContent = findViewById(R.id.memoContent);
        memoContent.setMovementMethod(new ScrollingMovementMethod());
        memoContent_m = findViewById(R.id.memoContent_m);
        memoTitle = findViewById(R.id.memoTitle);
        memoTitle.setText("'"+siteName+"' 메모");

        getMemoQuery();

        btnMemoClose = findViewById(R.id.btnMemoClose);
        btnMemoModify = findViewById(R.id.btnMemoModify);
        btnMemoModify_m = findViewById(R.id.btnMemoModify_m);

        btnMemoClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnMemoModify.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                memoContent.setVisibility(View.GONE);
                memoContent_m.setVisibility(View.VISIBLE);
                memoContent_m.setText(memoContent.getText());
                memoContent_m.setFocusableInTouchMode(true);
                memoContent_m.requestFocus();

                btnMemoModify.setVisibility(View.GONE);
                btnMemoModify_m.setVisibility(View.VISIBLE);
                btnMemoClose.setText("취소");
            }
        });

        btnMemoModify_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String modify_content = memoContent_m.getText().toString();
                if(modify_content.equals(memoContent.getText().toString())){
                    Toast.makeText(MemoActivity.this, "변경 사항이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    MemoDTO mdto = new MemoDTO();
                    modify_content = modify_content.replaceAll("\n", "<br>");
                    mdto.setContent(modify_content);
                    mdto.setSiteid(siteid);
                    updateMemoQuery(mdto);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getMemoQuery();
                        }
                    },500);
                }

                memoContent.setVisibility(View.VISIBLE);
                memoContent_m.setVisibility(View.GONE);
                btnMemoModify.setVisibility(View.VISIBLE);
                btnMemoModify_m.setVisibility(View.GONE);
                btnMemoClose.setText("확인");
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }


    private void getMemoQuery(){
        planInterface.getMemo(siteid).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String response_memo = response.body().string();
                    response_memo = response_memo.replaceAll("<br>","\n");
                    memoContent.setText(response_memo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void updateMemoQuery(MemoDTO mdto){
        planInterface.updateMemo(mdto).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    if(result.equalsIgnoreCase("true")){
                        Toast.makeText(getApplicationContext(), "변경 완료!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "변경 실패ㅠ", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
