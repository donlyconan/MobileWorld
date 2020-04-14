package com.team.mobileworld.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;
import com.team.mobileworld.R;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.core.service.LoginService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import maes.tech.intentanim.CustomIntent;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "taglogin";
    public static final int REQUEST_SIGNUP = 200;
    public static final int ADD_INFO_USER = 201;
    public static final int REQUEST_INFOUSER = 202;
    public static final String ITEM_USER = "userinfo";
    public static final String OPEN_LOAD_USERINFO = "load user info";

    private EditText inpemail;
    private EditText inppassword;
    private Button btnlogin;
    LoginButton btnloginfb;
    private TextView linksignup;
    private User user;

    //Xử lý phản hồi đăng nhập
    private CallbackManager callbackManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        //Anh xa phan tu
        inpemail = findViewById(R.id.inp_email);
        inppassword = findViewById(R.id.inp_password);
        btnlogin = findViewById(R.id.btn_login);
        linksignup = findViewById(R.id.link_signup);
        btnloginfb = findViewById(R.id.btn_loginfb);


        //Xu ly su kien dang nhap
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //cau hinh callbackmanganer
        callbackManager = CallbackManager.Factory.create();
        final List<String> permissions = Arrays.asList("public_profile", "email", "user_birthday", "user_friends");

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
        linksignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Đăng ký sự kiện  chuyển tiếp (FB SDK)
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SIGNUP && resultCode == RESULT_OK) {
            // TODO: Implement successful signup logic here
            // By default we just finish the Activity and log them in automatically
            this.finish();
        }
    }


    //Tạo progressbar dialog custom
    public ProgressDialog createProgressBar(String text) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.color.colorForeign);
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
                    String id = object.getString("id");
                    String name = object.getString("name");
                    String email = object.getString("email");
                    String birthday = object.getString("birthday");
                    String urlimage = "http://graph.facebook.com/" + id + "/picture?type=large";

                    user = new User(null, id, name, email, urlimage, null, birthday, 0, null, null);
                    user.setToken("TK" + new Random().nextLong());
                    Log.d(TAG, response.toString());


                    Intent intent = new Intent();
                    Log.d(TAG, "Send user: " + user.toString());
                    intent.putExtra(ITEM_USER, user);
                    setResult(ADD_INFO_USER, intent);
                    finish();
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

    public void login() {
        Log.d("userinfo", "Login");

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
                                assert response != null;

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // On complete call either onLoginSuccess or onLoginFailed
                            onLoginSuccess();
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
        btnlogin.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed!", Toast.LENGTH_SHORT).show();
        btnlogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = inpemail.getText().toString();
        String password = inppassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inpemail.setError("enter a valid email address");
            valid = false;
        } else {
            inpemail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            inppassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            inppassword.setError(null);
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

}
