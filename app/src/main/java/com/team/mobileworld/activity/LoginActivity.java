package com.team.mobileworld.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.team.mobileworld.core.handle.APIhandler;
import com.team.mobileworld.core.object.Account;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.core.service.LoginService;
import com.team.mobileworld.database.Database;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import maes.tech.intentanim.CustomIntent;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "taglogin";
    public static final int REQUEST_SIGNUP = 200;
    public static final int ADD_INFO_USER = 201;
    public static final int REQUEST_INFOUSER = 202;
    public static final int REQUEST_LOAD_INFOUSER = 202;
    public static final String ITEM_USER = "userinfo";
    public static final String OPEN_LOAD_USERINFO = "load user info";

    private EditText inpemail;
    private EditText inppassword;
    private Button btnlogin;
    LoginButton btnloginfb;
    private TextView linksignup;
    private CheckBox remember;
    private TextView txtforgot;
    private User user;
    private Database db;

    //Xử lý phản hồi đăng nhập
    private CallbackManager callbackManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        //Anh xa phan tu
        elementMapping();

        db = MainActivity.getDatabaseInstence();

        //Thiet lap co ban lay du lieu tu database
        init();

        //Xu ly su kien dang nhap
        btnlogin.setOnClickListener(v -> login());

        //cau hinh callbackmanganer
        callbackManager = CallbackManager.Factory.create();
        final List<String> permissions = Arrays.asList("public_profile", "email", "user_birthday", "user_friends");

        //Dang ky quyen truy cap
        btnloginfb.setReadPermissions(permissions);
        btnloginfb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loadPersonalInfo(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "error: " + error.getMessage());
            }
        });

        //Đăng ký tài khoản
        linksignup.setOnClickListener(e -> {
            // Start the Signup activity
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
        });
    }

    private void init() {
        Account account = db.getAccount();
        if (account != null) {
            inpemail.setText(account.getUsername());
            inppassword.setText(account.getPassword());
            remember.setChecked(true);
        }
    }

    private void onActionChange(boolean bool) {
        if (bool) {
            String username = inpemail.getText().toString();
            String password = inppassword.getText().toString();
            db.updatePassword(username, password);
        }
    }

    private void elementMapping() {
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
        super.onActivityResult(requestCode, resultCode, data);

        //Đăng ký sự kiện  chuyển tiếp (FB SDK)
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SIGNUP && resultCode == RESULT_OK) {
            inpemail.setText(data.getExtras().getString("username"));
            inppassword.setText(data.getExtras().getString("password"));
        }
    }

    //Tạo progressbar dialog custom
    public ProgressDialog createProgressBar(String text) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.color.colorAccent);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(text);
        return progressDialog;
    }


    //Lấy thông tin người kết nối FB
    private void loadPersonalInfo(AccessToken token) {

        //API hiển thị activity đăng nhập
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    user = getInfoUserFromFacebook(object);
                    Log.d(TAG, response.toString());
                    onStartMainActivity();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Bundle params = new Bundle();
        params.putString("fields", "name, id, email,birthday, gender,friends");
        request.setParameters(params);
        request.executeAsync();
    }


    public void onStartMainActivity() {
        Intent intent = new Intent();
        intent.putExtra(ITEM_USER, user);
        setResult(ADD_INFO_USER, intent);
        finish();
    }


    public static User getInfoUserFromFacebook(JSONObject object) throws JSONException {
        User user = null;
        long id = Long.valueOf(object.getString("id"));
        String name = object.getString("name");
        String email = object.getString("email");
        String birthday = object.getString("birthday");
        String urlimage = "http://graph.facebook.com/" + id + "/picture?type=large";

        return new User(id, name, email, email, null, birthday, 0, null, null);
    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
        } else {
            String email = inpemail.getText().toString();
            String password = inppassword.getText().toString();

            //Thiet lap service
            LoginService service = NetworkCommon.getRetrofit().create(LoginService.class);

            //Thiet lap callback truy cap url giu va nhan du lieu 
            Call<ResponseBody> call = service.loginAccount(email, password);

            btnlogin.setEnabled(false);
            ProgressDialog progressDialog = createProgressBar("Đang đăng nhập...");
            progressDialog.show();


            // TODO: Implement your own authentication logic here.

            new android.os.Handler().post(
                    new Runnable() {
                        public void run() {
                            //Thuc hien goi lai lay ket qua
                            try {
                                ResponseBody response = call.execute().body();
                                JsonObject json = APIhandler.convertToJSon(response.string());

                                if (json.has("id")) {
                                    MainActivity.setUser(new User(Long.valueOf(json.get("id").toString())));
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    onLoginSuccess();

                                    //Dang ky
                                    onActionChange(remember.isChecked());
                                    onStartMainActivity();
                                } else {
                                    onLoginFailed();
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bạn: " + json.get("error").toString(), Toast.LENGTH_LONG).show();
                                }


                            } catch (IOException e) {
                                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            }
                            // onLoginFailed();
                            progressDialog.dismiss();
                        }
                    });
        }
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
        startActivityForResult(null, REQUEST_LOAD_INFOUSER);
        btnlogin.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed!", Toast.LENGTH_SHORT).show();
        btnlogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

//        String email = inpemail.getText().toString();
//        String password = inppassword.getText().toString();
//
//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            inpemail.setError("enter a valid email address");
//            valid = false;
//        } else {
//            inpemail.setError(null);
//        }
//
//        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
//            inppassword.setError("between 4 and 10 alphanumeric characters");
//            valid = false;
//        } else {
//            inppassword.setError(null);
//        }

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

}
