package com.team.mobileworld.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.share.widget.ShareDialog;
import com.team.mobileworld.R;
import com.team.mobileworld.adapter.OrderAdapter;
import com.team.mobileworld.core.handle.APIhandler;
import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.ultil.CheckWifiValid;

import java.text.DecimalFormat;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class OrderActivity extends AppCompatActivity {
    public static final List<Order> OrderList = MainActivity.OrderList;

    ListView listview;
    static TextView txtthanhtien;
    Button btnthanhtoan;
    CheckBox checkall;
    Toolbar toolbarorder;
    OrderAdapter adapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            adapter.notifyDataSetChanged();
            if (adapter.getSize() == 0) checkall.setChecked(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //Anh xa [han tu
        listview = findViewById(R.id.listvieworder);
        txtthanhtien = findViewById(R.id.txtThanhTien);
        btnthanhtoan = findViewById(R.id.btMuaHang);
        toolbarorder = findViewById(R.id.toolbarorder);
        setSupportActionBar(toolbarorder);


        //Cai dat listview
        adapter = new OrderAdapter(OrderActivity.this, OrderList);
        listview.setAdapter(adapter);

        checkall = findViewById(R.id.ckSelAll);
        checkall.setChecked(true);

        //Xu ly su kien
        checkall.setOnClickListener((e) -> {
            OrderList.forEach(es -> es.setSelect(checkall.isChecked()));
            adapter.notifyDataSetChanged();
            txtthanhtien.setText(APIhandler.formatMoney(APIhandler.getTotalMoneySelected(OrderList)));
        });

        toolbarorder.setNavigationOnClickListener(e -> {
            finish();
            CustomIntent.customType(OrderActivity.this, Animation.RIGHT_TO_LEFT);
        });

        btnthanhtoan.setOnClickListener(e -> {
            boolean canbuy = (OrderList.stream().filter(Order::isSelect).count() != 0);

            //Xem xét nếu có thể và không thể mua hàng
            if(!canbuy)
            {
                String text = OrderList.size() == 0 ? "Danh sách trống!" : "Bạn chưa chọn sản phầm nào!";
                Toast.makeText(OrderActivity.this, text, Toast.LENGTH_SHORT).show();
                return;
            } else {
                Intent intent = new Intent(OrderActivity.this, OrderedActivity.class);
                startActivityForResult(intent, 10);
                CustomIntent.customType(OrderActivity.this, Animation.LEFT_TO_RIGHT);
            }

        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtthanhtien.setText(APIhandler.formatMoney(APIhandler.getTotalMoneySelected(OrderList)));
            }
        });

        txtthanhtien.setText(APIhandler.getFormatTotalMoneySelected(OrderList));
    }

    public void shareProductOnFacebook()
    {
        ShareDialog shareDialog;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Lấy id menu item
        int id = item.getItemId();

        switch (id)
        {
            case R.id.item_delete:{
                //Tính tổng số lượng sản phẩm  được chọn có trong giỏ hàng
                int count = OrderList.stream().filter(Order::isSelect)
                        .mapToInt(Order::getAmount).sum();

                Log.d("SLuong", "count=" + count);

                if(count == 0) {
                    Toast.makeText(this, "Không có sản phẩm nào để xóa", Toast.LENGTH_SHORT).show();
                } else {
                    String message = "Bạn có muốn xóa " + count +  " sản phẩm ra khỏi giỏ hàng không?";

                    AlertDialog.Builder builder = new AlertDialog.Builder(this).
                            setTitle("Xoá").setMessage(message)
                            .setNegativeButton("Có", (e,mid) -> {
                                OrderList.removeIf(Order::isSelect);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(OrderActivity.this,
                                        "Đã xóa " + count + " sản phẩm", Toast.LENGTH_SHORT).show();
                            }).setPositiveButton("Không",null);
                    builder.show();
                }
                return true;
            }

            case R.id.item_selall:{
                OrderList.forEach(e->e.setSelect(true));
                adapter.notifyDataSetChanged();
                return true;
            }

            default:
            break;
        }

        return super.onOptionsItemSelected(item);
    }


}
