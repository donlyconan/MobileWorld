package com.team.mobileworld.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.team.mobileworld.R;
import com.team.mobileworld.adapter.OrderApdater;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.handle.ValidNetwork;
import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.core.service.BillService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillActivity extends AppCompatActivity {

    ImageButton btnback;
    SearchView search;
    RecyclerView recycler;
    private List<Order> list;
    OrderApdater adapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        //Anhxa phan tu
        elemetMapping();

        //Cau hinh doi tuong
        init();

        //su kien quay tro lai
        btnback.setOnClickListener(e -> finish());

        //Lay du lieu tren server
        loadDataFromServer();

        //Thuc hien searh
        onActionSearch();
    }


    private void init() {
        list = new ArrayList<>();
        adapter = new OrderApdater(this, list);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
    }

    private void elemetMapping() {
        btnback = findViewById(R.id.btn_back);
        recycler = findViewById(R.id.recycler);
        search = findViewById(R.id.search_bar);
    }

    void  loadDataFromServer(){
        if(ValidNetwork.hasNetwork(this))
        {
            User user = MainActivity.getUser();
            //Thiet lap service
            BillService service = NetworkCommon.getRetrofit().create(BillService.class);

            Call<List<Order>> call = service.loadBill(user.getId());

            call.enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    list = response.body();
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    Toast.makeText(BillActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            AlertDialog builder = new AlertDialog.Builder(this)
                    .setTitle(MainActivity.APP_NAME)
                    .setMessage("Không có kết nối Internet")
                    .show();
            builder.show();
        }
    }

    private void onActionSearch() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(list.size() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(BillActivity.this)
                            .setTitle(MainActivity.APP_NAME)
                            .setMessage("Danh sách trống")
                            .setPositiveButton("OK", null);
                    builder.show();
                }

                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {
                List<Order> newList = new ArrayList<>();
                list.stream().filter(e -> e.getName().contains(newText)).forEach(newList::add);
                adapter.setList(newList);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        search.setOnCloseListener(() -> {
            adapter.setList(list);
            adapter.notifyDataSetChanged();
            return true;
        });
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        CustomIntent.customType(this, Animation.LEFT_TO_RIGHT);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        CustomIntent.customType(this, Animation.LEFT_TO_RIGHT);
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, Animation.RIGHT_TO_LEFT);
    }
}
