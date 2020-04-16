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
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.team.mobileworld.R;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.handle.APIhandler;
import com.team.mobileworld.core.handle.ItemTest;
import com.team.mobileworld.core.handle.ValidNetwork;
import com.team.mobileworld.core.object.ItemList;
import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.core.service.CartService;
import com.team.mobileworld.core.service.UserService;
import com.team.mobileworld.database.Database;
import com.team.mobileworld.fragment.CustomViewFragement;
import com.team.mobileworld.fragment.LoadFragement;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String APP_NAME = "Mobile Word";

    public static final String TAG = "tagmain";
    public static final String MESSAGE = "message";
    public static final String ERROR = "error";
    public static final String TOKEN = "token";
    public static final String MAIN_OPEN_ORDER = "MAIN_OPEN_ORDER";
    public static final String DATABASE_NAME = "MOBILE_APP";
    public static final String AUTO_LOGIN = "AUTO_LOGIN";

    public static final int STATUS_NOT_LOGIN = 0;
    public static final int STATUS_LOGIN_NOT_PHONE = 1;
    public static final int STATUS_NOT_ADDRESS = 2;
    public static final int STATUS_VALID= 3;

    public static final int RQ_OPEN_LOGIN = 55;
    public static final int CURRENT_VERSION = 1;
    private static final int OPEN_ORDER = 100;
    private static final int OPEN_PRODUCT = 101;
    private static final List<Order> Cart = new ArrayList<>();
    private static User user;
    private static Database db;

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
    ImageButton btngiohang;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, data + "");

        //Cap nhat so luong
        if (requestCode == ProductDetail.REPLACE_SIZE)
            txtSLuong.setText(APIhandler.getTotalAmount(getCart()) + "");


        //Xuat thong tin nguoi dung dang nhap
        if (requestCode == RQ_OPEN_LOGIN && resultCode == LoginActivity.ADD_INFO_USER) {
            user = (User) data.getExtras().getSerializable(LoginActivity.ITEM_USER);
            Log.d(TAG, "AMain: " + user.toString());

            txtfullname.setText(user.getFullname());
            txtemail.setText(user.getEmail());
            Picasso.get().load(user.getProfit()).fit().error(R.drawable.ic_profit).into(imgprofit);
        }

        if (requestCode == RQ_OPEN_LOGIN && resultCode == LoginActivity.REQUEST_LOAD_INFOUSER) {
            loadUserInfo();
            loadDataToCart();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Xu ly anh xa id
        elementMapping();

        //thiet lap co ban
        init();

        //Xu ly su kien
        btngiohang.setOnClickListener(e -> startActivityForResult(new Intent(MainActivity.this, CartActivity.class), ProductDetail.REPLACE_SIZE));

        btnNav.setOnClickListener(e -> drawer.openDrawer(GravityCompat.START));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fragcview = new CustomViewFragement(ItemTest.getItemList(), fragload);
            fragcview.onAttach(this);
            txtSLuong.setText(APIhandler.getTotalAmount(getCart()) + "");
        }

        int commit = fmanager.beginTransaction().replace(R.id.frag_main, fragcview).commit();

//        loadPageView("home");

        if (validUser() != 0) {
            loadUserInfo();
            loadDataToCart();
        } else {
            loadDataToCartFrom();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txtSLuong.setText(APIhandler.getTotalAmount(getCart()) + "");
            }
        }

        getInfoPackage();
    }

    private void loadDataToCartFrom() {
        List<Order> orders = db.getAllCart();
        getCart().addAll(orders);
    }

    /**
     * status:
     * 0: chưa được xác nhận
     * 1: Không có số điện thoại
     * 2: chưa có đại chỉ
     * 3: tài khoản hợp lệ
     *
     * @return
     */
    public static int validUser() {

        if (user == null) return 0;
        //User chưa được xác nhận
        if (user.getId() == 0) return 0;
        //Không có số điện thoại
        if (user.getPnumber() == null && user.getPnumber().length() == 0) return 1;
        //Không có địa chỉ
        if (user.getAddress() == null && user.getAddress().length() == 0) return 2;

        return 3;
    }

    //Khởi tạo
    private void init() {
        //Xu ly su kien chuyen doi
        fmanager = getSupportFragmentManager();

        //Danh sach cach loai mat hang
        goods = new ArrayList<>();
        fragload = new LoadFragement();

        db = new Database(this, CURRENT_VERSION);
    }

    //Ánh xạ phẩn tử
    private void elementMapping() {
        drawer = findViewById(R.id.drawer_layout);
        txtSLuong = findViewById(R.id.txtSoluong);
        btnNav = findViewById(R.id.btn_nav);
        btngiohang = findViewById(R.id.btGiohang);
        navview = findViewById(R.id.nav_view);
        searchView = findViewById(R.id.search_bar);

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
            fragcview.loadPage("home");
        else
            new AlertDialog.Builder(this).setTitle(APP_NAME)
                    .setMessage("Không có kết nối Internet")
                    .setPositiveButton("Thoát", (e, id) -> MainActivity.this.finish())
                    .show();

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
            case R.id.nav_home: {
                loadPageView("home");
            }
            break;
            case R.id.nav_phone: {
                loadPageView("mobile");
            }
            break;
            case R.id.nav_laptop: {
                loadPageView("laptop");
            }
            break;
            case R.id.nav_bill: {
                if (user == null || user.getId() == 0) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, RQ_OPEN_LOGIN);
                } else {
                    Intent intent = new Intent(MainActivity.this, BillActivity.class);
                    startActivity(intent);
                }
            }
            break;
            case R.id.nav_user: {
                if (user == null) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, RQ_OPEN_LOGIN);
                } else {
                    Intent intent = new Intent(this, PersonalInfoActivity.class);
                    startActivity(intent);
                }
            }
            break;
            case R.id.nav_logut: {
                LoginManager.getInstance().logOut();
                getCart().clear();
                startActivityForResult(new Intent(this, LoginActivity.class), RQ_OPEN_LOGIN);
            }
            break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadUserInfo() {
        AccessToken token = AccessToken.getCurrentAccessToken();

        if(token != null){
            //API hiển thị activity đăng nhập
            GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try {
                        user = LoginActivity.getInfoUserFromFacebook(object);
                        Log.d(TAG, response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this.getBaseContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Bundle params = new Bundle();
            params.putString("fields", "name, id, email,birthday, gender,friends");
            request.setParameters(params);
            request.executeAsync();
        }
        else if(validUser() == STATUS_NOT_LOGIN) {
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
    }

    private void loadDataToCart() {
        CartService service = NetworkCommon.getRetrofit().create(CartService.class);

        Call<List<Order>> call = service.loadCart(user.getId());

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                getCart().clear();
                getCart().addAll(response.body());
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
