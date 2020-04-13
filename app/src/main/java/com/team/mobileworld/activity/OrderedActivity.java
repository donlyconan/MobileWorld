package com.team.mobileworld.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
import com.team.mobileworld.adapter.OrderedApdater;
import com.team.mobileworld.core.handle.APIhandler;
import com.team.mobileworld.core.object.Order;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class OrderedActivity extends AppCompatActivity {
    public static final List<Order> ORDER_LIST = MainActivity.OrderList;
    public static final String AC_OPEN_EDIT_ADD = "AC_OPEN_EDIT_ADD";

    OrderedApdater apdater;
    RecyclerView recycler;
    List<Order> list;
    TextView txtTStien, txtTSpham, txtTTien, txtname, txtaddress;
    Button btnDhang;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == 10) {
            String address = data.getExtras().getString("address");
            txtaddress.setText(address);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered);

        //Anh xa phan tu
        txtTSpham = findViewById(R.id.txtTSpham);
        txtTStien = findViewById(R.id.txtTStien);
        txtTTien = findViewById(R.id.txtTTien);
        txtaddress = findViewById(R.id.txtaddress);
        txtname = findViewById(R.id.txtname);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        recycler = findViewById(R.id.recycle_view);
        btnDhang = findViewById(R.id.btnbuy);
        final ImageButton btnedit = findViewById(R.id.btnedit);

        //Cai dat hien thi
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(this, 1));
//        DividerItemDecoration did = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);

//        recycler.addItemDecoration(new VerticalSpace);
        btnDhang.setOnClickListener(e -> {
            if (list.size() <= 0) {
                Toast.makeText(OrderedActivity.this, "Vui lòng chọn mua sản phẩm trước khi đặt hàng.", Toast.LENGTH_SHORT).show();
            } else {
                ORDER_LIST.removeAll(list);
                Toast.makeText(OrderedActivity.this, "Đặt hàng thàng công", Toast.LENGTH_SHORT).show();
                finish();
                CustomIntent.customType(OrderedActivity.this, Animation.RIGHT_TO_LEFT);
            }
        });

        btnedit.setOnClickListener(e -> {
            Intent intent = new Intent(OrderedActivity.this, PersonalInfoActivity.class);
            intent.setAction(Intent.ACTION_EDIT);
            startActivityForResult(intent, 10);
            CustomIntent.customType(OrderedActivity.this, Animation.LEFT_TO_RIGHT);
        });

        getDataFromIntent();
        //Xu ly su kien
        toolbar.setNavigationOnClickListener(e -> {
            finish();
            CustomIntent.customType(OrderedActivity.this, Animation.RIGHT_TO_LEFT);
        });


        //cai dat thuoc tinh
        txtTSpham.setText("Tổng số (" + APIhandler.getTotalAmount(list) + " sản phầm):");
        String ThanhTien = APIhandler.formatMoney(APIhandler.getTotalMoney(list));
        txtTStien.setText(ThanhTien);
        txtTTien.setText(ThanhTien);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDataFromIntent() {
        if (getIntent().getAction() == ProductDetail.BUY_GOODS_NOW) {
            list = new ArrayList<>();
            list.add((Order) getIntent().getExtras().get("item"));
        } else {
            list = APIhandler.getProductSeleted(ORDER_LIST);
        }
        apdater = new OrderedApdater(this, list);
        Log.d("SIZE", list.size() + "");
        recycler.setAdapter(apdater);

    }





}
