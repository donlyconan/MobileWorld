package com.team.mobileworld.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.team.mobileworld.R;
import com.team.mobileworld.adapter.OrderApdater;
import com.team.mobileworld.core.handle.APIhandler;
import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.core.object.Product;
import com.team.mobileworld.core.object.User;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class OrderActivity extends AppCompatActivity {
    public static final String Inte = "AC_OPEN_EDIT_ADD";
    private static final int REQUEST_PERSON = 10;

    OrderApdater apdater;
    RecyclerView recycler;
    List<Order> list;
    TextView txtTStien, txtTSpham, txtTTien, txtname, txtinfo;
    EditText txtaddress;
    Button btnDhang;
    ImageButton btnedit;
    Toolbar toolbar;
    List<Order> cart;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PERSON && resultCode == REQUEST_PERSON) {
            String address = data.getExtras().getString("address");
            txtaddress.setText(address);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //Anh xa phan tu
        elementMapping();

        //Cai dat hien thi
        init();

        btnDhang.setOnClickListener(e -> onActionOrder());

        btnedit.setOnClickListener(e -> startPersonalInfo());

        //Xu ly su kien
        toolbar.setNavigationOnClickListener(e -> finish());
    }

    private void startPersonalInfo() {
        Intent intent = new Intent(OrderActivity.this, PersonalInfoActivity.class);
        intent.setAction(Intent.ACTION_EDIT);
        startActivityForResult(intent, REQUEST_PERSON);
    }

    private void onActionOrder() {
        User user = MainActivity.getUser();

        //Kiem tra so dien thoai nguoi nhan
        if (MainActivity.validUser() == MainActivity.STATUS_LOGIN_NOT_PHONE) {
            Intent intent = new Intent(OrderActivity.this, PersonalInfoActivity.class);
            intent.setAction(Intent.ACTION_EDIT);
            startActivityForResult(intent, REQUEST_PERSON);
        } else
            //Kiem tra gio hang
            if (list.size() <= 0) {
                Toast.makeText(OrderActivity.this, "Vui lòng chọn mua sản phẩm trước khi đặt hàng.", Toast.LENGTH_SHORT).show();
            } else {
                cart.removeAll(list);
                Toast.makeText(OrderActivity.this, "Đặt hàng thàng công", Toast.LENGTH_SHORT).show();
                finish();
            }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(this, 1));
        getDataFromIntent();

        final User user = MainActivity.getUser();
        cart = MainActivity.getCart();

        if (user != null) {
            txtinfo.setText(PersonalInfoActivity.get(user.getFullname()) + " | " + PersonalInfoActivity.get(user.getPnumber()));
            txtaddress.setText(user.getAddress());
        } else {
            txtinfo.setText("");
            txtaddress.setFocusable(true);
            txtaddress.requestFocus();
        }
        //cai dat thuoc tinh
        txtTSpham.setText("Tổng số (" + APIhandler.getTotalAmount(list) + " sản phầm):");
        String ThanhTien = APIhandler.formatMoney(APIhandler.getTotalMoney(list));
        txtTStien.setText(ThanhTien);
        txtTTien.setText(ThanhTien);
    }

    private void elementMapping() {
        txtTSpham = findViewById(R.id.txtTSpham);
        txtTStien = findViewById(R.id.txtTStien);
        txtTTien = findViewById(R.id.txtTTien);
        txtaddress = findViewById(R.id.txtaddress);
        txtname = findViewById(R.id.txtname);
        toolbar = findViewById(R.id.toolbar);
        recycler = findViewById(R.id.recycle_view);
        btnDhang = findViewById(R.id.btnbuy);
        txtinfo = findViewById(R.id.txtinfoname);
        btnedit = findViewById(R.id.btnedit);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDataFromIntent() {
        if (getIntent().getAction() == ProductDetail.BUY_GOODS_NOW) {
            list = new ArrayList<>();
            list.add((Order) getIntent().getExtras().get("item"));
        } else {
            list = APIhandler.getProductSeleted(cart);
        }
        apdater = new OrderApdater(this, list);
        Log.d("SIZE", list.size() + "");
        recycler.setAdapter(apdater);
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
