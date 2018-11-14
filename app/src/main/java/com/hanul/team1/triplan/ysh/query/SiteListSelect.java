package com.hanul.team1.triplan.ysh.query;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hanul.team1.triplan.ysh.dtos.DayListDTO;
import com.hanul.team1.triplan.ysh.dtos.SiteListDTO;
import com.hanul.team1.triplan.ysh.listview.SiteListAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SiteListSelect extends AsyncTask<Void, Void, Void> {
    ArrayList<SiteListDTO> dtos;
    SiteListAdapter adapter;
    DayListDTO dayDto;
    BufferedReader br;
    ProgressDialog dialog;

    public SiteListSelect(ArrayList<SiteListDTO> dtos, SiteListAdapter adapter, DayListDTO dayDto,ProgressDialog dialog) {
        this.dtos = dtos;
        this.adapter = adapter;
        this.dayDto = dayDto;
        this.dialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... voids) {
        String requestURL = "http://192.168.0.27/triplan/android/sitelist?dayid="+dayDto.getDayid()+"&planid="+dayDto.getPlanid();
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("content-type","application/json");

            br = new BufferedReader( new InputStreamReader( conn.getInputStream()));
            String line;

            while( (line=br.readLine()) != null){
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = (JsonArray) jsonParser.parse(sb.toString());
        for(int i=0; i<jsonArray.size();i++){
            JsonObject obj = (JsonObject) jsonArray.get(i);
            SiteListDTO dto = new Gson().fromJson(obj, SiteListDTO.class);
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
