package com.team.mobileworld.core.object;

import com.google.gson.JsonObject;
import com.team.mobileworld.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Weather {
    private String city;
    private String decrip;
    private float temp;
    private float feels_like;
    private float temp_min;
    private float temp_max;

    public Weather() {
        // TODO Auto-generated constructor stub
    }

    public Weather(String city, String decrip, float temp, float feels_like, float temp_min, float temp_max) {
        super();
        this.city = city;
        this.decrip = decrip;
        this.temp = temp;
        this.feels_like = feels_like;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
    }

    public static Weather jsonConvertPlace(JsonObject json) {
        String city = json.get("name").getAsString();
        String decrip = json.getAsJsonArray("weather").get(0)
                .getAsJsonObject().get("description").getAsString();
        JsonObject jtemp = json.getAsJsonObject("main");
        float temp = jtemp.get("temp").getAsFloat() - 273.15f;
        float feels_like = jtemp.get("feels_like").getAsFloat() - 273.15f;
        float temp_min = jtemp.get("temp_min").getAsFloat() - 273.15f;
        float temp_max = jtemp.get("temp_max").getAsFloat() - 273.15f;

        return new Weather(city, decrip, temp, feels_like, temp_min, temp_max);
    }

    @Override
    public String toString() {
        return "Weather [city=" + city + ", decrip=" + decrip + ", temp=" + temp + ", feels_like=" + feels_like
                + ", temp_min=" + temp_min + ", temp_max=" + temp_max + "]";
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDecrip() {
        return decrip;
    }

    public void setDecrip(String decrip) {
        this.decrip = decrip;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(float feels_like) {
        this.feels_like = feels_like;
    }

    public float getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(float temp_min) {
        this.temp_min = temp_min;
    }

    public float getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(float temp_max) {
        this.temp_max = temp_max;
    }

}
