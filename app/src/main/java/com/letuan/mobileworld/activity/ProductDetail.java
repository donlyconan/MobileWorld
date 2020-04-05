package com.letuan.mobileworld.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.letuan.mobileworld.model.Order;
import com.letuan.mobileworld.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

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
    Spinner spinner;
    Button btnOrder, btnBuy;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REPLACE_SIZE)
            txtSl.setText(MainActivity.totalOrder());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        toolbarDetail = findViewById(R.id.toolbardetail);
        imgDetail = findViewById(R.id.imgdetail);
        txtNameDetail = findViewById(R.id.txtnamedetail);
        txtPriceDetail = findViewById(R.id.txtpricedetail);
        txtDescription = findViewById(R.id.txtdescriptiondetail);
        spinner = findViewById(R.id.spSluong);
        btnOrder = findViewById(R.id.btnorder);
        btnBuy = findViewById(R.id.btnbuy);

        txtSl = findViewById(R.id.txtSoluong);
        final ImageButton btGioHang = findViewById(R.id.btGiohang);

        btGioHang.setOnClickListener(e->{
            Intent intent = new Intent(this, OrderActivity.class);
            intent.setAction(PRODUCT_OPEN_ORDER);
            startActivityForResult(intent,REPLACE_SIZE);
            CustomIntent.customType(this, Animation.LEFT_TO_RIGHT);
        });

        final List<Order> list = MainActivity.OrderList;
        txtSl.setText(MainActivity.totalOrder());

        btnOrder.setOnClickListener(e->{
                Product item = (Product) ProductDetail.this.getIntent().getSerializableExtra("thongtin");
                Order order = new Order(list.size(), item.getName(), Long.valueOf(item.getPrice()), item.getImage()
                        , Integer.valueOf(spinner.getSelectedItem().toString()));
                OrderActivity.addProducts(order);
                txtSl.setText(MainActivity.totalOrder());
                Toast.makeText(ProductDetail.this.getBaseContext(), "Đã thêm sản phầm vào giỏ", Toast.LENGTH_SHORT).show();
         });

        toolbarDetail.setNavigationOnClickListener((e)->{
            finish();
            CustomIntent.customType(this,Animation.RIGHT_TO_LEFT);
        });

        btnBuy.setOnClickListener(e->{
            Product item = (Product) ProductDetail.this.getIntent().getSerializableExtra("thongtin");
            Order order = new Order(list.size(), item.getName(), Long.valueOf(item.getPrice()), item.getImage()
                    , Integer.valueOf(spinner.getSelectedItem().toString()));
            Intent intent = new Intent(this, OrderedActivity.class);
            intent.putExtra("item", order);
            intent.setAction(BUY_GOODS_NOW);
            startActivity(intent);
            CustomIntent.customType(this, Animation.LEFT_TO_RIGHT);
        });



//        actionToolbar();
        getInfomation();
        catchEventSpinner();
//        catchEventButtonAddOrder();
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
        spinner.setAdapter(arrayAdapter);
    }

    private void getInfomation() {
        Product item = (Product) getIntent().getSerializableExtra("thongtin");
        TextView tittle = findViewById(R.id.txttitle);
        tittle.setText(item.getName());

        txtNameDetail.setText(item.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtPriceDetail.setText("Giá: " + decimalFormat.format(item.getPrice()) + " $");
        txtDescription.setText(item.getDescription());
        Picasso.with(getApplicationContext()).load(item.getImage()).fit().centerCrop().placeholder(R.drawable.error)
                .error(R.drawable.error).into(imgDetail);
    }

    private void actionToolbar() {
        setSupportActionBar(toolbarDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
