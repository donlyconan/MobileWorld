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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

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
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String MESSAGE = "message";
    public static final String ERROR = "error";
    public static final String TAG = "user debug";

    public static final String MAIN_OPEN_ORDER = "MAIN_OPEN_ORDER";
    public static final String DATABASE_NAME = "MOBILE_APP";
    public static final String ACTION_AUTO_LOGIN = "Auto_Login";

    public static final int RQ_OPEN_LOGIN = 55;
    public static final int CURRENT_VERSION = 1;
    public static final int OPEN_ORDER = 100;

    public static final int REQUEST_LOCATION = 1000;
    private static final int REQUEST_UPDATE = 110;
    public static boolean NewLogin = false;

    //Danh tinh nguoi su dung
    private static User CurrentUser = new User(); //Chua dinh danh user
    //Gio hang
    private static final List<Order> Basket = new ArrayList<>(50);
    //Co so du lieu hien tai
    private static Database db;

    ImageButton btnOpenNav;
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
        if (requestCode == RQ_OPEN_LOGIN && resultCode == LoginActivity.ADD_INFO_USER && CurrentUser == null) {
            loadUserInfo();
        }

        //Tai trang san pham
        if (!fragload.getProgressBar().isIndeterminate() || NewLogin)
            loadPageView("home");

        //Tai thong tin nguoi su dung
        if (CurrentUser.isLogin() && NewLogin) {
            NewLogin = false;
            txtfullname.setText(CurrentUser.getFullname());
            txtemail.setText(CurrentUser.getEmail());
            Handler.loadImage(this, CurrentUser.getAvatar(), imgprofit);
        }

        txtSLuong.setText(Handler.getTotalAmount(getBasket()) + "");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Xu ly anh xa id
        mapping();

        //thiet lap co ban
        _init_();

        //Xu ly su kien
        btngiohang.setOnClickListener(e -> startActivityForResult(new Intent(getApplicationContext()
                , BasketActivity.class), ProductDetailActivity.REQUEST_SIZE_AMOUNT));

        //Su kien search
        textsearch.setOnClickListener(e -> openActivitySearch());

        //xu ly su kien mo drawer
        btnOpenNav.setOnClickListener(e -> drawer.openDrawer(GravityCompat.START));
    }

    private void openActivitySearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, 10);
        CustomIntent.customType(this, Animation.FADE_IN_TO_OUT);
    }

    public void showToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loadLocalData() {
        List<Order> orders = db.getAllCart();
        getBasket().addAll(orders);

        Database.print("Load Kho hang: " + Basket.size() + " san pham");
    }

    //Khởi tạo
    private void _init_() {
        //Xu ly su kien chuyen doi
        fmanager = getSupportFragmentManager();
        //Danh sach cach loai mat hang
        goods = new ArrayList<>(3);

        fragload = new LoadFragement();
        db = new Database(this, CURRENT_VERSION);

        //Dang ky su dung toa do GPS
        LocationInfo.register(this);
        //Lay toa do hien tai
        LocationInfo.getCurrentLocation();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            listpage = new ArrayList<>(3);
            fragcview = new CustomViewFragement(listpage, fragload);
            fragcview.onAttach(this);
        }

        //thay doi trang hien thi
        int commit = fmanager.beginTransaction().replace(R.id.frag_main, fragcview).commit();

        //load view san pham
        loadPageView("home");

        //Load data tu local database
        loadLocalData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtSLuong.setText(Handler.getTotalAmount(Basket) + "");
        }

        Database.print("Tổng số lượng trong giỏ: " + Handler.totalSize(Basket));
        getInfoPackage();
    }

    //Ánh xạ phẩn tử
    private void mapping() {
        drawer = findViewById(R.id.drawer_layout);
        txtSLuong = findViewById(R.id.txtSoluong);
        btnOpenNav = findViewById(R.id.btn_nav);
        btngiohang = findViewById(R.id.btGiohang);
        navview = findViewById(R.id.nav_view);
        textsearch = findViewById(R.id.txtsearch);

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
                Intent intent = new Intent(getApplicationContext(), BasketActivity.class);
                startActivityForResult(intent, OPEN_ORDER);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean checkPermissionUser(int id) {
        //mo cac lua chon neu da dang nhap
        if (CurrentUser.isLogin())
            return true;

        //quyen truy cap
        List permissions = Arrays.asList(R.id.item_home, R.id.item_laptop, R.id.item_phone, R.id.item_logut);

        //neu chua dang nhapf thi chi co the su dung cach quyen truy cap tren
        if (permissions.contains(id))
            return true;

        return false;
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

        //kiem tra quyen su dung tinh nang
        if (!checkPermissionUser(id)) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }

        switch (id) {
            case R.id.item_home:
                loadPageView("home");
                break;
            case R.id.item_phone:
                loadPageView("mobile");
                break;
            case R.id.item_laptop:
                loadPageView("laptop");
                break;
            case R.id.nav_bill:
                startActivity(new Intent(this, BillActivity.class));
                break;
            case R.id.item_user:
                startActivityForResult(new Intent(this, PersonalInfoActivity.class), REQUEST_UPDATE);
                break;

            case R.id.item_logut: {
                LoginManager.getInstance().logOut();
                startActivityForResult(new Intent(this, LoginActivity.class), RQ_OPEN_LOGIN);
            }
            break;
            case R.id.item_notification: {
                startActivity(new Intent(this, NotificationActivity.class));
                break;
            }
            case R.id.item_feedback:
                startActivity(new Intent(this, FeedbackActivity.class));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void loadUserInfo() {

        UserService service = NetworkCommon.getRetrofit().create(UserService.class);

        //Thiet lap service lay thong tin
        Call<User> call = service.getPersonalInfo(CurrentUser.getAccesstoken());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                MainActivity.setCurrentUser(CurrentUser);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this.getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public AlertDialog.Builder noNetworkConnection() {
        return new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setIcon(R.drawable.ic_error)
                .setMessage("Không có kết nối internet.")
                .setPositiveButton("Thoát", (e,id)->finish());
    }

    private void loadServerData() {
        BasketService service = NetworkCommon.getRetrofit().create(BasketService.class);

        Call<List<Order>> call = service.loadCart(CurrentUser.getAccesstoken());

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                db.print("Json get Basket:" + response.body());
                if (response.isSuccessful()) {
                    getBasket().clear();
                    getBasket().addAll(response.body());
                    txtSLuong.setText(Handler.totalSize(Basket) + "");
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

    public static User getCurrentUser() {
        return CurrentUser;
    }

    public static void setCurrentUser(User user) {
        MainActivity.CurrentUser = user;
    }

    public static List<Order> getBasket() {
        return Basket;
    }
}
