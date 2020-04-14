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
    public static final List<Order> ORDER_LIST = MainActivity.OrderList;
    public static final String AC_OPEN_EDIT_ADD = "AC_OPEN_EDIT_ADD";
    private static final int REQUEST_PERSON = 10;

    OrderApdater apdater;
    RecyclerView recycler;
    List<Order> list;
    TextView txtTStien, txtTSpham, txtTTien, txtname, txtinfo;
    EditText txtaddress;
    Button btnDhang;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PERSON && resultCode == 10) {
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
        txtTSpham = findViewById(R.id.txtTSpham);
        txtTStien = findViewById(R.id.txtTStien);
        txtTTien = findViewById(R.id.txtTTien);
        txtaddress = findViewById(R.id.txtaddress);
        txtname = findViewById(R.id.txtname);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        recycler = findViewById(R.id.recycle_view);
        btnDhang = findViewById(R.id.btnbuy);
        txtinfo = findViewById(R.id.txtinfoname);
        final ImageButton btnedit = findViewById(R.id.btnedit);

        //Cai dat hien thi
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(this, 1));

        final User user = MainActivity.getUser();

        if (user != null) {
            txtinfo.setText(PersonalInfoActivity.get(user.getFullname()) + " | " + PersonalInfoActivity.get(user.getPnumber()));
            txtaddress.setText(user.getAddress());
        } else {
            txtinfo.setText("");
            txtaddress.setFocusable(true);
            txtaddress.requestFocus();
        }

        btnDhang.setOnClickListener(e -> {

            //Kiem tra so dien thoai nguoi nhan
            if (user == null || user.getToken() == null || user.getPnumber() == null || user.getPnumber().length() == 0) {
                Intent intent = new Intent(OrderActivity.this, PersonalInfoActivity.class);
                intent.setAction(AC_OPEN_EDIT_ADD);
                startActivityForResult(intent, REQUEST_PERSON);
            } else
                //Kiem tra gio hang
                if (list.size() <= 0) {
                    Toast.makeText(OrderActivity.this, "Vui lòng chọn mua sản phẩm trước khi đặt hàng.", Toast.LENGTH_SHORT).show();
                } else {
                    ORDER_LIST.removeAll(list);
                    Toast.makeText(OrderActivity.this, "Đặt hàng thàng công", Toast.LENGTH_SHORT).show();
                    finish();
                }
        });

        btnedit.setOnClickListener(e -> {
            Intent intent = new Intent(OrderActivity.this, PersonalInfoActivity.class);
            intent.setAction(Intent.ACTION_EDIT);
            startActivityForResult(intent, REQUEST_PERSON);
        });

        getDataFromIntent();
        //Xu ly su kien
        toolbar.setNavigationOnClickListener(e -> finish());


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
