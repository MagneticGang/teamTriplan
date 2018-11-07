package com.hanul.team1.triplan.ggs.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GNUserClient {

    public static final String GNUSERURL = "http://192.168.0.110/triplan/ggs/andgnuser/";

    //구글 로그인 하면 회원가입 처리
    @POST("User/GUserSignUp")
    Call<ResponseBody> GUserSignUp(
            @Query("isOk") byte[] GUserInfo
    );

    //Retrofit 객체 생성
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GNUSERURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

}
