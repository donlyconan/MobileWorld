package com.team.mobileworld.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.team.mobileworld.R;
import com.team.mobileworld.core.handle.APIhandler;
import com.team.mobileworld.core.object.ItemList;
import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.fragment.CustomViewFragement;
import com.team.mobileworld.fragment.LoadFragement;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "tagmain";
    public static final int RQ_OPEN_LOGIN = 55;
    public static final String MAIN_OPEN_ORDER = "MAIN_OPEN_ORDER";
    public static final List<Order> OrderList = new ArrayList<>();
    private static final int OPEN_ORDER = 100;
    private static final int OPEN_PRODUCT = 101;
    static User user;

    ImageButton btnNav;
    DrawerLayout drawer;
    TextView txtSLuong, txtfullname, txtemail;
    FragmentManager fmanager;
    CustomViewFragement fragcview;
    LoadFragement fragload;
    List<ItemList> goods;
    SearchView searchView;
    CircleImageView imgprofit;
    NavigationView navview;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,  data + "");

        //Cap nhat so luong
        if (requestCode == ProductDetail.REPLACE_SIZE)
            txtSLuong.setText(APIhandler.getTotalAmount(OrderList) + "");


        //Xuat thong tin nguoi dung dang nhap
        if (requestCode == RQ_OPEN_LOGIN && resultCode == LoginActivity.REQUEST_FBINFOUSER) {
            user = (User) data.getExtras().getSerializable(LoginActivity.ITEM_USER);
            Log.d(TAG, "AMain: " + user.toString());

            txtfullname.setText(user.getFullname());
            txtemail.setText(user.getEmail());
            Picasso.get().load(user.getProfit()).fit().error(R.drawable.ic_profit).into(imgprofit);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Xu ly anh xa id
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        txtSLuong = findViewById(R.id.txtSoluong);
        btnNav = findViewById(R.id.btn_nav);
        final ImageButton btGioHang = findViewById(R.id.btGiohang);
        navview = findViewById(R.id.nav_view);
        searchView = findViewById(R.id.search_bar);

        final View header = navview.getHeaderView(0);
        txtfullname = header.findViewById(R.id.txt_fullname);
        txtemail = header.findViewById(R.id.txt_email);
        imgprofit = header.findViewById(R.id.img_profit);


        if (getIntent().getAction() == Intent.ACTION_INSERT) {
            User user = (User) getIntent().getExtras().getSerializable(LoginActivity.ITEM_USER);
            Log.d(TAG, "Main:" + user.toString());
        }


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

        fragload = new LoadFragement();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fragcview = new CustomViewFragement(goods);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fragcview.onAttach(this);
        }
        int commit = fmanager.beginTransaction().replace(R.id.frag_main, fragcview).commit();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtSLuong.setText(APIhandler.getTotalAmount(OrderList) + "");
        }

        getInfoPackage();
    }

    protected void getUserInfo() {
        User user = (User) getIntent().getExtras().getSerializable(LoginActivity.ITEM_USER);
        if (user != null) {

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
                startActivityForResult(intent, OPEN_ORDER);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sự kiện Nav-draw
     *
     * @param item
     * @return
     */
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
            case R.id.nav_bill: {
                Intent intent = new Intent(MainActivity.this, BillActivity.class);
                startActivity(intent);
                CustomIntent.customType(this, com.team.mobileworld.activity.Animation.LEFT_TO_RIGHT);
            }
            break;
            case R.id.nav_user: {
                if(user == null )
                {

                } else {
                    Intent intent = new Intent(MainActivity.this, PersonalInfoActivity.class);
                    startActivity(intent);
                    CustomIntent.customType(this, com.team.mobileworld.activity.Animation.LEFT_TO_RIGHT);
                }
            }
            break;
            case R.id.nav_logut: {
                LoginManager.getInstance().logOut();
                OrderList.clear();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, RQ_OPEN_LOGIN);
                CustomIntent.customType(this, com.team.mobileworld.activity.Animation.LEFT_TO_RIGHT);
            }
            break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //get key hashs
    private void getInfoPackage() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.team.mobileworld", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("keyhash", something);
            }
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

}
