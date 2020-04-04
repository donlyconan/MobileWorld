package com.letuan.mobileworld.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.letuan.mobileworld.R;
import com.letuan.mobileworld.adapter.OrderedApdater;
import com.letuan.mobileworld.model.Order;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class OrderedActivity extends AppCompatActivity {
    public static final List<Order> ORDER_LIST = MainActivity.OrderList;

    OrderedApdater apdater;
    ListView listView;
    List<Order> list;
    TextView txtTStien, txtTSpham, txtTTien;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered);
        Toolbar toolbar = findViewById(R.id.toolbar);
        listView = findViewById(R.id.listview);

        getDataFromIntent();

        toolbar.setNavigationOnClickListener(e->{
            finish();
            CustomIntent.customType(OrderedActivity.this, Animation.RIGHT_TO_LEFT);
        });

        txtTSpham = findViewById(R.id.txtTSpham);
        txtTStien = findViewById(R.id.txtTStien);
        txtTTien = findViewById(R.id.txtTTien);

        txtTSpham.setText("Tổng số (" + getTSPham() + " sản phầm):");
        String ThanhTien = getTotalMoney();
        txtTStien.setText(ThanhTien);
        txtTTien.setText(ThanhTien);

        Log.d("test", list.size() + "");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDataFromIntent() {
        if(getIntent().getAction() == ProductDetail.BUY_GOODS_NOW)
        {
            list = new ArrayList<>();
            list.add((Order) getIntent().getExtras().get("item"));
        } else {
            list = getProducts();

        }
        apdater = new OrderedApdater(this, list);
        listView.setAdapter(apdater);
    }

    public String getTotalMoney(){
        long sum = 0l;
        for(Order order : this.list) if(order.isSel()) sum += order.getTongSoTien();
        DecimalFormat format = new DecimalFormat("###,###,###");
        return format.format(sum) + " đ";
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getTSPham(){
        int sum = 0;
        for(Order item : list) sum += item.getSize();
        return sum;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Order> getProducts() {
        final List<Order> list = new ArrayList<>();
        ORDER_LIST.forEach(it -> {
            if(it.isSel())
                list.add(it);
        });
        return list;
    }
}
