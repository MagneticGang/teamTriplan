package com.hanul.team1.triplan.ysh.query;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MemoUpdate extends AsyncTask<Void, Void, Void> {

    String modify_content;
    int siteid;
    BufferedReader br;
    String result;
    Context context;

    public MemoUpdate( int siteid, String modify_content, Context context) {
        this.modify_content = modify_content;
        this.siteid = siteid;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        modify_content = modify_content.replace("\n", "<br/>");
        String requestURL = "http://192.168.0.27/triplan/android/memoupdate?siteid="+siteid+"&content="+modify_content;
        StringBuilder sb = new StringBuilder();
        String inputLine;

        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("content-type", "application/json;");

            br = new BufferedReader( new InputStreamReader( conn.getInputStream()));
            while( (inputLine = br.readLine()) != null){
                sb.append(inputLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        result = sb.toString();
        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if(result.equalsIgnoreCase("true")){
            Toast.makeText(context, "변경 완료!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "변경 실패ㅠ", Toast.LENGTH_SHORT).show();
        }
    }
}
