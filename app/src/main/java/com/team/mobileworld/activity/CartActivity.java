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
import androidx.fragment.app.Fragment;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.team.mobileworld.R;
import com.team.mobileworld.adapter.CartAdapter;
import com.team.mobileworld.core.handle.APIhandler;
import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.database.Database;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class CartActivity extends AppCompatActivity {
    private static final int RQ_CHANGE_DATA = 10;
    private static final String ACTION_LOGIN = "login for buy";

    ListView listview;
    static TextView txtthanhtien;
    Button btnthanhtoan;
    CheckBox checkall;
    Toolbar toolbarorder;
    CartAdapter adapter;
    Fragment fragment;
    Database db;
    List<Order> cart;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RQ_CHANGE_DATA && resultCode == LoginActivity.ADD_INFO_USER) {
            MainActivity.setUser((User) data.getExtras().getSerializable(LoginActivity.ITEM_USER));
        }

        if (requestCode == RQ_CHANGE_DATA) {
            adapter.notifyDataSetChanged();
            if (adapter.getSize() == 0) checkall.setChecked(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Anh xa [han tu
        elementMaping();

        //Cai dat listview
        init();

        //Xu ly su kien
        checkall.setOnClickListener((e) -> onActionChecked());

        toolbarorder.setNavigationOnClickListener(e -> finish());

        btnthanhtoan.setOnClickListener(e -> onActionBuy());

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtthanhtien.setText(APIhandler.formatMoney(APIhandler.getTotalMoneySelected(cart)));
            }
        });

        txtthanhtien.setText(APIhandler.getFormatTotalMoneySelected(cart));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onActionChecked() {
        cart.forEach(es -> es.setSelect(checkall.isChecked()));
        adapter.notifyDataSetChanged();
        txtthanhtien.setText(APIhandler.formatMoney(APIhandler.getTotalMoneySelected(cart)));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onActionBuy() {

        boolean canbuy = (cart.stream().filter(Order::isSelect).count() != 0);

        //Neu chua dang nhap thi nguoi dung phai dang nhap
        if (MainActivity.validUser() == 0) {
            Intent intent = new Intent(CartActivity.this, LoginActivity.class);
            intent.setAction(ACTION_LOGIN);
            startActivityForResult(intent, RQ_CHANGE_DATA);
        } else

            //Xem xét nếu có thể và không thể mua hàng
            if (!canbuy) {
                String text = cart.size() == 0 ? "Danh sách trống!" : "Bạn chưa chọn sản phầm nào!";
                Toast.makeText(CartActivity.this.getBaseContext(), text, Toast.LENGTH_SHORT).show();
            } else {
                startActivityForResult(new Intent(CartActivity.this, OrderActivity.class)
                        , 10);
            }
    }

    private void init() {
        cart = MainActivity.getCart();
        adapter = new CartAdapter(CartActivity.this, cart);
        listview.setAdapter(adapter);

        db = MainActivity.getDatabaseInstence();
        checkall = findViewById(R.id.ckSelAll);
        if (cart.size() != 0)
            checkall.setChecked(true);
    }

    private void elementMaping() {
        listview = findViewById(R.id.listvieworder);
        txtthanhtien = findViewById(R.id.txtThanhTien);
        btnthanhtoan = findViewById(R.id.btMuaHang);
        toolbarorder = findViewById(R.id.toolbarorder);
        setSupportActionBar(toolbarorder);
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

        switch (id) {
            case R.id.item_delete: {
                //Tính tổng số lượng sản phẩm  được chọn có trong giỏ hàng
                int count = cart.stream().filter(Order::isSelect)
                        .mapToInt(Order::getAmount).sum();

                if (MainActivity.validUser() == 0)
                    deleteOnLocalDatabase(count);
                else
                    deleteOnServer(count);

                txtthanhtien.setText(APIhandler.getFormatTotalMoneySelected(cart));
                return true;
            }

            case R.id.item_selall: {
                cart.forEach(e -> e.setSelect(true));
                adapter.notifyDataSetChanged();
                return true;
            }

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void deleteOnLocalDatabase(final int count) {

        if (count == 0) {
            Toast.makeText(this, "Không có sản phẩm nào để xóa", Toast.LENGTH_SHORT).show();
        } else {
            String message = "Bạn có muốn xóa " + count + " sản phẩm ra khỏi giỏ hàng không?";

            AlertDialog.Builder builder = new AlertDialog.Builder(this).
                    setTitle("Xoá").setMessage(message)
                    .setNegativeButton("Có", (e, mid) -> {
                        int index = 0, delete = 0;
                        List<Order> arrList = new ArrayList<>();

                        for (Order item : cart) {
                            if (item.isSelect()) {
                                delete = db.deleleOrder(item.getId());

                                if(delete > 0){
                                    arrList.add(item);
                                    index++;
                                }
                            }
                        }

                        adapter.getOrderList().removeAll(arrList);
                        adapter.notifyDataSetChanged();
                        Database.print("Delete count=" + index + "\t\t size=" + cart.size());
                    }).setPositiveButton("Không", null);
            builder.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteOnServer(int count) {
        if (count == 0) {
            Toast.makeText(this, "Không có sản phẩm nào để xóa", Toast.LENGTH_SHORT).show();
        } else {
            String message = "Bạn có muốn xóa " + count + " sản phẩm ra khỏi giỏ hàng không?";

            AlertDialog.Builder builder = new AlertDialog.Builder(this).
                    setTitle("Xoá").setMessage(message)
                    .setNegativeButton("Có", (e, mid) -> {
                        cart.removeIf(Order::isSelect);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(CartActivity.this,
                                "Đã xóa " + count + " sản phẩm", Toast.LENGTH_SHORT).show();
                    }).setPositiveButton("Không", null);
            builder.show();
            adapter.notifyDataSetChanged();
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
