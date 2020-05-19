package com.team.mobileworld.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

import com.google.gson.JsonObject;
import com.team.mobileworld.R;
import com.team.mobileworld.adapter.BasketAdapter;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.handle.Handler;
import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.core.service.BasketService;
import com.team.mobileworld.core.database.Database;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import maes.tech.intentanim.CustomIntent;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasketActivity extends AppCompatActivity {
    private static final int REQUEST_CHANGE_DATA = 0x001;
    private static final int REQUEST_LOGIN_ORDER = 0x002;
    private static final String ACTION_LOGIN = "open_acivity_login";

    private List<Order> cart;
    private int amount, index;
    BasketAdapter adapter;
    private Order item;

    ListView listview;
    static TextView txtthanhtien;
    Button btnthanhtoan;
    CheckBox checkall;
    Toolbar toolbarorder;
    Database db;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHANGE_DATA && resultCode == RESULT_OK) {
            adapter.notifyDataSetChanged();
            if (adapter.getSize() == 0) checkall.setChecked(false);
        }

        if (requestCode == REQUEST_LOGIN_ORDER) {
            cart = MainActivity.getBasket();
            adapter.setOrderList(cart);
            adapter.notifyDataSetChanged();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        //Anh xa phan tu
        mapping();

        //Cai dat listview
        init();

        //Xu ly su kien
        checkall.setOnClickListener((e) -> onActionChecked());

        toolbarorder.setNavigationOnClickListener(e -> finish());

        btnthanhtoan.setOnClickListener(e -> onActionBuy());

        listview.setOnItemClickListener((parent, view, position, id) ->
                txtthanhtien.setText(Handler.formatMoney(Handler.getTotalMoneySelected(cart)))
        );

        txtthanhtien.setText(Handler.getFormatTotalMoneySelected(cart));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onActionChecked() {
        cart.forEach(es -> es.setSelect(checkall.isChecked()));
        adapter.notifyDataSetChanged();
        txtthanhtien.setText(Handler.formatMoney(Handler.getTotalMoneySelected(cart)));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        MainActivity.getBasket().forEach(e -> e.setSelect(true));
        cart = MainActivity.getBasket();
        adapter = new BasketAdapter(BasketActivity.this, cart);
        listview.setAdapter(adapter);
        db = MainActivity.getDatabaseInstence();
        checkall = findViewById(R.id.ckSelAll);
        if (cart.size() != 0)
            checkall.setChecked(true);

    }
    
    
    /**
     * Xu ly su kien mua hang
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onActionBuy() {
        /**
         * Neu da dang nhap goi den vao activity mua hang
         * Neu chua dangg nhap yeu cau dang nhap
         */
        if (!MainActivity.getCurrentUser().isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN_ORDER);
        } else {
            //tinh so san pham duoc chon
            boolean canbuy = (MainActivity.getBasket().stream().filter(Order::isSelect).count() != 0);

            //Xem xét nếu có thể và không thể mua hàng
            if (!canbuy) {
                String text = cart.size() == 0 ? "Danh sách trống!" : "Bạn chưa chọn sản phầm nào!";
                Toast.makeText(BasketActivity.this.getBaseContext(), text, Toast.LENGTH_SHORT).show();
            } else {
                startActivityForResult(new Intent(BasketActivity.this, OrderActivity.class)
                        , REQUEST_CHANGE_DATA);
            }
        }
    }


    private void mapping() {
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
                int count = cart.stream().filter(x -> !x.isSelect())
                        .mapToInt(Order::getAmount).sum();

                if (MainActivity.getCurrentUser().isLogin())
                    //xoa san pham tren server
                    deleteOnServer(count);
                else
                    //xoa san pham tai local database
                    deleteOnLocalDatabase(count);
                txtthanhtien.setText(Handler.getFormatTotalMoneySelected(cart));
                return true;
            }

            case R.id.item_selall: {
                cart.forEach(e -> e.setSelect(false));
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
                    setTitle("Xoá sản phẩm").setMessage(message)
                    .setNegativeButton("Có", (e, mid) -> {
                        int index = 0;
                        //Lay danh sach san pham can xoa
                        List<Order> arrList = cart.stream().filter(x -> !x.isSelect()).collect(Collectors.toList());

                        //Xoa san pham tai local
                        MainActivity.getDatabaseInstence()
                                .deleteOrders(arrList);
                        //xoa san pham trong gio hang
                        cart.removeAll(arrList);
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

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Xoá").setMessage(message)
                    .setNegativeButton("Có", (e, mid) -> {
                        List<Integer> delids = cart.stream().filter(x -> !x.isSelect())
                                .mapToInt(Order::getId)
                                .boxed()
                                .collect(Collectors.toList());

                        BasketService service = NetworkCommon.getRetrofit().create(BasketService.class);

                        //Goi den api xoa san pham
                        Call<ResponseBody> call = service.deleteOrder(MainActivity.getCurrentUser().getAccesstoken(), delids);

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    JsonObject json = Handler.convertToJSon(response.body().string());
                                    if (response.isSuccessful() && json.has(MainActivity.MESSAGE)) {
                                        Toast.makeText(BasketActivity.this,
                                                "Đã xóa " + count + " sản phẩm", Toast.LENGTH_SHORT).show();
                                        cart.removeIf(e -> delids.contains(e.getId()));
                                        adapter.notifyDataSetChanged();
                                    } else
                                        LoginActivity.showToast(getBaseContext(), json.get(MainActivity.ERROR).getAsString());
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(BasketActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).setPositiveButton("Không", null);
            builder.show();
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
