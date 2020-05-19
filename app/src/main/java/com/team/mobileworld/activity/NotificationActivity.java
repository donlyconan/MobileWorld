package com.team.mobileworld.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.team.mobileworld.R;
import com.team.mobileworld.adapter.NotifyAdapter;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.database.Database;
import com.team.mobileworld.core.handle.Handler;
import com.team.mobileworld.core.handle.LocationInfo;
import com.team.mobileworld.core.object.Message;
import com.team.mobileworld.core.object.Weather;
import com.team.mobileworld.core.service.NotificationService;
import com.team.mobileworld.core.service.WeatherService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import maes.tech.intentanim.CustomIntent;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    private static final long WAIT_TIME = 5000;
    public static boolean runthread = true;
    Toolbar toolbar;
    NotifyAdapter adapter;
    RecyclerView recyclerView;
    static List<Message> list = new ArrayList<>(100);
    static Call<List<Message>> serviceload, serviceupdate;
    static Call<ResponseBody> weatherService;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        toolbar = findViewById(R.id.toolbar);
        adapter = new NotifyAdapter(this, list);
        recyclerView = findViewById(R.id.recycle_view);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(e -> finish());

        LocationInfo.worker = this::loadInfoWeather;
        settingsAndCkecking();
        loadNotification();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void settingsAndCkecking() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION
                    , Manifest.permission.ACCESS_FINE_LOCATION
            }, MainActivity.REQUEST_LOCATION);
        } else
            LocationInfo.getCurrentLocation();
        Log.d("Current location", "" + LocationInfo.getLatLng().toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MainActivity.REQUEST_LOCATION && grantResults.length > 0) {
            LocationInfo.getCurrentLocation();
        } else {
            showToast("Không thể truy cập GPS!");
        }
    }


    /**
     * Load thong bao
     */
    public void loadNotification() {
        list.clear();
        serviceload = NetworkCommon.getRetrofit()
                .create(NotificationService.class)
                .loadNotification(MainActivity.getCurrentUser().getAccesstoken());
        serviceload.enqueue(new Callback<List<Message>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                List<Message> newlist = response.body();
                Database.print("list=" + list);
                if (response.isSuccessful()) {
                    list.addAll(newlist);
                    list.sort((u, v) -> (int) (v.getDate().getTime() - u.getDate().getTime()));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                showToast(t.getMessage());
            }
        });
    }

    public void loadInfoWeather() {
        LatLng location = LocationInfo.getLatLng();

        weatherService = NetworkCommon.buildURL(WeatherService.BASE_URL)
                .create(WeatherService.class)
                .weartherPlace(location.latitude, location.longitude);
        weatherService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonObject json = Handler.convertToJSon(response.body().string());
                        Weather weather = Weather.jsonConvertPlace(json);
                        Message message = new Message(
                                String.format("Nhiệt độ %s hiện tại", weather.getCity())
                                , String.format("Nhiệt độ hiện tại: %.0f°C %s" +
                                        "\nCao nhất: %.0f°C" +
                                        "\nThấp nhất: %.0f°C" +
                                        "\nChúc bạn mua sắm vui vẻ, đừng quyên truy " +
                                        "cập https://mobileworld.com/flashsale để nhận thêm ưu đãi"
                                , weather.getTemp(), weather.getDecrip()
                                , weather.getTemp_max(), weather.getTemp_min())
                                , Calendar.getInstance().getTime()
                                , 1
                        );
                        list.add(0, message);
                        adapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, Animation.RIGHT_TO_LEFT);
    }

    public static void restart() {
        runthread = false;
        list.clear();
    }
}
