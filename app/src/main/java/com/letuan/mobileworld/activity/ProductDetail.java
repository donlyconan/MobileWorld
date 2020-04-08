package com.letuan.mobileworld.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
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

import com.letuan.mobileworld.R;
import com.letuan.mobileworld.fragment.InfoLaptopFragment;
import com.letuan.mobileworld.fragment.InfoSmarphoneFragement;
import com.letuan.mobileworld.model.Order;
import com.letuan.mobileworld.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import maes.tech.intentanim.CustomIntent;

import static java.lang.String.valueOf;

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
    FragmentManager fmanager;
    InfoSmarphoneFragement fphone;
    InfoLaptopFragment flatop;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REPLACE_SIZE)
            txtSl.setText(MainActivity.totalOrder());
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
        final ImageButton btGioHang = findViewById(R.id.btGiohang);


        //Xu ly su kien
        btGioHang.setOnClickListener(e -> {
            Intent intent = new Intent(this, OrderActivity.class);
            intent.setAction(PRODUCT_OPEN_ORDER);
            startActivityForResult(intent, REPLACE_SIZE);
            CustomIntent.customType(this, Animation.LEFT_TO_RIGHT);
        });

        final List<Order> list = MainActivity.OrderList;
        txtSl.setText(MainActivity.totalOrder());

        //Lay thong tin san pham
        final Product item = (Product) ProductDetail.this.getIntent().getSerializableExtra("thongtin");
        btnOrder.setOnClickListener(e -> {
            boolean contain = false;
            int size = 0;

            for (Order order : OrderList)
                if (order.getId() == item.getId()){
                    size = order.getSize();
                    contain = true;
                }

            size += Integer.valueOf(spSluong.getSelectedItem().toString());

            if (contain)
                new AlertDialog.Builder(ProductDetail.this).setTitle("Mobile Word")
                        .setMessage("Sản phầm này đã tồn tại trong giỏ hàng của bạn!").setNegativeButton("Thêm",(ex,id)->buyGoods(item))
                        .setPositiveButton("Bỏ qua", null).show();

            else
                buyGoods(item);

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
        if (item.getLsanpham() == Product.PL_SMARTPHONE) {
            fphone = new InfoSmarphoneFragement();
            fmanager.beginTransaction().replace(R.id.frag_info_product, fphone).commit();
        }

        if (item.getLsanpham() == Product.PL_LAPTOP) {
            flatop = new InfoLaptopFragment();
            fmanager.beginTransaction().replace(R.id.frag_info_product, flatop).commit();
        }

//        actionToolbar();
        getInfomation();
        catchEventSpinner();
//        catchEventButtonAddOrder();
    }

    //them hang vao gio
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void buyGoods(Product item)
    {
        Order order = new Order(item.getId(), item.getName(), Long.valueOf(item.getPrice()), item.getImage()
                , Integer.valueOf(spSluong.getSelectedItem().toString()));
        OrderActivity.addProducts(order);
        txtSl.setText(MainActivity.totalOrder());
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

//    private void catchEventButtonAddOrder() {
//        btnOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
//                //kiem tra trong gio hang co san pham chua
//                //-true- cong them san pham vao san pham da co trong gio hang
//                //-false- them san pham moi vao trong gio hang
//                boolean exists = false; // flag check ton tai san pham trong gio hang khong
////                if (MainActivity.orderList.size() > 0) {
////                    for (Order order : MainActivity.orderList) {
////                        if (order.getId() == id) {
////                            //tinh lai so luong cua san pham
////                            order.setSize(order.getSize() + soluong);
////                            //set max gia tri cho gio hang la 10
////                            if (order.getSize() >= 10) {
////                                order.setSize(10);
////                            }
////                            //tinh lai tong gia cho san pham
////                            order.setPriceProduct(Long.valueOf(order.getSize() * price));
////                            exists = true;
////                        }
////                    }
////                    if (!exists) {
////                        long newPrice = soluong * price;
////                        MainActivity.orderList.add(new Order(id, name, newPrice, image, soluong));
////                    }
////                } else {
////                    long newPrice = soluong * price;
////                    MainActivity.orderList.add(new Order(id, name, newPrice, image, soluong));
////                }
////                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
////                startActivity(intent);
//            }
//        });
//    }

    private void catchEventSpinner() {
        Integer[] size = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> arrayAdapter =
                new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, size);
        spSluong.setAdapter(arrayAdapter);

        String[] colors = new String[]{"Vàng", "Xanh", "Đỏ", "Cam"};
        ArrayAdapter<String> adapterColor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colors);
        spPloai.setAdapter(adapterColor);
    }

    private void getInfomation() {
        Product item = (Product) getIntent().getSerializableExtra("thongtin");
        TextView txttitle = findViewById(R.id.txtTitle);
        txttitle.setText(item.getName());

        txtNameDetail.setText(item.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtPriceDetail.setText("Giá: " + decimalFormat.format(item.getPrice()) + " $");
        txtDescription.setText(item.getDescription());
        Picasso.with(getApplicationContext()).load(item.getImage()).fit().centerCrop()
                .error(R.drawable.error).into(imgDetail);
    }

    private void actionToolbar() {
        setSupportActionBar(toolbarDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
