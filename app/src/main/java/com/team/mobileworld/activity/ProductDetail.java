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
import com.squareup.picasso.Picasso;
import com.team.mobileworld.R;
import com.team.mobileworld.core.handle.APIhandler;
import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.core.object.Product;
import com.team.mobileworld.fragment.InfoLaptopFragment;
import com.team.mobileworld.fragment.InfoSmarphoneFragement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import maes.tech.intentanim.CustomIntent;

public class ProductDetail extends AppCompatActivity {
    public static final String PRODUCT_OPEN_ORDER = "PRODUCT_OPEN_ORDER";
    public static final int REPLACE_SIZE = 100;
    public static final String BUY_GOODS_NOW = "BUY_GOODS_NOW";
    public static final List<Order> OrderList = MainActivity.OrderList;

    Toolbar toolbarDetail;
    ImageView imgDetail;
    TextView txtNameDetail, txtPriceDetail, txtDescription, txtSl;
    Spinner spSluong, spPloai;
    Button btnOrder, btnBuy;
    ImageButton btnshare;
    FragmentManager fmanager;
    InfoSmarphoneFragement fphone;
    InfoLaptopFragment flatop;

    Product item;
    int amount = 0;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REPLACE_SIZE)
            txtSl.setText(APIhandler.getTotalAmount(OrderList));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //Anh xa phan tu
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
        final ImageButton btGioHang = findViewById(R.id.btGiohang);


        //Xu ly su kien
        btGioHang.setOnClickListener(e -> {
            Intent intent = new Intent(this, OrderActivity.class);
            intent.setAction(PRODUCT_OPEN_ORDER);
            startActivityForResult(intent, REPLACE_SIZE);
            CustomIntent.customType(this, Animation.LEFT_TO_RIGHT);
        });

        final List<Order> list = MainActivity.OrderList;
        txtSl.setText(APIhandler.getTotalAmount(OrderList));

        //Lay thong tin san pham
        item = (Product) ProductDetail.this.getIntent().getSerializableExtra("thongtin");

        //Dem tong so san pham co the mua
        final Stream<Order> stream = OrderList.stream().filter(e -> e.getId() == item.getId());
        amount = stream.mapToInt(Order::getAmount).sum();

        btnOrder.setOnClickListener(e -> {

            //Kiểm tra sản phẩm có tồn tại trong giỏ hàng không
            boolean contain = stream.count() != 0;

            if (contain)
                new AlertDialog.Builder(ProductDetail.this).setTitle("Mobile Word")
                        .setMessage("Sản phầm này đã tồn tại trong giỏ hàng của bạn!").setNegativeButton("Thêm", (ex, id) -> buyGoods(item, contain))
                        .setPositiveButton("Bỏ qua", null).show();

            else
                buyGoods(item, contain);

        });

        toolbarDetail.setNavigationOnClickListener((e) -> {
            finish();
            CustomIntent.customType(this, Animation.RIGHT_TO_LEFT);
        });

        btnBuy.setOnClickListener(e -> {
            Order order = new Order(item.getId(), item.getName(), Long.valueOf(item.getPrice()), item.getImage()
                    , Integer.valueOf(spSluong.getSelectedItem().toString()));
            Intent intent = new Intent(this, OrderedActivity.class);
            intent.putExtra("item", order);
            intent.setAction(BUY_GOODS_NOW);
            startActivity(intent);
            CustomIntent.customType(this, Animation.LEFT_TO_RIGHT);
        });


        fmanager = getSupportFragmentManager();

        //Chuyen doi thong tin ky thuat san pham
        if (item.getCategoryid() == Product.LSP_PHONE) {
            fphone = new InfoSmarphoneFragement();
            fmanager.beginTransaction().replace(R.id.frag_info_product, fphone).commit();
        }

        if (item.getCategoryid() == Product.LSP_LAPTOP) {
            flatop = new InfoLaptopFragment();
            fmanager.beginTransaction().replace(R.id.frag_info_product, flatop).commit();
        }

        btnshare.setOnClickListener(e->shareProductOnFacebook());

        getInfomation();
        catchEventSpinner();
    }

    public void shareProductOnFacebook() {
        //Thiet lap 1 dialog hien thi thong tin chia se
        ShareDialog share = new ShareDialog(this);

        if (share.canShow(ShareLinkContent.class)) {
            //Thiet lap 1 object chua doi tuong hien thi
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(item.getName())
                    .setContentDescription(item.getDescription())
                    .setContentUrl(Uri.parse("api/product/" + item.getId()))
                    .build();
            share.show(linkContent);
        } else
            Toast.makeText(this, "Sản phẩm chưa được chia sẻ", Toast.LENGTH_SHORT).show();
    }

    //them hang vao gio
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void buyGoods(Product item, boolean contain) {
        Order order = new Order(item.getId(), item.getName(), Long.valueOf(item.getPrice()), item.getImage()
                , Integer.valueOf(spSluong.getSelectedItem().toString()));
        /**
         *    Neu gio hang da chua san phan them so luong san pham vao vi tri do
         *    Neu gio hang chua chua san pham thi se them san pham vao gio hang
         */

        if (contain)
            APIhandler.add(OrderList, order);
        else
            OrderList.add(order);

        txtSl.setText(APIhandler.getTotalAmount(OrderList));
        Toast.makeText(ProductDetail.this.getBaseContext(), "Đã thêm sản phầm vào giỏ", Toast.LENGTH_SHORT).show();
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

    private void catchEventSpinner() {
        List<Integer> list = new ArrayList<>();
        int maxsize = item.getSlmax() - amount;
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
        txtPriceDetail.setText("Giá: " + APIhandler.formatMoney((long)item.getPrice()));
        txtDescription.setText(item.getDescription());

        Picasso.get().load(item.getImage()).placeholder(R.drawable.no_image_icon)
                .error(R.drawable.error)
                .fit().into(imgDetail);
    }

}
