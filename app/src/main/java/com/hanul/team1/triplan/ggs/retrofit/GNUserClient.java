package com.hanul.team1.triplan.ggs.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GNUserClient {

    public static final String GNUSERURL = "http://192.168.0.27/triplan/ggs/andgnuser/";

    //회원 탈퇴 요청
    @POST("User/UserSignOut")
    Call<ResponseBody> UserSignOut(
            @Query("requestedID") String userid
    );

    //회원 정보 수정 요청
    @POST("User/UserUpdate")
    Call<ResponseBody> UserUpdate(
            @Query("UserInfo") byte[] UserInfo
    );

    //일반 회원 로긴 처리
    @POST("User/NUserLogIn")
    Call<ResponseBody> NUserLogIn(
            @Query("NUserInfo") byte[] NUserInfo
    );

    //일반 회원 회갑 처리
    @POST("User/NUserSignUp")
    Call<ResponseBody> NUserSignUp(
            @Query("NUserInfo") byte[] NUserInfo
    );

    //구글 로그인 하면 회갑 처리
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
