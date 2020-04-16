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

import com.google.gson.JsonObject;
import com.team.mobileworld.R;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.handle.APIhandler;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.core.service.SignUpService;
import com.team.mobileworld.core.service.UserService;

import java.io.IOException;
import java.io.Serializable;

import maes.tech.intentanim.CustomIntent;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

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

        _loginLink.setOnClickListener(v->{
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
        });
    }

    private void anhxa() {
        _username = findViewById(R.id.inp_username);
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

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String username = _username.getText().toString();
        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        int id = sexs.getCheckedRadioButtonId();
        int gender = (id == R.id.rdboy ? 0 : id == R.id.rdgirl ? 1 : 2);


        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(() -> {
                    SignUpService service = NetworkCommon.getRetrofit().create(SignUpService.class);

                    Call<ResponseBody> call = service.registerAccount(username, password,  email);

                    try {
                        ResponseBody data = call.execute().body();
                        JsonObject json = APIhandler.convertToJSon(data.string());
                        if(json.has("message")) {
                            User user = new User(Long.valueOf(json.get("id").toString()),name,email,null,null,null,gender,null,null );
                            UserService update = NetworkCommon.getRetrofit().create(UserService.class);
                            Call<ResponseBody> callupdate = update.updatePersonalInfo(user);
                            call.enqueue(null);

                            getAlertBuilder("Đăng khoản thành công.").show();
                        } else {
                            getAlertBuilder("Đăng ký thất bại: " + json.get("error")).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(SignupActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    // depending on success
                    onSignupSuccess();
                    // onSignupFailed();
                    progressDialog.dismiss();
                }, 3000);
    }

    public AlertDialog.Builder getAlertBuilder(String content){
        return new AlertDialog.Builder(this).setTitle(MainActivity.APP_NAME)
                .setMessage(content).setPositiveButton("OK", null);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        Intent intent = new Intent();
        intent.putExtra("username", _username.getText());
        intent.putExtra("password", _passwordText.getText());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }


        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
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
        CustomIntent.customType(this, Animation.RIGHT_TO_LEFT);
    }
}