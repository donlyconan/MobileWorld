package com.team.mobileworld.core.service;

import com.team.mobileworld.core.object.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NotificationService {

    public static final String URL_LOAD = "/api/bill/notify?";

    public static final String URL_LOAD_UPDATE = "/api/bill/notify?";

    @GET(URL_LOAD)
    Call<List<Message>> loadNotification(@Query("userid") long userid);


    @GET(URL_LOAD_UPDATE)
    Call<List<Message>> loadNotificationUpdate(@Query("userid") long userid);

}
