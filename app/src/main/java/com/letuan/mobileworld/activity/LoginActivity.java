package com.letuan.mobileworld.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.letuan.mobileworld.R;
import com.letuan.mobileworld.dialog.DialogCheck;
import com.letuan.mobileworld.ultil.CheckWifiValid;
import com.letuan.mobileworld.ultil.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText edtPhoneName, edtPassword, edtYourName;
    TextView txtTitleLogin;
    Button btnRegister, btnConfirm, btnBack;
    TextInputLayout tilYourName;
    String txtPhone, txtName, txtPassword;
    ProgressBar progLoadLogin;
    MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhxa();
        if (CheckWifiValid.haveNetworkConnection(getApplicationContext())) {
            eventButton();
        } else {
            DialogCheck dialogCheck = new DialogCheck(LoginActivity.this);
            dialogCheck.setNotification(LoginActivity.class, LoginActivity.this);
            dialogCheck.show();
        }

    }

    private void eventButton() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("Hủy".equals(btnBack.getText())) {
                    backLogin();
                } else {
                    finish();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnConfirm.setText("Đăng ký");
                btnBack.setText("Hủy");
                btnRegister.setVisibility(View.GONE);
                txtTitleLogin.setText("Đăng ký tài khoản");
                tilYourName.setVisibility(View.VISIBLE);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int vali = validateData();
                if (vali == 0) {
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }

            }
        });
    }

    private int validateData() {
        txtPhone = edtPhoneName.getText().toString().trim();
        txtPassword = edtPassword.getText().toString().trim();
        txtName = edtYourName.getText().toString().trim();

        //Validate data
        String regexPhonenumber = "^0\\d{9}$";
        String regexPassword = "^[\\w\\d @$%]{5,20}$";

        if (txtPhone.isEmpty()) {
            CheckWifiValid.ShowToast_Short(getApplicationContext(), "Chưa nhập số điện thoại");
            return 1;
        } else if (txtPassword.isEmpty()) {
            CheckWifiValid.ShowToast_Short(getApplicationContext(), "Chưa nhập mật khẩu");
            return 2;
        }

        Matcher matcher = Pattern.compile(regexPhonenumber).matcher(txtPhone);
        if (!matcher.matches()) {
            CheckWifiValid.ShowToast_Short(getApplicationContext(), "Số điện thoại không đúng");
            return 3;
        }
        matcher = Pattern.compile(regexPassword).matcher(txtPassword);
        if (!matcher.matches()) {
            CheckWifiValid.ShowToast_Short(getApplicationContext(), "Mật khẩu chứa 5 đến 20 kí tự");
            return 4;
        }

        //validate data username
        String regexName = "^\\D+$";

        if (btnConfirm.getText().equals("Đăng ký")){
            if (txtName.isEmpty()) {
                CheckWifiValid.ShowToast_Short(getApplicationContext(), "Chưa nhập tên của bạn");
                return 5;
            }
            matcher = Pattern.compile(regexName).matcher(txtName);
            if (!matcher.matches()) {
                CheckWifiValid.ShowToast_Short(getApplicationContext(), "Tên không chứa kí tự đặc biệt");
                return 6;
            }
        }

        return 0;
    }

    private void eventLoginAndRegister() {

        // catch event click btndangky
        if ("Đăng ký".equals(btnConfirm.getText())) {
            registerAccount();
        }
        // catch event click btnlogin
        else {
            login();
        }
    }

    private void login() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlPostLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progLoadLogin.setVisibility(View.GONE);
                        if ("OK".equals(response)) {
                            CheckWifiValid.ShowToast_Short(getApplicationContext(), "Thanh toán thành công");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CheckWifiValid.ShowToast_Short(getApplicationContext(), "404 Not Found");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> user = new HashMap<>();
                user.put("phonename", txtPhone);
                user.put("password", txtPassword);
                user.put("summoney", String.valueOf(OrderActivity.tongtien));
                return user;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void registerAccount() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlPostRegisterAccount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progLoadLogin.setVisibility(View.GONE);
                        if (response.equals("OK")) {
                            CheckWifiValid.ShowToast_Short(getApplicationContext(), "Đăng ký thành công");
                            backLogin();
                        } else {
                            CheckWifiValid.ShowToast_Short(getApplicationContext(), "Số " + txtPhone + " đã được sử dụng");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CheckWifiValid.ShowToast_Short(getApplicationContext(), "404 Not Found");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> user = new HashMap<>();
                user.put("phonename", txtPhone);
                user.put("password", txtPassword);
                user.put("username", txtName);
                return user;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void backLogin() {
        btnConfirm.setText("Xác nhận");
        btnBack.setText("Trở về");
        tilYourName.setVisibility(View.GONE);
        btnRegister.setVisibility(View.VISIBLE);
        edtYourName.setText("");
        edtPassword.setText("");
        txtTitleLogin.setText("Đăng nhập để thanh toán");
    }

    private void anhxa() {
        edtPhoneName = findViewById(R.id.edtusername);
        edtPassword = findViewById(R.id.edtpassword);
        btnRegister = findViewById(R.id.btnregister);
        btnConfirm = findViewById(R.id.btnconfirm);
        btnBack = findViewById(R.id.btnback);
        txtTitleLogin = findViewById(R.id.txttitlelogin);
        edtYourName = findViewById(R.id.edtyourname);
        tilYourName = findViewById(R.id.tilyourname);
        progLoadLogin = findViewById(R.id.progloadlogin);
        myHandler = new MyHandler();
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    btnConfirm.setEnabled(false);
                    btnBack.setEnabled(false);
                    btnRegister.setEnabled(false);
                    progLoadLogin.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    eventLoginAndRegister();
                    progLoadLogin.setVisibility(View.GONE);
                    btnConfirm.setEnabled(true);
                    btnBack.setEnabled(true);
                    btnRegister.setEnabled(true);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread {
        @Override
        public void run() {
            super.run();
            myHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = myHandler.obtainMessage(1);
            myHandler.sendMessage(message);
        }
    }
}
