package com.hanul.team1.triplan.ysh.query;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.hanul.team1.triplan.ysh.dtos.DayListDTO;
import com.hanul.team1.triplan.ysh.listview.DayListAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DayListSelect extends AsyncTask<Void, Void, Void> {
    ArrayList<DayListDTO> dtos;
    DayListAdapter adapter;
    int planid;
    BufferedReader br;
    ProgressDialog dialog;

    public DayListSelect(ArrayList<DayListDTO> dtos, DayListAdapter adapter, int planid, ProgressDialog dialog) {
        this.dtos = dtos;
        this.adapter = adapter;
        this.planid = planid;
        this.dialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        dtos.clear();

        String requestUrl = "http:192.168.0.104/triplan/android/daylist?planid="+planid;
        String inputLine;
        StringBuilder resultSet = new StringBuilder();

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("content-type","application/json");
            br = new BufferedReader( new InputStreamReader( conn.getInputStream()));

            while( (inputLine = br.readLine()) != null){
                resultSet.append(inputLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = (JsonArray) jsonParser.parse(resultSet.toString());
        for(int i=0; i < jsonArray.size(); i++){
            JsonObject obj = (JsonObject) jsonArray.get(i);
            DayListDTO dto = new Gson().fromJson(obj, DayListDTO.class);
            dtos.add(dto);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        dialog.dismiss();
        adapter.notifyDataSetChanged();
        if(br != null){
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
