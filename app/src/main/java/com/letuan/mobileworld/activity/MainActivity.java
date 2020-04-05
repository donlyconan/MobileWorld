package com.letuan.mobileworld.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.letuan.mobileworld.R;
import com.letuan.mobileworld.adapter.CategoryAdapter;
import com.letuan.mobileworld.adapter.ProductAdapter;
import com.letuan.mobileworld.dialog.DialogCheck;
import com.letuan.mobileworld.mapper.ProductMapper;
import com.letuan.mobileworld.model.Category;
import com.letuan.mobileworld.model.Order;
import com.letuan.mobileworld.model.Product;
import com.letuan.mobileworld.ultil.CheckWifiValid;
import com.letuan.mobileworld.ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {
    public static final String MAIN_OPEN_ORDER = "MAIN_OPEN_ORDER";
    public static final List<Order> OrderList = new ArrayList<>();

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    List<Category> categoryList;
    CategoryAdapter categoryAdapter;
    List<Product> productList;
    ProductAdapter productAdapter;
    TextView txtSL;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ProductDetail.REPLACE_SIZE)
            txtSL.setText(MainActivity.totalOrder());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewFlipper = findViewById(R.id.viewflipper);
        actionViewFlipper();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            productList = createItems();
        }

        productAdapter = new ProductAdapter(this, productList);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(productAdapter);

        txtSL = findViewById(R.id.txtSoluong);
        txtSL.setText(totalOrder());

        final ImageButton btGioHang =  findViewById(R.id.btGiohang);
        btGioHang.setOnClickListener(e->{
            Intent intent = new Intent(this, OrderActivity.class);
            startActivityForResult(intent, ProductDetail.REPLACE_SIZE);
            CustomIntent.customType(this, com.letuan.mobileworld.activity.Animation.LEFT_TO_RIGHT);
        });

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(e->{
            drawer.openDrawer(GravityCompat.START);
        });



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
    }

    public static String totalOrder(){
        int sum = 0;
        for (Order e : OrderList) {
            sum += e.getSize();
        }
        return sum + "";
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Product> createItems() {
        List<Product> list = new ArrayList<>();
        Product product = new Product(1,"IPhone XI Mode 1823 - new",
                28940000,
                "https://www.google.com/search?q=iphone+x&rlz=1C1CHBF_enVN883VN883&sxsrf=ALeKk00ksDR2-KmISRpOyVSy1VX4BffIeg:15857" +
                "35321945&source=lnms&tbm=isch&sa=X&ved=2ahUKEwjV2cm0_MboAhX763MBHQJsB9AQ_AUoAXoECAwQAw&biw=1366&bih=667#imgrc=Xw4AA8" +
                "s2Jf2wxM","Shop cần bán dòng Iphone X 64G Quốc Tế,đẹp 98-99%,zin,các chức năng đang hoàn hảo.\n" +
                "Máy đã qua sử dụng bên nước ngoài nhập về còn full chức năng.\n" +
                "\n" +
                "Tất cả các máy bán ra bảo hành 3 tháng phần cứng và hỗ trợ bảo hành 12 tháng.\n" +
                "\n" +
                "Khj bạn mua máy với giá trên shopee sẽ được tặng kèm :  \n" +
                "   _  Cóc cáp sạc tai nghe\n" +
                "   _  Miếng dán màn hình cường lực chống trầy xước và va đập.\n" +
                "   _ Ốp lưng dẻo bảo về điện thoại chống trầy xước\n" +
                "\n" +
                "Ship COD (trao máy mới nhận tiền)toàn quốc => Được kiểm tra hàng trước khj trả tiền nên đảm bảo yên tâm chất lượng uy tín.\n" +
                "\n" +
                "Liên hệ zalo: 0826644996 (hỗ trợ mọi thắc mắc hoặc phản hồi về sản phẩm của shop nhé.",1);
        Random rd = new Random();
        for (int i = 1; i <= 50; i++){
            product.setId(i);
            list.add(product);

            if(rd.nextInt() % 3 == 0)
            {
                Order order = new Order(product.getId(),product.getName(),(long)product.getPrice(),product.getImage(), rd.nextInt(9) + 1);
                OrderActivity.addProducts(order);
            }
        }
        return list;
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

    private void CatchOnItemListView() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                if (!CheckWifiValid.haveNetworkConnection(getApplicationContext())) {
                    DialogCheck dialogCheck = new DialogCheck(MainActivity.this);
                    dialogCheck.setNotification(MainActivity.class, MainActivity.this);
                    dialogCheck.show();
                }
                Intent intent = null;
                switch (i) {
                    case 0:
                        intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, ContactActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, InfoAppActivity.class);
                        startActivity(intent);
                        break;
                }
                if (i == 1 || i == 2) {
                    intent = new Intent(MainActivity.this, PhoneActivity.class);
                    intent.putExtra("categoryid", categoryList.get(i).getId());
                    startActivity(intent);
                }
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void getDataProductNewest() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlGetProductNewest,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null) {
                            ProductMapper.mapper(response, productList);
                            productAdapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    //lấy dữ liệu trên server đổ vào action bar
    private void getDataCategory() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlGetAllCategory,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null) {
                            int length = response.length();
                            for (int i = 0; i < length; i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    int id = jsonObject.getInt("categoryid");
                                    String name = jsonObject.getString("categoryname");
                                    String img = jsonObject.getString("categoryimage");
                                    categoryList.add((i + 1), new Category(id, name, img));
                                    categoryAdapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DialogCheck dialogCheck = new DialogCheck(MainActivity.this);
                        dialogCheck.setNotification(MainActivity.class, MainActivity.this);
                        dialogCheck.show();
                    }
                });
        categoryList.add(new Category(0, "Liên hệ", (String.valueOf(R.drawable.contact_us_icon))));
        categoryList.add(new Category(0, "Thông tin", String.valueOf(R.drawable.info_icon)));
        categoryAdapter.notifyDataSetChanged();
        requestQueue.add(jsonArrayRequest);
    }

    //bắt sự kiện chạy quảng cáo (slider)
    private void actionViewFlipper() {
        ArrayList<String> mangQuangCao = new ArrayList<>();
        mangQuangCao.add("https://cdn.tgdd.vn/Products/Images/42/217936/samsung-galaxy-s20-plus-400x460-fix-400x460.png");
        mangQuangCao.add("https://cdn.tgdd.vn/Products/Images/44/207680/asus-vivobook-x509f-i7-8565u-8gb-mx230-win10-ej13-5-2-1-2-1-600x600.jpg");
        mangQuangCao.add("https://cdn.tgdd.vn/Products/Images/42/210653/iphone-11-pro-max-256gb-black-400x460.png");
        mangQuangCao.add("https://cdn.tgdd.vn/Products/Images/44/198795/lenovo-ideapad-530s-14ikb-i7-8550u-8gb-256gb-win10-16-600x600.jpg");

        for (int i = 0; i < mangQuangCao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(mangQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            viewFlipper.addView(imageView);
        }

        //thời gian chuyển
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);

        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);

        //set animation
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);
    }

    // bắt sự kiện đóng mở action bar
    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void anhxa() {
//        toolbar = findViewById(R.id.toolbarmanhinhchinh);
//        viewFlipper = findViewById(R.id.viewflipper);
//        recyclerViewManHinhChinh = findViewById(R.id.recyclerview);
//        navigationView = findViewById(R.id.navigationview);
//        listViewManHinhChinh = findViewById(R.id.listviewmanhinhchinh);
//        drawerLayout = findViewById(R.id.drawerlayout);
//        categoryList = new ArrayList<>();
//        categoryList.add(0, new Category(0, "Trang chính", String.valueOf(R.drawable.home_page_icon)));
//        categoryAdapter = new CategoryAdapter(categoryList, getApplicationContext());
//        listViewManHinhChinh.setAdapter(categoryAdapter);
//
//        productList = new ArrayList<>();
//        productAdapter = new ProductAdapter(getApplicationContext(), productList);
//        recyclerViewManHinhChinh.setHasFixedSize(true);
//        recyclerViewManHinhChinh.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
//        recyclerViewManHinhChinh.setAdapter(productAdapter);
    }

}
