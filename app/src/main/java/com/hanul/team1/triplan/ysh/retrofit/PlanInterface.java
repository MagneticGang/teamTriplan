package com.hanul.team1.triplan.ysh.retrofit;

import com.hanul.team1.triplan.ysh.dtos.DayListDTO;
import com.hanul.team1.triplan.ysh.dtos.LatLngSiteVO;
import com.hanul.team1.triplan.ysh.dtos.MemoDTO;
import com.hanul.team1.triplan.ysh.dtos.PlanListDTO;
import com.hanul.team1.triplan.ysh.dtos.SiteListDTO;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PlanInterface {
    @GET("planlist")
    Call<List<PlanListDTO>> getPlanList(@Query("userid") String userid);

    @GET("daylist")
    Call<List<DayListDTO>> getDayList(@Query("planid") int planid);

    @FormUrlEncoded
    @POST("sitelist")
    Call<List<SiteListDTO>> getSiteList(@FieldMap HashMap<String, Integer> map);

    @GET("memoselect")
    Call<ResponseBody> getMemo(@Query("siteid") int siteid);

    @POST("memoupdate")
    Call<ResponseBody> updateMemo(@Body MemoDTO mdto);

    @GET("latlngQuery")
    Call<List<LatLngSiteVO>> getLatlngQuery(@Query("planid") int planid);
}
