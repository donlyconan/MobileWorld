package com.team.mobileworld.activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.team.mobileworld.R;
import com.team.mobileworld.adapter.NotifyAdapter;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.database.Database;
import com.team.mobileworld.core.object.Message;
import com.team.mobileworld.core.service.NotificationService;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityNotification extends AppCompatActivity {
    private static final long WAIT_TIME = 5000;
    public static boolean runthread = true;
    Toolbar toolbar;
    NotifyAdapter adapter;
    RecyclerView recyclerView;
    static List<Message> list = new ArrayList<>(100);
    static Call<List<Message>> serviceload, serviceupdate;


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
        toolbar.setNavigationOnClickListener(e->finish());

        loadNotification();

    }

    /**
     * Load thong bao
     */
    public void loadNotification() {
        list.clear();
        serviceload = NetworkCommon.getRetrofit()
                .create(NotificationService.class)
                .loadNotification(MainActivity.getUser().getId());
        serviceload.enqueue(new Callback<List<Message>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                List<Message> newlist = response.body();
                Database.print("list=" + list);
                if (response.isSuccessful()) {
                    list.addAll(newlist);
                    list.sort((u,v)-> (int) (v.getDate().getTime()-u.getDate().getTime()));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                showToast(t.getMessage());
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

    public static void restart(){
        runthread = false;
        list.clear();
    }
}
