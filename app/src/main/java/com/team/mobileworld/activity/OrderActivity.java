package com.team.mobileworld.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.team.mobileworld.R;
import com.team.mobileworld.adapter.OrderApdater;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.handle.Handler;
import com.team.mobileworld.core.handle.Validate;
import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.core.service.BasketService;
import com.team.mobileworld.core.service.BillService;
import com.team.mobileworld.core.task.Worker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import maes.tech.intentanim.CustomIntent;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.team.mobileworld.activity.MainActivity.RQ_OPEN_LOGIN;
import static com.team.mobileworld.activity.MainActivity.getCart;

public class OrderActivity extends AppCompatActivity {
    private static final int REQUEST_PERSON = 10;
    private static final int REQUEST_ADDRESS = 11;
    private static final int ID_NOTIFICATION = 100;

    OrderApdater apdater;
    RecyclerView recycler;
    List<Order> list;
    TextView txtTStien, txtTSpham, txtTTien;
    EditText inpname, inpaddress;
    Button btnDhang;
    ImageButton btnedit;
    Toolbar toolbar;
    List<Order> cart;
    ImageView imgaddress;
    ProgressDialog dialog;
    Call<ResponseBody> callorder;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PERSON && resultCode == RESULT_OK) {
            User user = MainActivity.getUser();
            String line = String.format("%s | %s", get(user.getFullname()), get(user.getPnumber()));
            inpname.setText(line);
            inpaddress.setText(get(user.getAddress()));
        }

        if (requestCode == REQUEST_ADDRESS && resultCode == RESULT_OK) {
            inpaddress.setText(MapsActivity.Address);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //Anh xa phan tu
        mapping();

        //Cai dat hien thi
        init();

        btnDhang.setOnClickListener(e -> onActionOrder());

        btnedit.setOnClickListener(e -> startPersonalInfo());

        //Xu ly su kien
        toolbar.setNavigationOnClickListener(e -> finish());

        imgaddress.setOnClickListener(e -> onActionOpenMap());


    }

    private void onActionOpenMap() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, REQUEST_ADDRESS);
    }

    private void startPersonalInfo() {
        Intent intent = new Intent(OrderActivity.this, PersonalInfoActivity.class);
        intent.setAction(Intent.ACTION_EDIT);
        startActivityForResult(intent, REQUEST_PERSON);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onActionOrder() {
        User user = MainActivity.getUser();
        String fullname = inpname.getText().toString();
        String address = inpaddress.getText().toString();

//        Kiem tra so dien thoai nguoi nhan
        if (user.isLogin() && user.hasPhoneNumber() && user.hasAddress()) {
            BasketService service = NetworkCommon.getRetrofit().create(BasketService.class);

            dialog.show();
            final List<Integer> idorders = list.stream().map(Order::getId).collect(Collectors.toList());

            /**
             * Tao thong bao day cho ung dung
             */
            Intent intent = new Intent(this, BillActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent, 0);
            final Notification noti = Handler.createNotificationChannel(this,pendingIntent
            , "Đặt hàng thành công!", "Bạn đã một đơn hàng, lúc "
                            + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime()) +
                            "hãy vào đơn mua để theo cập nhật thông tin sản phẩm sớm nhất.");
            final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            Worker worker = () -> {
                Map<String, Object> map = new HashMap<>();
                map.put("shiptoaddress", inpaddress.getText().toString());
                map.put("idorders", idorders);
                callorder = service.orderedList(user.getId(), map);

                callorder.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JsonObject json = Handler.convertToJSon(response.body().string());
                                if (json.has(MainActivity.MESSAGE)) {
                                    MainActivity.getCart().removeIf(Order::isSelect);
                                    Toast.makeText(OrderActivity.this, json.get(MainActivity.MESSAGE).getAsString(), Toast.LENGTH_SHORT).show();
                                    //Tra ve ok neu dat hang thanh cong
                                    manager.notify(0, noti);
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    LoginActivity.showToast(getBaseContext(), json.get(MainActivity.ERROR).getAsString());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(OrderActivity.this, "Không thể đặt thêm hàng do sản phẩm hiện đang được xét duyệt!", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(OrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            };

            if (ProductDetail.BUY_GOODS_NOW == getIntent().getAction()) {
                Order item = list.get(0);
                idorders.clear();
                idorders.add(item.getId());
                addProductOnCart(item.getId(), item.getAmount(), worker);
            } else
                worker.hanlde();
        } else {
            Intent intent = new Intent(this, PersonalInfoActivity.class);
            startActivityForResult(intent, RQ_OPEN_LOGIN);
        }

    }

    @MainThread
    public void addProductOnCart(int catalogid, int amount, Worker worker) {
        Call<ResponseBody> call = NetworkCommon.getRetrofit()
                .create(BasketService.class)
                .addOrder(MainActivity.getUser().getId(), catalogid, amount);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                    worker.hanlde();
                else
                    LoginActivity.showToast(getBaseContext(), "Lỗi giao dịch có thể do trùng đơn hàng!");
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LoginActivity.showToast(getBaseContext(), t.getMessage());
                dialog.dismiss();
            }
        });

    }


    private boolean validate(String fullname, String address, String sdt) {
        boolean valid = true;
        if (!Validate.valid(fullname, Validate.REGEX_NAME)) {
            inpname.setError("Tên không hợp lệ");
            valid = false;
        }
        if (!Validate.valid(address, Validate.REGEX_ADDRESS)) {
            inpaddress.setError("Địa chỉ không hợp lệ");
            valid = false;
        }
        return valid;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(this, 1));
        getDataFromIntent();

        final User user = MainActivity.getUser();
        cart = getCart();

        if (MainActivity.getUser() != null) {
            String line = String.format("%s | %s", user.getFullname(), user.getPnumber());
            inpname.setText(line);
            inpaddress.setText(user.getAddress());
        } else {
            inpname.findFocus();
        }
        dialog = new ProgressDialog(this);
        dialog.setMessage("Đang xử lý...");
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);

        //cai dat thuoc tinh
        txtTSpham.setText("Tổng số (" + Handler.getTotalAmount(list) + " sản phầm):");
        String ThanhTien = Handler.formatMoney(Handler.getTotalMoney(list));
        txtTStien.setText(ThanhTien);
        txtTTien.setText(ThanhTien);
    }

    private void mapping() {
        inpname = findViewById(R.id.inp_fullname);
        inpaddress = findViewById(R.id.inp_address);
        txtTSpham = findViewById(R.id.txtTSpham);
        txtTStien = findViewById(R.id.txtTStien);
        txtTTien = findViewById(R.id.txtTTien);
        toolbar = findViewById(R.id.toolbar);
        recycler = findViewById(R.id.recycle_view);
        btnDhang = findViewById(R.id.btnbuy);
        btnedit = findViewById(R.id.btnedit);
        imgaddress = findViewById(R.id.imgaddress);
    }

    public static String get(String a){
        return  a == null ? "" : a;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDataFromIntent() {
        if (getIntent().getAction() == ProductDetail.BUY_GOODS_NOW) {
            Order item = (Order) getIntent().getExtras().get("item");
            list = Arrays.asList(item);
        } else {
            list = Handler.getProductSeleted(getCart());
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


//    private void onShowAlertInfo() {
//        View view = getLayoutInflater().inflate(R.layout.alert_address, null);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                .setView(view);
//
//        Button btnhoantat = view.findViewById(R.id.btnhoantat);
//        Button btnhuybo = view.findViewById(R.id.btnhuybo);
//
//        EditText inpname = view.findViewById(R.id.inp_fullname);
//        EditText inpsdt = view.findViewById(R.id.inp_sdt);
//        EditText inpaddress = view.findViewById(R.id.inp_address);
//        final AlertDialog close = builder.show();
//
//
//        btnhoantat.setOnClickListener(e -> {
//            String fullname = inpname.getText().toString();
//            String sdt = inpsdt.getText().toString();
//            String address = inpaddress.getText().toString();
//
//            if (validate(fullname, address, sdt)) {
//                final User user = new User(1000000000L, fullname, null, null, null, null, 0, address, sdt);
//
//
//                close.dismiss();
//            }
//        });
//
//        btnhuybo.setOnClickListener(e -> close.dismiss());
//    }


}
