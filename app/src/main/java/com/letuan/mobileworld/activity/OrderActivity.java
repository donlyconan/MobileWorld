package com.letuan.mobileworld.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.letuan.mobileworld.R;
import com.letuan.mobileworld.adapter.OrderAdapter;
import com.letuan.mobileworld.dialog.NoticeDialog;
import com.letuan.mobileworld.model.Order;
import com.letuan.mobileworld.model.Product;
import com.letuan.mobileworld.ultil.CheckWifiValid;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class OrderActivity extends AppCompatActivity {
    public static final List<Order> OrderList = MainActivity.OrderList;
    public static Long tongtien = 0L;

    ListView listViewOrder;
    static TextView txtThanhTien;
    Button btnThanhToan;
    CheckBox ckSelAll;
    Toolbar toolbarOrder;
    OrderAdapter orderAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            orderAdapter.notifyDataSetChanged();
            if (orderAdapter.getSize() == 0) ckSelAll.setChecked(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //Anh xa [han tu
        listViewOrder = findViewById(R.id.listvieworder);
        txtThanhTien = findViewById(R.id.txtThanhTien);
        btnThanhToan = findViewById(R.id.btMuaHang);
        toolbarOrder = findViewById(R.id.toolbarorder);


        //Cai dat listview
        orderAdapter = new OrderAdapter(OrderActivity.this, OrderList);
        listViewOrder.setAdapter(orderAdapter);

        ckSelAll = findViewById(R.id.ckSelAll);
        ckSelAll.setChecked(true);
        //Xu ly su kien
        ckSelAll.setOnClickListener((e) -> {
            OrderList.forEach(es -> es.setSel(ckSelAll.isChecked()));
            orderAdapter.notifyDataSetChanged();
            txtThanhTien.setText(updateGoods());
        });

        toolbarOrder.setNavigationOnClickListener(e -> {
            finish();
            CustomIntent.customType(OrderActivity.this, Animation.RIGHT_TO_LEFT);
        });

        btnThanhToan.setOnClickListener(e -> {
            boolean canbuy = false;
            //Kiem tra danhsach chon
            for (Order order : OrderList)
                if (order.isSel()) {
                    canbuy = true;
                    break;
                }

            //Kiem tra co the mua hay khong
            if (!canbuy) {
                String text = OrderList.size() == 0 ? "Danh sách trống!" : "Bạn chưa chọn sản phầm cần mua";
                Toast.makeText(OrderActivity.this, text, Toast.LENGTH_SHORT).show();
                return;
            } else {
                Intent intent = new Intent(OrderActivity.this, OrderedActivity.class);
                startActivityForResult(intent, 10);
                CustomIntent.customType(OrderActivity.this, Animation.LEFT_TO_RIGHT);
            }

        });

        listViewOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtThanhTien.setText(updateGoods());
            }
        });

        txtThanhTien.setText(this.updateGoods());
//        checkData();
//        eventUltil();
////        catchOnItemListViewClicked();
//        eventButton();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String updateGoods() {
        long sum = 0l;
        for (Order order : OrderList) if (order.isSel()) sum += order.getTongSoTien();
        DecimalFormat format = new DecimalFormat("###,###,###");
        return format.format(sum) + " đ";
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void addProducts(Order order) {
        boolean end = false;

        for (int i = 0; i < OrderList.size(); i++) {
            Order item = OrderList.get(i);
            if (order.getId() == item.getId()) {
                OrderList.get(i).setSize(order.getSize() + item.getSize());
                end = true;
            }
        }
        if (!end) {
            OrderList.add(0, order);
        }
    }


    private void eventButton() {

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OrderList.size() > 0) {
                    finish();
                    CustomIntent.customType(OrderActivity.this, Animation.RIGHT_TO_LEFT);
                } else {
                    CheckWifiValid.ShowToast_Short(getApplicationContext(), "Giỏ hàng trống");
                }
            }
        });
    }

//    private void catchOnItemListViewClicked() {
//        listViewOrder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                final NoticeDialog noticeDialog = new NoticeDialog(OrderActivity.this);
//                noticeDialog.setCancelable(true);
//                noticeDialog.setNotification("Xóa sản phẩm khỏi giỏ hàng ?", "Đồng ý", "Hủy", new View.OnClickListener() {
//                    @RequiresApi(api = Build.VERSION_CODES.N)
//                    @Override
//                    public void onClick(View v) {
//                        if (v.getId() == R.id.btn_ok) {
//                            if (orderList.size() <= 0) {
//                                txtMessage.setVisibility(View.VISIBLE);
//                            } else {
//                                orderList.remove(position);
//                                orderAdapter.notifyDataSetChanged();
//                                eventUltil();
//                                if (orderList.size() <= 0) {
//                                    txtMessage.setVisibility(View.VISIBLE);
//                                } else {
//                                    txtMessage.setVisibility(View.INVISIBLE);
//                                    orderAdapter.notifyDataSetChanged();
//                                    eventUltil();
//                                }
//                            }
//                        }
//                        noticeDialog.dismiss();
//                    }
//                });
//                noticeDialog.show();
//
//                return true;
//            }
//        });
//    }

    // bat su kien do len list view
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void eventUltil() {
        tongtien = 0L;
        OrderList.forEach(e -> {
            if (e.isSel())
                tongtien += e.getTongSoTien();
        });
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtThanhTien.setText(decimalFormat.format(tongtien) + " đ");
    }

    private void checkData() {
        //ko co data hien thong bao chua co san pham trong gio hang
        if (OrderList.size() > 0) {
            orderAdapter.notifyDataSetChanged();
//            txtMessage.setVisibility(View.INVISIBLE);
            listViewOrder.setVisibility(View.VISIBLE);
        } else {
            orderAdapter.notifyDataSetChanged();
//            txtMessage.setVisibility(View.VISIBLE);
            listViewOrder.setVisibility(View.INVISIBLE);
        }
    }


}
