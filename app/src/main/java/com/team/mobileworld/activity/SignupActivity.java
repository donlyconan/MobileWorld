package com.team.mobileworld.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.team.mobileworld.R;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.handle.Validate;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.core.service.RegisterService;
import com.team.mobileworld.core.database.Database;

import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "signup_out_debug";

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

        mapping();

        //Dang ky tai khoan
        _signupButton.setOnClickListener(e -> signup());

        _loginLink.setOnClickListener(v -> finish());
    }

    private void mapping() {
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
        if (!validate()) {
            onSignupFailed();
            return;
        }

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setMessage("Đang xử lý...");
        progress.show();

        String fullname = _nameText.getText().toString();
        String username = _username.getText().toString();
        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        int id = sexs.getCheckedRadioButtonId();
        int gender = (id == R.id.rdboy ? 0 : id == R.id.rdgirl ? 1 : 2);


        // TODO: Implement your own signup logic here.
        RegisterService service = NetworkCommon.getRetrofit().create(RegisterService.class);

        Call<User> callback = service.registerAccount(fullname, username, password, email, gender);

        Database.print("Sign up: " + callback.request());

        callback.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                Database.print("Sign up user: " + user);

                if (user != null && response.isSuccessful()) {
                    MainActivity.setCurrentUser(user);
                    createAndShowDialog(SignupActivity.this,"Đăng ký tài khoản."
                            ,"Đăng ký tài khoản thành công."
                            , R.drawable.ic_check_green);
                    onSignupSuccess();
                } else {
                    createAndShowDialog(SignupActivity.this,"Đăng ký tài khoản",
                            "Tài khoản đã tồn tại!"
                            , R.drawable.ic_error);
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

    public static View createAndShowDialog(Activity activity, String title, String content
            ,int resource) {
        View view = activity.getLayoutInflater().inflate(R.layout.notification_dialog, null);
        TextView txttitle = view.findViewById(R.id.txt_title);
        Button btnok = view.findViewById(R.id.btn_ok);
        TextView txtcontent = view.findViewById(R.id.txt_content);
        ImageView img_image = view.findViewById(R.id.img_image);

        txtcontent.setText(content);
        txttitle.setText(title);
        img_image.setImageResource(resource);

        final AlertDialog factory = new AlertDialog.Builder(activity)
                .setView(view).show();
        btnok.setOnClickListener(e->factory.dismiss());
        return view;
    }

    public boolean validate() {
        boolean valid = true;
        if (!Validate.validate(_nameText.getText().toString(), Validate.REGEX_NAME)) {
            _nameText.setError("Tên không hợp lệ.");
            valid = false;
        }
        if (!Validate.validate(_emailText.getText().toString(), Validate.REGEX_EMAIL)) {
            _emailText.setError("Email không hợp lệ.");
            valid = false;
        }
        if (!Validate.validate(_username.getText().toString(), Validate.REGEX_USERNAME)) {
            _username.setError("Tên đăng không hợp lệ.");
            valid = false;
        }
        if (!Validate.validate(_passwordText.getText().toString(), Validate.REGEX_PASSWORD)) {
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