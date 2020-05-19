package com.team.mobileworld.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.team.mobileworld.R;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.handle.Handler;
import com.team.mobileworld.core.object.LaptopInfo;
import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.core.object.PhoneInfo;
import com.team.mobileworld.core.object.Product;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.core.service.BasketService;
import com.team.mobileworld.core.service.ProductDetailService;
import com.team.mobileworld.core.database.Database;
import com.team.mobileworld.fragment.LaptopInfoFragment;
import com.team.mobileworld.fragment.PhoneInfoFragement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import maes.tech.intentanim.CustomIntent;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    public static final String PRODUCT_OPEN_ORDER = "open_order";
    public static final String BUY_GOODS_NOW = "open_activity_buy_product";
    public static final String ITEM = "item_detail";
    private static final String ACTION_BUY_PRODUCT = "action_buy_product";

    public static final int REQUEST_SIZE_AMOUNT = 0x001;
    public static final int REQUEST_LOGIN_ORDER = 0x002;

    private Product item;
    private Database db;
    private int amount = 0, index = -1;
    private List<Order> cart;

    Toolbar toolbarDetail;
    ImageView imgDetail;
    TextView txtNameDetail, txtPriceDetail, txtDescription, txtSl;
    Spinner spSluong, spPloai;
    Button btnOrder, btnBuy;
    ImageButton btnshare;
    FragmentManager fmanager;
    PhoneInfoFragement fphone;
    LaptopInfoFragment flatop;
    ImageButton btnGioHang;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOGIN_ORDER && resultCode == RESULT_OK) {
            buyProductNow();
        }

        txtSl.setText(Handler.getTotalAmount(MainActivity.getBasket()) + "");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //Anh xa phan tu
        mapping();

        init();

        //Xu ly su kien
        btnGioHang.setOnClickListener(e -> onActionOpenCart());

        btnOrder.setOnClickListener(e -> addProductOnCart());

        toolbarDetail.setNavigationOnClickListener((e) -> finish());

        btnBuy.setOnClickListener(e -> buyProductNow());

        fmanager = getSupportFragmentManager();

        btnshare.setOnClickListener(e -> createShareLink(
                "Sản phẩm: " + item.getName()
                        + "\nGiá: " + Handler.formatMoney((long) item.getPrice())
                        + "\nChi tiết: " + item.getDescription(), item.getImage()));

        getInfomation();

        catchEventSpinner();

        getInfoTechnicalProduct();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        cart = MainActivity.getBasket();
        txtSl.setText(Handler.getTotalAmount(cart) + "");

        //Tim kiem san pham da ton tai trong gio hang
        item = (Product) getIntent().getExtras().get(ITEM);

        //Lay thong tin san pham
        db = MainActivity.getDatabaseInstence();
    }

    private void buyProductNow() {
        index = Handler.findById(item.getId(), cart);
        if(index > -1) {
            LoginActivity.showToast(getBaseContext(), "Đơn hàng đã tồn tại trong giỏ hàng!");
        } else if (MainActivity.getCurrentUser().isLogin()) {
            Order order = new Order(item.getId(), item.getName(), item.getPrice(), item.getImage()
                    , Integer.valueOf(spSluong.getSelectedItem().toString()));
            //Start activity Order
            Intent intent = new Intent(this, OrderActivity.class);
            intent.putExtra("item", order);
            intent.setAction(BUY_GOODS_NOW);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setAction(ACTION_BUY_PRODUCT);
            startActivityForResult(intent, REQUEST_LOGIN_ORDER);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addProductOnCart() {
        //Dem tong so san pham co the mua
        final Stream<Order> stream = cart.stream().filter(e -> e.getId() == item.getId());
        amount = stream.mapToInt(Order::getAmount).sum();

        index = Handler.findById(item.getId(), cart);

        if (index != -1)
            amount = cart.get(index).getAmount();

        Database.print("Vi tri ton tai: " + index);

        //Lay order san pham
        Order order = Order.convertToOrder(item);
        order.setAmount(Integer.valueOf(spSluong.getSelectedItem().toString()));

        //Kiểm tra sản phẩm có tồn tại trong giỏ hàng không
        int size = amount + order.getAmount();

        final boolean contain = index != -1;

        //Kiem tra so luong dat hang voi so luong ton trong kho
        if (size > item.getSlmax()) {
            Toast.makeText(this, "Hàng trong kho không đủ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contain)
            new AlertDialog.Builder(ProductDetailActivity.this).setTitle(R.string.app_name)
                    .setMessage("Sản phầm này đã tồn tại trong giỏ hàng của bạn!").setNegativeButton("Thêm", (ex, id) -> addItemOnCart(order))
                    .setPositiveButton("Bỏ qua", null).show();

        else
            addItemOnCart(order);
    }

    //Tai thong tin san pham
    private void getInfoTechnicalProduct() {
        ProductDetailService service = NetworkCommon.getRetrofit()
                .create(ProductDetailService.class);

        //Goi lai service
        Call<ResponseBody> call = service.getTechnicalData(item.getId());
        Database.print("request: " + call.request());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Gson gson = new Gson();
                try {
                    if (!response.isSuccessful())
                        throw new IOException("load sản phẩm không thành công!");

                    if (item.getCategoryid() == Product.LSP_PHONE) {
                        JsonObject json = Handler.convertToJSon(response.body().string());
                        PhoneInfo info = gson.fromJson(json.get("technical"), PhoneInfo.class);
                        fphone = new PhoneInfoFragement(info);

                        fmanager.beginTransaction()
                                .replace(R.id.frag_info_product, fphone).commit();
                    }

                    if (item.getCategoryid() == Product.LSP_LAPTOP) {
                        JsonObject json = Handler.convertToJSon(response.body().string());
                        LaptopInfo info = gson.fromJson(json.get("technical"), LaptopInfo.class);
                        flatop = new LaptopInfoFragment(info);
                        fmanager.beginTransaction().replace(R.id.frag_info_product, flatop)
                                .commit();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ProductDetailActivity.this.getBaseContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this.getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }

    //them hang vao gio
    public void addItemOnCart(Order order) {
        /**
         * Nếu đã đăng nhập thì sẽ giử tất cả dữ liệu lên server
         * Nếu chửa đăng nhập tất cả dữ liệu sẽ được lưu vào local database
         */
        boolean contain = index != -1;

        if (!MainActivity.getCurrentUser().isLogin()) {
            boolean success = true;

            if (contain) {
                Order newOrder = new Order(order.getId(), order.getName(), order.getPrice()
                        , order.getImage(), order.getAmount() + amount);
                newOrder.setSlmax(order.getSlmax());

                success = db.updateCart(order.getId(), newOrder);

            } else
                success = db.addOrder(order);

            if (success) {
                Handler.addItem(cart, order);
                Toast.makeText(this.getBaseContext(), "Đã thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getBaseContext(), "Lỗi thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        } else {
            BasketService service = NetworkCommon.getRetrofit().create(BasketService.class);

            User user = MainActivity.getCurrentUser();
            Call<ResponseBody> call = service.addOrder(user.getAccesstoken(), order.getId(), order.getAmount());

            Database.print("259: product " + call.request());

            //thực hiện xử lý thông tin nhận về
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JsonObject json = Handler.convertToJSon(response.body().string());
                        boolean success = json.has("message") && response.isSuccessful();
                        boolean contain = index != -1;

                        if (success) {
                            /**
                             *    Neu gio hang da chua san phan them so luong san pham vao vi tri do
                             *    Neu gio hang chua chua san pham thi se them san pham vao gio hang
                             */
                            if (contain)
                                Handler.addItem(MainActivity.getBasket(), order);
                            else
                                MainActivity.getBasket().add(order);
                            txtSl.setText(Handler.totalSize(MainActivity.getBasket()) + "");
                            Toast.makeText(getBaseContext(), json.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getBaseContext(), json.get("error").getAsString(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(ProductDetailActivity.this.getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        txtSl.setText(Handler.totalSize(cart) + "");
    }

    private void onActionOpenCart() {
        Intent intent = new Intent(this, BasketActivity.class);
        intent.setAction(PRODUCT_OPEN_ORDER);
        startActivityForResult(intent, REQUEST_SIZE_AMOUNT);
    }

    private void mapping() {
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
                Intent intent = new Intent(getApplicationContext(), BasketActivity.class);
                startActivityForResult(intent, REQUEST_SIZE_AMOUNT);
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
        TextView sluongton = findViewById(R.id.txttongsoluong);

        sluongton.setText("Số lượng tồn: " + item.getSlmax());
        txtNameDetail.setText(item.getName());
        txtPriceDetail.setText("Giá: " + Handler.formatMoney((long) item.getPrice()));
        txtDescription.setText(item.getDescription());

        Handler.loadImage(this, item.getImage(), imgDetail);
    }


    public void createShareLink(String quote, String url) {
        ShareDialog dialog = new ShareDialog(this);

        if (dialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                    .setQuote(quote)
                    .setContentUrl(Uri.parse(url))
                    .build();
            dialog.show(shareLinkContent);
        }
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
