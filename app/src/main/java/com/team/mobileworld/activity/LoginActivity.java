package com.team.mobileworld.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;
import com.team.mobileworld.R;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.database.Database;
import com.team.mobileworld.core.handle.Handler;
import com.team.mobileworld.core.handle.LoginMobileWorld;
import com.team.mobileworld.core.handle.Validate;
import com.team.mobileworld.core.object.Account;
import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.core.service.BasketService;
import com.team.mobileworld.core.service.LoginService;
import com.team.mobileworld.core.service.UserService;
import com.team.mobileworld.core.task.Worker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import maes.tech.intentanim.CustomIntent;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "taglogin";
    public static final int REQUEST_SIGNUP = 200;
    public static final int ADD_INFO_USER = 201;
    public static final String ITEM_USER = "userinfo";
    public static final String OPEN_LOAD_USERINFO = "load_user_info";
    private static final long DELAY_APP = 3000;

    private EditText inpemail;
    private EditText inppassword;
    private Button btnlogin;
    LoginButton btnloginfb;
    private TextView linksignup;
    private CheckBox remember;
    private TextView txtforgot;
    private User user;
    private Database db;
    private ProgressDialog dialog;

    //Xử lý phản hồi đăng nhập
    private CallbackManager callbackManager;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        //Anh xa phan tu
        mapping();

        db = MainActivity.getDatabaseInstence();

        //Thiet lap co ban lay du lieu tu database
        init();

        //Xu ly su kien dang nhap
        btnlogin.setOnClickListener(v -> login());

        //cau hinh callbackmanganer
        callbackManager = CallbackManager.Factory.create();

        dialog.setMessage("Đang liên kết...");

        //Dang ky quyen truy cap
        btnloginfb.setReadPermissions(Arrays.asList("public_profile", "email"));

        btnloginfb.registerCallback(callbackManager, facebookCallback);

        //Đăng ký tài khoản
        linksignup.setOnClickListener(e -> {
            // Start the Signup activity
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
        });

        txtforgot.setOnClickListener(e -> forgotPassword());
    }

    private FacebookCallback facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            dialog.show();
            Worker success = () -> {
                if (MainActivity.getCurrentUser().isLogin()) {
                    onLoginSuccess();
                    setResult(RESULT_OK);
                } else {
                    LoginManager.getInstance().logOut();
                    onLoginFailed();
                }
            };
            loginWithFacebook(success);
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException error) {
            Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            onLoginFailed();
        }
    };

    private void forgotPassword() {
        EditText inpusername, inpemail;
        Button btncancle;
        View view = getLayoutInflater().inflate(R.layout.alert_address, null);
        inpusername = view.findViewById(R.id.inp_newpass);
        inpemail = view.findViewById(R.id.inp_email);
        final Button btnok = view.findViewById(R.id.btnhoantat);
        btncancle = view.findViewById(R.id.btnhuybo);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(view);
        final AlertDialog close = builder.show();

        btnok.setOnClickListener(e -> {
            String username = inpusername.getText().toString(), email = inpemail.getText().toString();
            boolean hoantat = true;
            if (!Validate.validate(username, Validate.REGEX_USERNAME)) {
                hoantat = false;
                inpusername.setError("Tài khoản không hợp lệ!");
            }

            if (!Validate.validate(email, Validate.REGEX_EMAIL)) {
                hoantat = false;
                inpemail.setError("Email không hợp lệ!");
            }

            if (hoantat) {
                btnok.setEnabled(false);
                Call<ResponseBody> call = NetworkCommon.getRetrofit()
                        .create(UserService.class)
                        .forgotPassword(username, email);
                dialog.show();
                dialog.setMessage("Đang xử lý...");

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if (response.isSuccessful()) {
                                JsonObject json = Handler.convertToJSon(response.body().string());
                                Database.print(response.body().string());
                                if (json.has("message"))
                                    showToast(getApplicationContext(), json.get("message").getAsString());
                                else
                                    showToast(getApplicationContext(), json.get("error").getAsString());
                            }
                        } catch (IOException ex) {
                            showToast(getBaseContext(), ex.getMessage());
                            ex.printStackTrace();
                        }
                        close.dismiss();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showToast(getBaseContext(), t.getMessage());
                        btnok.setEnabled(true);
                        dialog.dismiss();
                    }

                });
            }
        });

        btncancle.setOnClickListener(e -> close.dismiss());
    }

    //Lấy thông tin người kết nối FB
    public void loginWithFacebook(Worker action) {

        //Su dung GraphReuest lay thong tin nguoi dang ky dich vu
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    user = LoginMobileWorld.getInfoFromJsonFacebook(object);
                    String myavatar = user.getAvatar();
                    Log.d(TAG, "response: " + response.toString());

                    //Dang nhap
                    LoginService service = NetworkCommon.getRetrofit().create(LoginService.class);
                    Call<User> call = service.loginWithFacebook(user);
                    Database.print("request: " + call.request());

                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            user = response.body();
                            Database.print(user.toString());

                            if (response.isSuccessful() && user != null) {
                                user.setLink(User.LOGIN_FACEBOOK);

                                if(user.getAvatar() == null)
                                    user.setAvatar(myavatar);

                                MainActivity.setCurrentUser(user);
                                action.hanlde();
                            } else
                                Toast.makeText(getBaseContext(), "Đăng nhập thất bại!", Toast.LENGTH_SHORT)
                                        .show();
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                            action.hanlde();
                            LoginManager.getInstance().logOut();
                            t.printStackTrace();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Bundle params = new Bundle();
        params.putString("fields", "name, id, email,gender");
        request.setParameters(params);
        request.executeAsync();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        Account account = db.getAccount();
        if (account != null) {
            inpemail.setText(account.getUsername());
            inppassword.setText(account.getPassword());
            remember.setChecked(true);
        }

        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Đang đăng nhập...");

        user = MainActivity.getCurrentUser();

        //Neu la hanh dong tu dong dang nap truyen tu ham main se thuc hien tu dong dang nhap
        if (MainActivity.ACTION_AUTO_LOGIN.equals(getIntent().getAction()) && account != null) {
            login();
        }

    }

    private void onActionChange(boolean bool) {
        if (bool) {
            String username = inpemail.getText().toString();
            String password = inppassword.getText().toString();
            db.addAccount(username, password);
        }
    }

    private void mapping() {
        inpemail = findViewById(R.id.inp_email);
        inppassword = findViewById(R.id.inp_password);
        btnlogin = findViewById(R.id.btn_login);
        linksignup = findViewById(R.id.link_signup);
        btnloginfb = findViewById(R.id.btn_loginfb);
        remember = findViewById(R.id.ck_remeber);
        txtforgot = findViewById(R.id.txt_forgot);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Đăng ký sự kiện  chuyển tiếp (FB SDK)
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SIGNUP && resultCode == RESULT_OK) {
            inpemail.setText(data.getExtras().getString("username"));
            inppassword.setText(data.getExtras().getString("password"));
        }
    }

    public void onStartMainActivity() {
        setResult(ADD_INFO_USER);
        finish();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void login() {
        btnlogin.setEnabled(false);
        dialog.setMessage("Đang đăng nhập...");
        dialog.show();

        if (!validate()) {
            onLoginFailed();
            return;
        }

        String username = inpemail.getText().toString();
        String password = inppassword.getText().toString();

        //Thiet lap service
        LoginService service = NetworkCommon.getRetrofit().create(LoginService.class);

        //Thiet lap callback truy cap url giu va nhan du lieu
        Call<User> call = service.loginWithAccount(username, password);

        Database.print("login: " + call.request());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                final User user = response.body();
                if (user != null && user.isLogin() && response.isSuccessful()) {
                    Database.print("id user=" + user);
                    user.setLink(User.LOGIN_ACCOUNT);
                    MainActivity.setCurrentUser(user);
                    MainActivity.NewLogin = true;
                    onLoginSuccess();
                    onActionChange(remember.isChecked());
                } else {
                    Toast.makeText(getBaseContext(), "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_LONG).show();
                    onLoginFailed();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                onLoginFailed();
            }
        });
    }


    @Override
    protected void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
        super.onBackPressed();
    }

    public void onLoginSuccess() {
        MainActivity.NewLogin = true;
        try {
            pushDataToServer();
        } catch (IOException e) {
        }
        MainActivity.getBasket().clear();

        BasketService service = NetworkCommon.getRetrofit().create(BasketService.class);

        Call<List<Order>> call = service.loadCart(MainActivity.getCurrentUser().getAccesstoken());

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                db.print("Json get Cart:" + response.body());
                if (response.isSuccessful()) {
                    MainActivity.getBasket().addAll(response.body());
                    onStartMainActivity();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                onStartMainActivity();
            }
        });

        setResult(RESULT_OK);
        dialog.dismiss();
    }

    @MainThread
    public void pushDataToServer() throws IOException {
        Database db = MainActivity.getDatabaseInstence();
        User user = MainActivity.getCurrentUser();

        List<Order> list = db.getAllCart();
        BasketService service = NetworkCommon.getRetrofit().create(BasketService.class);

        for (Order item : list) {
            Call<ResponseBody> call = service.addOrder(user.getAccesstoken(), item.getId(), item.getAmount());
            synchronized (call) {
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        }
        db.deleleOrder(Database.DEL_ALL);
    }

    public void onLoginFailed() {
        btnlogin.setEnabled(true);
        dialog.dismiss();
    }

    public boolean validate() {
        boolean valid = true;
        String username = inpemail.getText().toString();
        String password = inppassword.getText().toString();

        if (!Validate.validate(username, Validate.REGEX_USERNAME)) {
            inpemail.setError("Tài khoản không hợp lệ.");
            valid = false;
        }
        if (!Validate.validate(password, Validate.REGEX_PASSWORD)) {
            inppassword.setError("Mật khẩu không hợp lệ");
            valid = false;
        }
        return valid;
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
        CustomIntent.customType(this, Animation.FADE_IN_TO_OUT);
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}

