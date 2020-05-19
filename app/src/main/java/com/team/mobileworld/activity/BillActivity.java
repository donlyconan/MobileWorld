package com.team.mobileworld.activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.team.mobileworld.R;
import com.team.mobileworld.adapter.OrderApdater;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.core.service.BillService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillActivity extends AppCompatActivity {
    private List<Order> list;
    private OrderApdater adapter;

    TabLayout tabLayout;
    SearchView search;
    RecyclerView recycler;
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        //Anhxa phan tu
        elemetMapping();

        tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getCurrentDatas();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                getCurrentDatas();
            }
        });

        //Lay du lieu tren server
        getCurrentDatas();

    }

    public void getCurrentDatas() {
        int index = tabLayout.getSelectedTabPosition();

        switch (index) {
            case 0:
                loadDataFromServer(Order.STATUS_CONFIRM);
                break;
            case 1:
                loadDataFromServer(Order.STATUS_DELIVRY);
                break;
            case 2:
                loadDataFromServer(Order.STATUS_RECEIVE);
                break;
            case 3:
                loadDataFromServer(Order.STATUS_CANCLE);
                break;
        }

    }


    private void elemetMapping() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabs_layout);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(e -> finish());
        recycler = findViewById(R.id.recycle_view);
        list = new ArrayList<>(50);
        adapter = new OrderApdater(this, list, OrderApdater.ITEM_BILL_VIEW);
        recycler.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recycler.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bill, menu);
        MenuItem item = menu.findItem(R.id.it_search_bar);
        search = (SearchView) item.getActionView();
        SearchManager manager = (SearchManager) getSystemService(SEARCH_SERVICE);
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        onActionSearch();
        return super.onCreateOptionsMenu(menu);
    }

    void loadDataFromServer(int status) {
        User user = MainActivity.getCurrentUser();
        //Thiet lap service
        BillService service = NetworkCommon.getRetrofit().create(BillService.class);

        Call<List<Order>> call = service.loadBill(user.getAccesstoken(), status);

        call.enqueue(new Callback<List<Order>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                list = response.body();
                if (response.isSuccessful() && list != null) {
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                } else
                    LoginActivity.showToast(getBaseContext(), "Lỗi tải dữ liệu!");
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(BillActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onActionSearch() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (list.size() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BillActivity.this)
                            .setTitle(R.string.app_name)
                            .setMessage("Danh sách trống")
                            .setPositiveButton("OK", null);
                    builder.show();
                }
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {
                List<Order> newList = list.stream().filter(e -> e.getName()
                        .toLowerCase().contains(newText.toLowerCase())
                        || (e.getId() + "").equals(newText)).collect(Collectors.toList());
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
