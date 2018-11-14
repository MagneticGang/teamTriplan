package com.hanul.team1.triplan.jiyoon;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hanul.team1.triplan.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PapagoActivity extends AppCompatActivity {

    private Button translationBtn, finishBtn;
    private EditText translationText;
    private TextView resultText;
    private String result;

    //백그라운드에서 파파고API와 연결하여 번역결과를 가져옵니다.
    class BackgroundTask extends AsyncTask<Integer, Integer, Integer> {
        protected void onPreExcute() {
        }

        @Override
        protected Integer doInBackground(Integer... arg0) {
            StringBuilder output = new StringBuilder();
            String clientId = "d0vpwyJ9njnWkoW2kmYM";
            String clientSecret = "Ym7j_G1Ocj";

            try {
                //번역문을 UTF-8로 인코딩
                String text = URLEncoder.encode(translationText.getText().toString(), "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/papago/n2mt";

                //파파고 API와의 연결 수행
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                //번역할 문장을 파라미터로 전송
                String postParams = "source=ko&target=en&text=" + text;
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();

                //번역결과 받아오기
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    output.append(inputLine);
                }
                br.close();
            }catch (Exception ex) {
                Log.e("SampleHTTP", "Exception in processing response", ex);
                ex.printStackTrace();
            }
            result = output.toString();
            return null;
        }

        protected  void onPostExecute(Integer a){
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            if (element.getAsJsonObject().get("errorMessage") !=  null) {
                Log.e("번역 오류", "번역 오류가 발생했습니다." +
                "[오류 코드 :" + element.getAsJsonObject().get("errorCode").getAsString()+"]");
            }else if(element.getAsJsonObject().get("message") != null){
                //번역 결과 출력
                resultText.setText(element.getAsJsonObject().get("message").getAsJsonObject().get("result")
                        .getAsJsonObject().get("translatedText").getAsString());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jy_papago);

        translationText = (EditText) findViewById(R.id.translationText);
        translationBtn = (Button) findViewById(R.id.translationBtn);
        resultText = (TextView) findViewById(R.id.resultText);

        finishBtn = (Button) findViewById(R.id.finishBtn);

        translationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackgroundTask().execute();
            }
        });

        //돌아가기
        finishBtn = findViewById(R.id.finishBtn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }//onCreate

    //activity 바깥은 터치 불가
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }


}//c