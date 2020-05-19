package com.team.mobileworld.core.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    public static final String BASE_URL = "http://api.openweathermap.org/";

    @GET("data/2.5/weather?appid=20653c9dc554e39770224aa6a8891cb7&lang=vi")
    public Call<ResponseBody> weartherPlace(@Query("lat") double lat, @Query("lon") double lon);
}