package com.hanul.team1.triplan.ysh.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://115.23.6.9/triplan/android/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
