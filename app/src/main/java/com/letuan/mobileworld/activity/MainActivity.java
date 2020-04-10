package com.letuan.mobileworld.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.letuan.mobileworld.R;
import com.letuan.mobileworld.adapter.CategoryAdapter;
import com.letuan.mobileworld.fragment.CustomViewFragement;
import com.letuan.mobileworld.fragment.LoadFragement;
import com.letuan.mobileworld.model.GoodsReview;
import com.letuan.mobileworld.model.Order;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String MAIN_OPEN_ORDER = "MAIN_OPEN_ORDER";
    public static final List<Order> OrderList = new ArrayList<>();

    ImageButton btnNav;
    DrawerLayout drawer;
    CategoryAdapter categoryAdapter;
    TextView txtSLuong;
    FragmentManager fmanager;
    CustomViewFragement fragcview;
    LoadFragement fragload;
    List<GoodsReview> goods;
    SearchView searchView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Cap nhat so luong
        if (requestCode == ProductDetail.REPLACE_SIZE)
            txtSLuong.setText(MainActivity.totalOrder());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Xu ly anh xa id
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        txtSLuong = findViewById(R.id.txtSoluong);
        txtSLuong.setText(totalOrder());
        btnNav = findViewById(R.id.btn_nav);
        final ImageButton btGioHang = findViewById(R.id.btGiohang);
        searchView = findViewById(R.id.search_bar);

        //Xu ly su kien
        btGioHang.setOnClickListener(e -> {
            Intent intent = new Intent(this, OrderActivity.class);
            startActivityForResult(intent, ProductDetail.REPLACE_SIZE);
            CustomIntent.customType(this, Animation.LEFT_TO_RIGHT);
        });

        btnNav.setOnClickListener(e -> {
            drawer.openDrawer(GravityCompat.START);
        });

        NavigationView navview = findViewById(R.id.nav_view);
        navview.setNavigationItemSelectedListener(this);

        //Xu ly su kien chuyen doi
        fmanager = getSupportFragmentManager();
        //Danh sach cach loai mat hang
        goods = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fragload = new LoadFragement();
            fragcview = new CustomViewFragement(goods);

            fragcview.onAttach(this);
            int commit = fmanager.beginTransaction().replace(R.id.frag_main, fragcview).commit();
        }
        //        productAdapter = new ProductAdapter(this, productList);
//        recyclerView = findViewById(R.id.recyclerview);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
//        recyclerView.setAdapter(productAdapter);
//        if (CheckWifiValid.haveNetworkConnection(getApplicationContext())) {
//            actionBar();
//            actionViewFlipper();
//            getDataCategory();
//            getDataProductNewest();
//            CatchOnItemListView();
//        } else {
//            DialogCheck dialogCheck = new DialogCheck(MainActivity.this);
//            dialogCheck.setNotification(MainActivity.class, MainActivity.this);
//            dialogCheck.show();
//        }

        txtSLuong.setText(totalOrder() + "");
    }


    //Tinh tong san pham trong gio hang
    public static String totalOrder() {
        int sum = 0;
        for (Order e : OrderList) {
            sum += e.getSize();
        }
        return sum + "";
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

//    private void CatchOnItemListView() {
//        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
//                if (!CheckWifiValid.haveNetworkConnection(getApplicationContext())) {
//                    DialogCheck dialogCheck = new DialogCheck(MainActivity.this);
//                    dialogCheck.setNotification(MainActivity.class, MainActivity.this);
//                    dialogCheck.show();
//                }
//                Intent intent = null;
//                switch (i) {
//                    case 0:
//                        intent = new Intent(MainActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                        break;
//                    case 3:
//                        intent = new Intent(MainActivity.this, ContactActivity.class);
//                        startActivity(intent);
//                        break;
//                    case 4:
//                        intent = new Intent(MainActivity.this, PersonalInfoActivity.class);
//                        startActivity(intent);
//                        break;
//                }
//                if (i == 1 || i == 2) {
//                    intent = new Intent(MainActivity.this, PhoneActivity.class);
//                    intent.putExtra("categoryid", categoryList.get(i).getId());
//                    startActivity(intent);
//                }
//                drawer.closeDrawer(GravityCompat.START);
//            }
//        });
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        RecyclerView.Adapter adapter = fragcview.getCadapter();


        switch (id) {
            case R.id.nav_home: {

                adapter.notifyDataSetChanged();
            }
            break;
            case R.id.nav_phone: {

            }
            break;
            case R.id.nav_laptop: {

            }
            break;
            case R.id.nav_bill:{
                Intent intent = new Intent(MainActivity.this, BillActivity.class);
                startActivity(intent);
                CustomIntent.customType(this, com.letuan.mobileworld.activity.Animation.LEFT_TO_RIGHT);
            }
            break;
            case R.id.nav_user: {
                Intent intent = new Intent(MainActivity.this, PersonalInfoActivity.class);
                startActivity(intent);
                CustomIntent.customType(this, com.letuan.mobileworld.activity.Animation.LEFT_TO_RIGHT);
            }
            break;
            case R.id.nav_logut: {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                onBackPressed();
                CustomIntent.customType(this, com.letuan.mobileworld.activity.Animation.LEFT_TO_RIGHT);
            }
            break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    private void getDataProductNewest() {
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlGetProductNewest,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        if (response != null) {
//                            ProductMapper.mapper(response, productList);
//                            productAdapter.notifyDataSetChanged();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//        requestQueue.add(jsonArrayRequest);
//    }
//
//    //lấy dữ liệu trên server đổ vào action bar
//    private void getDataCategory() {
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlGetAllCategory,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        if (response != null) {
//                            int length = response.length();
//                            for (int i = 0; i < length; i++) {
//                                try {
//                                    JSONObject jsonObject = response.getJSONObject(i);
//                                    int id = jsonObject.getInt("categoryid");
//                                    String name = jsonObject.getString("categoryname");
//                                    String img = jsonObject.getString("categoryimage");
//                                    categoryList.add((i + 1), new Category(id, name, img));
//                                    categoryAdapter.notifyDataSetChanged();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        DialogCheck dialogCheck = new DialogCheck(MainActivity.this);
//                        dialogCheck.setNotification(MainActivity.class, MainActivity.this);
//                        dialogCheck.show();
//                    }
//                });
//        categoryList.add(new Category(0, "Liên hệ", (String.valueOf(R.drawable.contact_us_icon))));
//        categoryList.add(new Category(0, "Thông tin", String.valueOf(R.drawable.info_icon)));
//        categoryAdapter.notifyDataSetChanged();
//        requestQueue.add(jsonArrayRequest);
//    }
//
//    //bắt sự kiện chạy quảng cáo (slider)

    // bắt sự kiện đóng mở action bar

}
