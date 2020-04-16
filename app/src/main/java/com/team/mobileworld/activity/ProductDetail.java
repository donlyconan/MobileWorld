package com.team.mobileworld.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.team.mobileworld.R;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.handle.APIhandler;
import com.team.mobileworld.core.handle.FacebookSharing;
import com.team.mobileworld.core.object.LaptopInfo;
import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.core.object.Product;
import com.team.mobileworld.core.object.SmartphoneInfo;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.core.service.CartService;
import com.team.mobileworld.core.service.ProductDetailService;
import com.team.mobileworld.database.Database;
import com.team.mobileworld.fragment.InfoLaptopFragment;
import com.team.mobileworld.fragment.InfoSmarphoneFragement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import maes.tech.intentanim.CustomIntent;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetail extends AppCompatActivity {
    public static final String PRODUCT_OPEN_ORDER = "PRODUCT_OPEN_ORDER";
    public static final int REPLACE_SIZE = 100;
    public static final String BUY_GOODS_NOW = "BUY_GOODS_NOW";

    Toolbar toolbarDetail;
    ImageView imgDetail;
    TextView txtNameDetail, txtPriceDetail, txtDescription, txtSl;
    Spinner spSluong, spPloai;
    Button btnOrder, btnBuy;
    ImageButton btnshare;
    FragmentManager fmanager;
    InfoSmarphoneFragement fphone;
    InfoLaptopFragment flatop;
    ImageButton btnGioHang;
    Product item;
    Database db;
    int amount = 0;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REPLACE_SIZE)
            txtSl.setText(APIhandler.getTotalAmount(MainActivity.getCart()) + "");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //Anh xa phan tu
        elementMapping();

        //Xu ly su kien
        btnGioHang.setOnClickListener(e -> onActionOpenCart());

        final List<Order> list = MainActivity.getCart();
        txtSl.setText(APIhandler.getTotalAmount(list) + "");

        //Lay thong tin san pham
        item = (Product) ProductDetail.this.getIntent().getSerializableExtra("thongtin");
        db = MainActivity.getDatabaseInstence();

        //Dem tong so san pham co the mua
        final Stream<Order> stream = list.stream().filter(e -> e.getId() == item.getId());
        amount = stream.mapToInt(Order::getAmount).sum();

        btnOrder.setOnClickListener(e -> addProductOnCart());

        toolbarDetail.setNavigationOnClickListener((e) -> finish());

        btnBuy.setOnClickListener(e -> buyOrderNow());

        fmanager = getSupportFragmentManager();

        btnshare.setOnClickListener(e -> FacebookSharing.shareLink(item.getName() + APIhandler.formatMoney((long) item.getPrice())
                , "http://google.com", ProductDetail.this));

        getInfomation();

        catchEventSpinner();

        getInfoTechnicalProduct();
    }

    private void buyOrderNow() {
        Order order = new Order(item.getId(), item.getName(), item.getPrice(), item.getImage()
                , Integer.valueOf(spSluong.getSelectedItem().toString()));
        //Start activity Order
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra("item", order);
        intent.setAction(BUY_GOODS_NOW);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addProductOnCart() {
        //Lay order san pham
        Order order = Order.convertToOrder(item);
        order.setAmount(Integer.valueOf(spSluong.getSelectedItem().toString()));

        //Kiểm tra sản phẩm có tồn tại trong giỏ hàng không
        int size = amount + order.getAmount();

        final boolean contain = amount == 0;

        //Kiem tra so luong dat hang voi so luong ton trong kho

        if (size > item.getSlmax()) {
            Toast.makeText(this, "Hàng trong kho không đủ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contain)
            new AlertDialog.Builder(ProductDetail.this).setTitle(MainActivity.APP_NAME)
                    .setMessage("Sản phầm này đã tồn tại trong giỏ hàng của bạn!").setNegativeButton("Thêm", (ex, id) -> addOrderOnCart(order, item, contain))
                    .setPositiveButton("Bỏ qua", null).show();

        else
            addOrderOnCart(order, item, contain);
    }

    //Tai thong tin san pham
    private void getInfoTechnicalProduct() {
        ProductDetailService service = NetworkCommon.getRetrofit()
                .create(ProductDetailService.class);

        //Goi lai service
        Call<ResponseBody> call = service.getTechnicalData(item.getId());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new Gson();
                try {
                    if (item.getCategoryid() == Product.LSP_PHONE) {
                        SmartphoneInfo info = gson.fromJson(response.body().string(), SmartphoneInfo.class);
                        fphone = new InfoSmarphoneFragement(info);

                        fmanager.beginTransaction()
                                .replace(R.id.frag_info_product, fphone).commit();
                    }

                    if (item.getCategoryid() == Product.LSP_LAPTOP) {
                        LaptopInfo info = gson.fromJson(response.body().string(), LaptopInfo.class);
                        flatop = new InfoLaptopFragment(info);
                        fmanager.beginTransaction().replace(R.id.frag_info_product, flatop)
                                .commit();
                    }
                } catch (IOException e) {
                    Toast.makeText(ProductDetail.this.getBaseContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProductDetail.this.getBaseContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }

    //them hang vao gio
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addOrderOnCart(Order order, Product item, boolean contain) {
        /**
         * Nếu đã đăng nhập thì sẽ giử tất cả dữ liệu lên server
         * Nếu chửa đăng nhập tất cả dữ liệu sẽ được lưu vào local database
         */
        Log.d("debug", "User=" + MainActivity.getUser() + "");
        if (MainActivity.validUser() == MainActivity.STATUS_NOT_LOGIN)
            pushOnDatabaseLocal(contain, order);
        else
            pushOrderOnCart(contain, order);

    }

    /**
     * Đẩy tất cả dữ liệu vào local database
     *
     * @param contain
     * @param order
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private long pushOnDatabaseLocal(boolean contain, Order order) {
        long index = 0;

        if (contain)
            index = db.updateCart(order.getId(), order);
        else
            index = db.addOrder(order);

        if (index > 0) {
            APIhandler.add(MainActivity.getCart(), order);
            txtSl.setText(APIhandler.getTotalAmount(MainActivity.getCart()) + "");
            Toast.makeText(this.getBaseContext(), "Đã thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        }
        return index;
    }

    /**
     * Đẩy tất cả dữ liệu nên server
     *
     * @param contain
     * @param order
     */
    public void pushOrderOnCart(boolean contain, Order order) {
        CartService service = NetworkCommon.getRetrofit().create(CartService.class);

        User user = MainActivity.getUser();
        Log.d("debug", "User=" + user);
        Call<ResponseBody> call = service.addOrder(user.getId(), order);

        //thực hiện xử lý thông tin nhận về
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JsonObject json = APIhandler.convertToJSon(response.body().string());
                    boolean success = json.has("message") && json.get("message") != null;

                    if (success) {
                        /**
                         *    Neu gio hang da chua san phan them so luong san pham vao vi tri do
                         *    Neu gio hang chua chua san pham thi se them san pham vao gio hang
                         */
                        if (contain)
                            APIhandler.add(MainActivity.getCart(), order);
                        else
                            MainActivity.getCart().add(order);

                        txtSl.setText(APIhandler.getTotalAmount(MainActivity.getCart()) + "");
                        Toast.makeText(ProductDetail.this.getBaseContext(), "Đã thêm sản phầm vào giỏ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductDetail.this.getBaseContext(), "Lỗi: " + json.get("error"), Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProductDetail.this.getBaseContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onActionOpenCart() {
        Intent intent = new Intent(this, CartActivity.class);
        intent.setAction(PRODUCT_OPEN_ORDER);
        startActivityForResult(intent, REPLACE_SIZE);
    }

    private void elementMapping() {
        toolbarDetail = findViewById(R.id.toolbardetail);
        imgDetail = findViewById(R.id.imgdetail);
        txtNameDetail = findViewById(R.id.txtnamedetail);
        txtPriceDetail = findViewById(R.id.txtpricedetail);
        txtDescription = findViewById(R.id.txtdescriptiondetail);
        spSluong = findViewById(R.id.spSluong);
        spPloai = findViewById(R.id.spPloai);
        btnOrder = findViewById(R.id.btnorder);
        btnBuy = findViewById(R.id.btnbuy);
        txtSl = findViewById(R.id.txtSoluong);
        btnshare = findViewById(R.id.btn_share);
        btnGioHang = findViewById(R.id.btGiohang);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menucartorder:
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void catchEventSpinner() {
        List<Integer> list = new ArrayList<>();
        int maxsize = item.getSlmax() - amount;
        maxsize = maxsize <= 50 ? maxsize : 50;

        for (int i = 1; i <= maxsize; i++) list.add(i);

        ArrayAdapter<Integer> arrayAdapter =
                new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spSluong.setAdapter(arrayAdapter);

        String[] colors = new String[]{"Vàng", "Xanh", "Đỏ", "Cam"};
        ArrayAdapter<String> adapterColor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colors);
        spPloai.setAdapter(adapterColor);
    }

    private void getInfomation() {
        TextView txttitle = findViewById(R.id.txtTitle);
        txttitle.setText(item.getName());

        txtNameDetail.setText(item.getName());
        txtPriceDetail.setText("Giá: " + APIhandler.formatMoney((long) item.getPrice()));
        txtDescription.setText(item.getDescription());

        Picasso.get().load(item.getImage()).placeholder(R.drawable.no_image_icon)
                .error(R.drawable.error)
                .fit().into(imgDetail);
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
