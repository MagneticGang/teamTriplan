package com.hanul.team1.triplan.ysh.query;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MemoSelect extends AsyncTask<Void, Void, Void> {

    String strMemo;
    int siteid;
    BufferedReader br;
    TextView memoContent;

    public MemoSelect(TextView memoContent, int siteid) {
        this.memoContent = memoContent;
        this.siteid = siteid;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        String requestURL = "http://192.168.0.27/triplan/android/memoselect?siteid="+siteid;
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

        strMemo = sb.toString().replace("<br/>","\n");
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        memoContent.setText(strMemo);
        if(br != null){
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
