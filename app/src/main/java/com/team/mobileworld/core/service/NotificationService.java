package com.team.mobileworld.core.service;

import com.team.mobileworld.core.object.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface NotificationService {

    public static final String API_BILL_NOTIFY = "/api/bill/notify";

    public static final String URL_LOAD_UPDATE = "/api/bill/notify";

    @GET(API_BILL_NOTIFY)
    Call<List<Message>> loadNotification(@Header("x-access-token") String accesstoken);


    @GET(URL_LOAD_UPDATE)
    Call<List<Message>> loadNotificationUpdate(@Header("x-access-token") String accesstoken);

}
