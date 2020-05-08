package com.team.mobileworld.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.team.mobileworld.R;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.database.Database;
import com.team.mobileworld.core.handle.Handler;
import com.team.mobileworld.core.handle.Validate;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.core.service.UserService;

import java.io.FileDescriptor;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalInfoActivity extends AppCompatActivity {

    private static final int READ_RQ_IMAGE = 150;
    private static final int READ_RQ_BIMAGE = 151;
    private static final int REQUEST_ADDRESS = 153;
    EditText txtfullname, txtpassword;
    EditText txtdofbirth, txtaddress, txtphonenumber, txtemail;
    Spinner spsexs;
    TextView txtusername;
    CircleImageView imgprofit;
    ImageView imgbground;
    ProgressDialog dialog;
    ImageButton btnaddaddress;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADDRESS && resultCode == RESULT_OK) {
            txtaddress.setText(MapsActivity.Address);
        }

        if (data == null || data.getData() == null)
            return;

        try {
            switch (requestCode) {
                case READ_RQ_IMAGE: {
                    Uri uri = data.getData();
                    imgprofit.setImageBitmap(getBitmapFromUri(uri));
                }
                break;

                case READ_RQ_BIMAGE: {
                    Uri uri = data.getData();
                    imgbground.setImageBitmap(getBitmapFromUri(uri));
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        //Anh xa phan tu
        Toolbar toolbar = findViewById(R.id.toolbar);
        txtfullname = findViewById(R.id.txt_fullname);
        txtusername = findViewById(R.id.txt_username);
        txtpassword = findViewById(R.id.txt_password);
        txtaddress = findViewById(R.id.txt_address);
        txtdofbirth = findViewById(R.id.txt_birthofdate);
        txtemail = findViewById(R.id.txt_email);
        txtphonenumber = findViewById(R.id.txt_phonenumber);
        imgprofit = findViewById(R.id.img_profit);
        imgbground = findViewById(R.id.img_background);
        spsexs = findViewById(R.id.sp_sexs);
        btnaddaddress = findViewById(R.id.btnaddaddress);
        setSupportActionBar(toolbar);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Đăng xử lý...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);

        //Cai dat spiner
        final String[] sexs = new String[]{"Nam", "Nữ", "Không xác định"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_custom, sexs);
        spsexs.setAdapter(arrayAdapter);
        spsexs.setSelection(0);

        //Xu ly su kien
        toolbar.setNavigationOnClickListener(e -> {

            //Kiem tra hanh dong
            if (Intent.ACTION_EDIT.equals(getIntent().getAction())) {
                setResult(RESULT_OK);
            }

            finish();
        });

        imgprofit.setOnClickListener(e -> openReplaceImage(READ_RQ_IMAGE));

        imgbground.setOnClickListener(e -> openReplaceImage(READ_RQ_BIMAGE));

        txtpassword.setOnClickListener(e -> changePassword());

        btnaddaddress.setOnClickListener(e -> onActionOpenMap());

        showInfo();
    }

    private void onActionOpenMap() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, REQUEST_ADDRESS);
    }


    private void changePassword() {
        if (MainActivity.getUser().getLink() == User.LOGIN_ACCOUNT) {
            EditText inpoldpass, inpnewpass, inprepass;
            View view = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
            inpoldpass = view.findViewById(R.id.inp_oldpass);
            inpnewpass = view.findViewById(R.id.inp_newpass);
            inprepass = view.findViewById(R.id.inp_repass);
            Button btnoldpass = view.findViewById(R.id.btn_oldpass);
            Button btnnewpass = view.findViewById(R.id.btn_newpass);
            Button btnrepass = view.findViewById(R.id.btn_repass);
            final Button btnok = view.findViewById(R.id.btnhoantat);
            Button btncancle = view.findViewById(R.id.btnhuybo);

            btnoldpass.setOnClickListener(e -> inpoldpass.setTransformationMethod(show(inpoldpass.getTransformationMethod())));
            btnnewpass.setOnClickListener(e -> inpnewpass.setTransformationMethod(show(inpnewpass.getTransformationMethod())));
            btnrepass.setOnClickListener(e -> inprepass.setTransformationMethod(show(inprepass.getTransformationMethod())));

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setView(view);
            final AlertDialog close = builder.show();

            btnok.setOnClickListener(e -> {
                String oldpass = inpoldpass.getText().toString(), newpass = inpnewpass.getText().toString(), repass = inprepass.getText().toString();
                boolean hoantat = true;
                if (!Validate.valid(oldpass, Validate.REGEX_PASSWORD)) {
                    hoantat = false;
                    inpoldpass.setError("Mật khẩu không hợp lệ!");
                }
                if (!Validate.valid(newpass, Validate.REGEX_PASSWORD)) {
                    hoantat = false;
                    inpnewpass.setError("Mật khẩu không hợp lệ!");
                }
                if (hoantat && newpass == repass) {
                    hoantat = false;
                    inprepass.setError("Mật khẩu không trùng khớp!");
                }
                if (hoantat) {
                    btnok.setEnabled(false);
                    Call<ResponseBody> call = NetworkCommon.getRetrofit()
                            .create(UserService.class)
                            .changePassowrd(MainActivity.getUser().getId(), oldpass, newpass);
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
        } else
            showToast(getBaseContext(), MainActivity.getUser().statusLogin());

    }

    private TransformationMethod show(TransformationMethod transfor) {
        if (transfor == HideReturnsTransformationMethod.getInstance())
            return PasswordTransformationMethod.getInstance();
        else
            return HideReturnsTransformationMethod.getInstance();
    }


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    public void openReplaceImage(final int mode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        //Loc file
        intent.setType("image/*");
        startActivityForResult(intent, mode);
    }

    private void showInfo() {

        User user = MainActivity.getUser();

        if (user != null) {
            Picasso.get().load(user.getProfit())
                    .placeholder(R.drawable.ic_profit)
                    .error(R.drawable.ic_profit)
                    .fit().into(imgprofit);
            txtfullname.setText(get(user.getFullname()));
            txtaddress.setText(get(user.getAddress()));
            txtdofbirth.setText(get(user.getBdate()));
            txtemail.setText(get(user.getEmail()));
            txtphonenumber.setText(get(user.getPnumber()));
            txtusername.setText(user.getUsername());
            spsexs.setSelection(user.getGender());
            txtpassword.setText("********");
        }
    }


    public static String get(String text) {

        return text == null ? "" : text;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.item_update) {
            String fullname = txtfullname.getText().toString();
            String std = txtphonenumber.getText().toString();
            String email = txtemail.getText().toString();
            int gender = spsexs.getSelectedItemPosition();
            String address = txtaddress.getText().toString();

            User user = MainActivity.getUser();
            user.setFullname(fullname);
            user.setAddress(address);
            user.setEmail(email);
            user.setGender(gender);
            user.setPnumber(std);
            if (validate()) {
                dialog.show();

                UserService service = NetworkCommon.getRetrofit().create(UserService.class);

                Call<User> call = service.updatePersonalInfo(user);

                Database.print("Rquest: " + call.request());

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        if (response.isSuccessful()) {
                            Toast.makeText(getBaseContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                        } else
                            Toast.makeText(getBaseContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                        dialog.dismiss();
                    }
                });
            } else
                dialog.dismiss();

        }

        return super.onOptionsItemSelected(item);
    }


    public boolean validate() {
        String std = txtphonenumber.getText().toString();
        String email = txtemail.getText().toString();
        String address = txtaddress.getText().toString();

        if (!Validate.valid(email, Validate.REGEX_EMAIL)) {
            txtemail.setError("Email không hợp lệ");
            return false;
        }
        if (!Validate.valid(std, Validate.REGEX_PHONE_NUMBER)) {
            txtphonenumber.setError("Số điện thoại không hợp lệ");
            return false;
        }
        if (!Validate.valid(address, Validate.REGEX_ADDRESS)) {
            txtaddress.setError("Địa chỉ không hợp lệ");
            return false;
        }

        return true;
    }

    public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        CustomIntent.customType(PersonalInfoActivity.this, Animation.RIGHT_TO_LEFT);
        super.finish();
    }
}
