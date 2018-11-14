package com.hanul.team1.triplan.ysh.query;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hanul.team1.triplan.ysh.dtos.PlanListDTO;
import com.hanul.team1.triplan.ysh.listview.PlanListAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PlanListSelect extends AsyncTask<Void, Void, Void> {

    ArrayList<PlanListDTO> dtos;
    PlanListAdapter adapter;
    ProgressDialog dialog;
    BufferedReader br;
    String userid;
    TextView planTvNull;

    public PlanListSelect(ArrayList<PlanListDTO> dtos, PlanListAdapter adapter, ProgressDialog dialog, String userid, TextView planTvNull) {
        this.dtos = dtos;
        this.adapter = adapter;
        this.dialog = dialog;
        this.userid = userid;
        this.planTvNull = planTvNull;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        publishProgress();
        dtos.clear();

        String requestUrl = "http://192.168.0.27/triplan/android/planlist?userid="+userid;
        String inputLine;
        StringBuilder resultSet = new StringBuilder();

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            br = new BufferedReader( new InputStreamReader( conn.getInputStream()));

            while( (inputLine = br.readLine()) != null){
                resultSet.append(inputLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = (JsonArray) jsonParser.parse(resultSet.toString());
        for(int i=0; i<jsonArray.size(); i++){
            JsonObject obj = (JsonObject) jsonArray.get(i);
            PlanListDTO dto = new Gson().fromJson(obj,PlanListDTO.class);
            dtos.add(dto);
        }

        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        dialog.dismiss();

        if(dtos.size()==0){
            planTvNull.setVisibility(View.VISIBLE);
        } else {
            planTvNull.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }

        if(br != null){
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
