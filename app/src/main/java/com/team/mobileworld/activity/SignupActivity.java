package com.team.mobileworld.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.team.mobileworld.R;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.handle.Validate;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.core.service.RegisterService;
import com.team.mobileworld.core.service.UserService;
import com.team.mobileworld.core.database.Database;

import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private EditText _nameText;
    private EditText _username;
    private EditText _emailText;
    private EditText _passwordText;
    private EditText _reEnterPasswordText;
    private Button _signupButton;
    private TextView _loginLink;
    RadioGroup sexs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        anhxa();

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(v -> finish());
    }

    private void anhxa() {
        _username = findViewById(R.id.inp_newpass);
        _nameText = findViewById(R.id.inp_fullname);
        _emailText = findViewById(R.id.inp_email);
        _passwordText = findViewById(R.id.inp_password);
        _reEnterPasswordText = findViewById(R.id.inp_repassword);
        _signupButton = findViewById(R.id.btn_signup);
        _loginLink = findViewById(R.id.link_login);
        sexs = findViewById(R.id.rd_sexs);
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setMessage("Đang xử lý...");
        progress.show();

        String username = _username.getText().toString();
        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        int id = sexs.getCheckedRadioButtonId();
        int gender = (id == R.id.rdboy ? 0 : id == R.id.rdgirl ? 1 : 2);


        // TODO: Implement your own signup logic here.
        RegisterService service = NetworkCommon.getRetrofit().create(RegisterService.class);

        Call<User> call = service.registerAccount(username, password, email);

        Database.print("Sign up: " + call.request());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                Database.print("Sign up user: " + user);

                if (user != null && response.isSuccessful()) {
                    user.setGender(user.getGender());
                    user.setFullname(name);

                    UserService update = NetworkCommon.getRetrofit().create(UserService.class);
                    Call<User> callupdate = update.updatePersonalInfo(user);

                    Database.print("Update user: " + callupdate.request());

                    callupdate.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User user = response.body();
                            if (response.isSuccessful() && user != null) {
                                MainActivity.setUser(user);
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                    MainActivity.setUser(user);
                    getAlertBuilder("Đăng ký tài khoản thành công.").show();
                    onSignupSuccess();
                } else {
                    getAlertBuilder("Tài khoản đã tồn tại!").show();
                }
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                progress.dismiss();
            }
        });


    }

    public AlertDialog.Builder getAlertBuilder(String content) {
        return new AlertDialog.Builder(this).setTitle(R.string.app_name)
                .setMessage(content).setPositiveButton("OK", null);
    }

    public boolean validate() {
        boolean valid = true;
        if (!Validate.valid(_nameText.getText().toString(), Validate.REGEX_NAME)) {
            _nameText.setError("Tên không hợp lệ.");
            valid = false;
        }
        if (!Validate.valid(_emailText.getText().toString(), Validate.REGEX_EMAIL)) {
            _emailText.setError("Email không hợp lệ.");
            valid = false;
        }
        if (!Validate.valid(_username.getText().toString(), Validate.REGEX_USERNAME)) {
            _username.setError("Tên đăng không hợp lệ.");
            valid = false;
        }
        if (!Validate.valid(_passwordText.getText().toString(), Validate.REGEX_PASSWORD)) {
            _passwordText.setError("Mật khẩu không hợp lệ.");
            valid = false;
        }
        if (!_reEnterPasswordText.getText().toString().equals(_passwordText.getText().toString())) {
            _reEnterPasswordText.setError("Mật khẩu không trùng khớp");
            valid = false;
        }
        return valid;
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        Intent intent = new Intent();
        intent.putExtra("username", _username.getText());
        intent.putExtra("password", _passwordText.getText());
        setResult(RESULT_OK, intent);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Đăng ký thất bại!", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

//    public boolean validate() {
//        boolean valid = true;
//
//        String name = _nameText.getText().toString();
//        String email = _emailText.getText().toString();
//        String password = _passwordText.getText().toString();
//        String reEnterPassword = _reEnterPasswordText.getText().toString();
//
//        if (name.isEmpty() || name.length() < 3) {
//            _nameText.setError("at least 3 characters");
//            valid = false;
//        } else {
//            _nameText.setError(null);
//        }
//
//
//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            _emailText.setError("enter a valid email address");
//            valid = false;
//        } else {
//            _emailText.setError(null);
//        }
//
//
//        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
//            _passwordText.setError("between 4 and 10 alphanumeric characters");
//            valid = false;
//        } else {
//            _passwordText.setError(null);
//        }
//
//        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
//            _reEnterPasswordText.setError("Password Do not match");
//            valid = false;
//        } else {
//            _reEnterPasswordText.setError(null);
//        }
//
//        return valid;
//    }

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