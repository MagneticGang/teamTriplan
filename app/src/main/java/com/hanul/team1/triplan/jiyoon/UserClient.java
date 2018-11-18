package com.hanul.team1.triplan.jiyoon;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserClient {

    public static final String TESTURL ="http://115.23.6.9/triplan/user/";
    @POST("insert")
    Call<ResponseBody> sendUuid(
            @Query("isOk") byte[] uuid
    );

    public  static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(TESTURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

}
