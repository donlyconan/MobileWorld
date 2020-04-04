package com.letuan.mobileworld.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.letuan.mobileworld.R;
import com.letuan.mobileworld.adapter.PhoneAdapter;
import com.letuan.mobileworld.dialog.DialogCheck;
import com.letuan.mobileworld.mapper.ProductMapper;
import com.letuan.mobileworld.model.Product;
import com.letuan.mobileworld.ultil.CheckWifiValid;
import com.letuan.mobileworld.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhoneActivity extends AppCompatActivity {

    Toolbar toolbarPhone;
    ListView listView;
    PhoneAdapter phoneAdapter;
    List<Product> phoneList;
    int id = 0; // id category
    int page = 1;
    View footerView;
    boolean isLoading = false; // kiem tra trang thai loading processbar
    MyHandler myHandler;
    boolean limitData = false; // kiem tra het data chua

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        anhxa();
        if (CheckWifiValid.haveNetworkConnection(getApplicationContext())) {
            getCategoryId();
            actionToolBar();
            getData(page);
            loadMoreData();
        } else {
            DialogCheck dialogCheck = new DialogCheck(PhoneActivity.this);
            dialogCheck.setNotification(PhoneActivity.class, PhoneActivity.this);
            dialogCheck.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menucartorder:
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMoreData() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProductDetail.class);
                intent.putExtra("thongtin", phoneList.get(position));
                startActivity(intent);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstItem, int visibleItem, int totalItem) {
                if (firstItem + visibleItem == totalItem && totalItem != 0 && isLoading == false && limitData == false) { //kiem tra vi tri cuoi cung
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void getData(int _page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String urlPhone = Server.urlGetProductNewest + id + "?page=" + _page;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPhone,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && response.length() != 2) { // do cap ngoac vuong
                            listView.removeFooterView(footerView);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                ProductMapper.mapper(jsonArray, phoneList);
                                phoneAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            limitData = true;
                            listView.removeFooterView(footerView);
                            CheckWifiValid.ShowToast_Short(getApplicationContext(), "Hết dữ liệu rồi");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CheckWifiValid.ShowToast_Short(getApplicationContext(), "404 Not Found");
                    }
                });
        requestQueue.add(stringRequest);
    }

    private void actionToolBar() {
        setSupportActionBar(toolbarPhone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarPhone.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getCategoryId() {
        id = getIntent().getIntExtra("categoryid", -1);
        if (id == 1) {
            toolbarPhone.setTitle("Điện thoại");
        } else if (id == 2) {
            toolbarPhone.setTitle("Laptop");
        }
    }

    private void anhxa() {
        toolbarPhone = findViewById(R.id.toolbarphone);
        listView = findViewById(R.id.listitemphone);
        phoneList = new ArrayList<>();
        phoneAdapter = new PhoneAdapter(getApplicationContext(), phoneList);
        listView.setAdapter(phoneAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView = inflater.inflate(R.layout.process_bar, null);
        myHandler = new MyHandler();
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    listView.addFooterView(footerView);
                    break;
                case 1:
                    getData(++page);
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread {
        @Override
        public void run() {
            super.run();
            myHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = myHandler.obtainMessage(1);
            myHandler.sendMessage(message);
        }
    }
}
