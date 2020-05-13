package com.team.mobileworld.activity;

import android.Manifest;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.team.mobileworld.R;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.database.Database;
import com.team.mobileworld.core.handle.LocationInfo;
import com.team.mobileworld.core.handle.Handler;
import com.team.mobileworld.core.handle.ValidNetwork;
import com.team.mobileworld.core.object.CatalogItem;
import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.core.service.BasketService;
import com.team.mobileworld.core.service.UserService;
import com.team.mobileworld.fragment.CustomViewFragement;
import com.team.mobileworld.fragment.LoadFragement;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.team.mobileworld.core.handle.LocationInfo.getCurrentLocation;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String MESSAGE = "message";
    public static final String ERROR = "error";
    public static final String TOKEN = "token";
    public static final String TAG = "user debug";

    public static final String MAIN_OPEN_ORDER = "MAIN_OPEN_ORDER";
    public static final String DATABASE_NAME = "MOBILE_APP";
    public static final String ACTION_AUTO_LOGIN = "Auto Login";

    public static final int RQ_OPEN_LOGIN = 55;
    public static final int CURRENT_VERSION = 1;
    public static final int OPEN_ORDER = 100;
    public static final int OPEN_PRODUCT = 101;

    private static final List<Order> Cart = new ArrayList<>(50);
    private static final long WAIT_TIME = 5000;
    public static final int REQUEST_LOCATION = 1000;
    private static User user = new User(); //Chua dinh danh user
    private static Database db;
    public static boolean NewLogin = false;

    ImageButton btnNav;
    DrawerLayout drawer;
    TextView txtSLuong, txtfullname, txtemail;
    FragmentManager fmanager;
    CustomViewFragement fragcview;
    LoadFragement fragload;
    List<CatalogItem> goods;
    TextView textsearch;
    CircleImageView imgprofit;
    NavigationView navview;
    ImageButton btngiohang;
    List<CatalogItem> listpage;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, data + "");

        //Xuat thong tin nguoi dung dang nhap
        if (requestCode == RQ_OPEN_LOGIN && resultCode == LoginActivity.ADD_INFO_USER) {
            if (user == null) {
                loadUserInfo();
            }
        }

        if (user.isLogin() && NewLogin) {
            NewLogin = false;
            txtfullname.setText(user.getFullname());
            txtemail.setText(user.getEmail());
            Handler.loadImage(this, user.getProfit(), imgprofit);
            txtSLuong.setText(Handler.totalSize(Cart) + "");
        }

        txtSLuong.setText(Handler.getTotalAmount(getCart()) + "");
        if (!fragload.getProgressBar().isIndeterminate())
            loadPageView("home");
        LocationInfo.register(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Xu ly anh xa id
        mapping();

        //thiet lap co ban
        init();

        //Xu ly su kien
        btngiohang.setOnClickListener(e -> startActivityForResult(new Intent(getApplicationContext()
                , CartActivity.class), ProductDetail.REQUEST_SIZE_AMOUNT));

        textsearch.setOnClickListener(e -> openActivitySearch());

        btnNav.setOnClickListener(e -> drawer.openDrawer(GravityCompat.START));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            listpage = new ArrayList<>();
            fragcview = new CustomViewFragement(listpage, fragload);
            fragcview.onAttach(this);
            txtSLuong.setText(Handler.getTotalAmount(getCart()) + "");
        }

        int commit = fmanager.beginTransaction().replace(R.id.frag_main, fragcview).commit();

        loadPageView("home");

        if (user.isLogin()) {
            loadUserInfo();
            loadServerData();
        } else {
            loadLocalData();
        }

        txtSLuong.setText(Handler.totalSize(Cart) + "");
        Database.print("Tổng số lượng trong giỏ: " + Handler.totalSize(Cart));
        getInfoPackage();
    }

    private void openActivitySearch() {
        Intent intent = new Intent(this, ResultSearchActivity.class);
        startActivityForResult(intent, 10);
        CustomIntent.customType(this, Animation.FADE_IN_TO_OUT);
    }

    public void showToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loadLocalData() {
        List<Order> orders = db.getAllCart();
        getCart().addAll(orders);

        Database.print("Load Kho hang: " + Cart.size() + " san pham");
    }

    //Khởi tạo
    private void init() {
        //Xu ly su kien chuyen doi
        fmanager = getSupportFragmentManager();
        //Danh sach cach loai mat hang
        goods = new ArrayList<>();
        fragload = new LoadFragement();
        db = new Database(this, CURRENT_VERSION);
        LocationInfo.register(this);
        LocationInfo.getCurrentLocation();
    }

    //Ánh xạ phẩn tử
    private void mapping() {
        drawer = findViewById(R.id.drawer_layout);
        txtSLuong = findViewById(R.id.txtSoluong);
        btnNav = findViewById(R.id.btn_nav);
        btngiohang = findViewById(R.id.btGiohang);
        navview = findViewById(R.id.nav_view);
        textsearch = findViewById(R.id.txtsearch);

        String[] permissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.ACCESS_COARSE_LOCATION
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 1000);
        }
        final View header = navview.getHeaderView(0);
        txtfullname = header.findViewById(R.id.txt_fullname);
        txtemail = header.findViewById(R.id.txt_email);
        imgprofit = header.findViewById(R.id.img_profit);

        NavigationView navview = findViewById(R.id.nav_view);
        navview.setNavigationItemSelectedListener(this);
    }

    //Tải dữ liệu sản phẩm
    public void loadPageView(String namepage) {
        if (ValidNetwork.hasNetwork(getApplicationContext()))
            fragcview.loadPage(namepage);
        else
            noNetworkConnection().show();
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
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
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
            case R.id.nav_home:
                loadPageView("home");
                break;
            case R.id.nav_phone:
                loadPageView("mobile");
                break;
            case R.id.nav_laptop:
                loadPageView("laptop");
                break;
            case R.id.nav_bill: {
                if (user.isLogin()) {
                    Intent intent = new Intent(MainActivity.this, BillActivity.class);
                    startActivity(intent);
                } else
                    onStartLogin();
            }
            break;
            case R.id.nav_user: {
                if (user.isLogin()) {
                    Intent intent = new Intent(this, PersonalInfoActivity.class);
                    startActivity(intent);
                } else
                    onStartLogin();
            }
            break;
            case R.id.nav_logut: {
                LoginManager.getInstance().logOut();
                startActivityForResult(new Intent(this, LoginActivity.class), RQ_OPEN_LOGIN);
            }
            break;
            case R.id.nav_noti: {
                if (user.isLogin())
                    startActivity(new Intent(this, ActivityNotification.class));
                else
                    startActivityForResult(new Intent(this, LoginActivity.class), RQ_OPEN_LOGIN);
            }
            break;
            case R.id.nav_feedback:
                if (user.isLogin())
                    startActivity(new Intent(this, FeedbackActivity.class));
                else
                    startActivityForResult(new Intent(this, LoginActivity.class), RQ_OPEN_LOGIN);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onStartLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, RQ_OPEN_LOGIN);
    }

    private void loadUserInfo() {

        UserService service = NetworkCommon.getRetrofit().create(UserService.class);

        //Thiet lap service lay thong tin
        Call<User> call = service.getPersonalInfo(user.getId());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                MainActivity.setUser(user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this.getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public AlertDialog.Builder noNetworkConnection() {
        return new AlertDialog.Builder(this).setTitle(R.string.app_name)
                .setMessage("Không có kết nối Internet")
                .setPositiveButton("Thoát", (e, id) -> MainActivity.this.onBackPressed());
    }

    private void loadServerData() {
        BasketService service = NetworkCommon.getRetrofit().create(BasketService.class);

        Call<List<Order>> call = service.loadCart(user.getId());

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                db.print("Json get Cart:" + response.body());
                if (response.isSuccessful()) {
                    getCart().clear();
                    getCart().addAll(response.body());
                    txtSLuong.setText(Handler.totalSize(Cart) + "");
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(MainActivity.this.getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        CustomIntent.customType(MainActivity.this, Animation.LEFT_TO_RIGHT);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        CustomIntent.customType(MainActivity.this, Animation.LEFT_TO_RIGHT);
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, Animation.RIGHT_TO_LEFT);
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

    public static Database getDatabaseInstence() {
        return db;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        MainActivity.user = user;
    }

    public static List<Order> getCart() {
        return Cart;
    }
}
