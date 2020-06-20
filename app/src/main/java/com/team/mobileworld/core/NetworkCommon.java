package com.team.mobileworld.core;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkCommon {
    public static final int READ_TIME_OUT = 10000;
    public static final int WRITE_TIME_OUT = 10000;
    public static final int CONNECT_TIME_OUT = 8000;

    //URL co ban
    public static final String BASE_URL = "http://192.168.55.250:8080/";

    //mang su dung
    private static volatile Retrofit retrofit = null;

    private static volatile OkHttpClient okhttp = null;


    public static OkHttpClient getCustomOkHttp() {
        //Thiết lập Interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(Level.BODY);

        //Thiet lap OkHttp
        okhttp = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                .build();

        return okhttp;
    }

    public static Retrofit getRetrofit() {
        return getRetrofit(BASE_URL);
    }

    public static Retrofit getRetrofit(String url) {
        if (retrofit == null) {
            //Thiet lap OKhttp
            OkHttpClient okHttp = getCustomOkHttp();


            //Thiet lap retrofit
            retrofit = new Retrofit.Builder()
                    .client(okHttp)
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static Retrofit buildURL(String base_url) {
        //Thiet lap OKhttp
        OkHttpClient okHttp = getCustomOkHttp();


        //Thiet lap retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttp)
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static void setRetrofit(Retrofit retrofit) {
        NetworkCommon.retrofit = retrofit;
    }

    public static OkHttpClient getOkhttp() {
        return okhttp;
    }

    public static void setOkhttp(OkHttpClient okhttp) {
        NetworkCommon.okhttp = okhttp;
    }
}
