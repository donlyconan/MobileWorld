package com.team.mobileworld.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
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
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.team.mobileworld.R;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.database.Database;
import com.team.mobileworld.core.handle.Handler;
import com.team.mobileworld.core.handle.LoginMobileWorld;
import com.team.mobileworld.core.handle.Validate;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.core.service.UserService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalInfoActivity extends AppCompatActivity {

    private static final int READ_RQ_IMAGE = 0x0011;
    private static final int READ_RQ_BIMAGE = 0x0012;
    private static final int REQUEST_ADDRESS = 0x0013;
    private static final int REQUEST_PREMISSION = 0x0014;
    private static final String TYPE_FILE = "multipart/form-data";

    EditText txtfullname, txtpassword;
    EditText txtdofbirth, txtaddress, txtphonenumber, txtemail;
    Spinner spsexs;
    TextView txtusername;
    CircleImageView imgavatar;
    ImageView imgbground;
    ProgressDialog dialog;
    ImageButton btnaddaddress;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADDRESS && resultCode == RESULT_OK) {
            txtaddress.setText(MapsActivity.Address);
        }

        if (data != null && data.getData() != null) {
            try {
                switch (requestCode) {
                    case READ_RQ_IMAGE: {
                        Uri uri = data.getData();
                        final File file = getFileFromURI(uri);

                        Log.d("userinfo", file.getAbsolutePath());
                        MainActivity.NewLogin = true;

                        String[] parts = file.getName().split("\\.");
                        String file_name = String.format("avatar_%s.%s", MainActivity.getCurrentUser().getUsername()
                                , parts[parts.length - 1]);
                        updateImage(uri, file_name);
                    }
                    break;

                    case READ_RQ_BIMAGE: {
                        Uri uri = data.getData();
                        imgbground.setImageURI(uri);
                    }
                    break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + requestCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        //Anh xa phan tu
        Toolbar toolbar = mapping();

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

        imgavatar.setOnClickListener(e -> openReplaceImage(READ_RQ_IMAGE));

        imgbground.setOnClickListener(e -> openReplaceImage(READ_RQ_BIMAGE));

        txtpassword.setOnClickListener(e -> changePassword());

        btnaddaddress.setOnClickListener(e -> onActionOpenMap());

        showInfo();
    }

    private Toolbar mapping() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        txtfullname = findViewById(R.id.txt_fullname);
        txtusername = findViewById(R.id.txt_username);
        txtpassword = findViewById(R.id.txt_password);
        txtaddress = findViewById(R.id.txt_address);
        txtdofbirth = findViewById(R.id.txt_birthofdate);
        txtemail = findViewById(R.id.txt_email);
        txtphonenumber = findViewById(R.id.txt_phonenumber);
        imgavatar = findViewById(R.id.img_profit);
        imgbground = findViewById(R.id.img_background);
        spsexs = findViewById(R.id.sp_sexs);
        btnaddaddress = findViewById(R.id.btnaddaddress);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void register() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PREMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void onActionOpenMap() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, REQUEST_ADDRESS);
    }


    public void updateImage(final Uri uri, String filename) throws IOException {

        byte[] data = readFileFromUri(uri);

        RequestBody requestfile = RequestBody.create(MediaType.parse(TYPE_FILE), data);

        MultipartBody.Part body = MultipartBody.Part.createFormData(
                "file", filename, requestfile
        );

        RequestBody requestBody = RequestBody.create(
                MediaType.parse(TYPE_FILE), MainActivity.getCurrentUser().getFullname()
        );


        Call<ResponseBody> serviceUpdate = NetworkCommon.getRetrofit()
                .create(UserService.class)
                .updateImage(MainActivity.getCurrentUser().getAccesstoken(), body, requestBody);

        //Cap nhat image
        serviceUpdate.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body() == null)
                        return;

                    //Doc json tra ve
                    JsonObject json = Handler.convertToJSon(response.body().string());

                    if (response.isSuccessful() && json.has("message")) {
                        String urlavatar = json.get("urlavatar").getAsString();
                        MainActivity.getCurrentUser().setAvatar(urlavatar);
                        Handler.loadImage(PersonalInfoActivity.this, MainActivity.getCurrentUser().getAvatar(), imgavatar);
                        playSound();
                        showToast(getBaseContext(), json.get("message").getAsString());
                    } else if (json.has("error")) {
                        showToast(getBaseContext(), json.get("error").getAsString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Log.d("userinfo", "End read!");
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public File getFileFromURI(Uri uri) {
        String path = null;

        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {

                //Lay ten file
                String filename = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)); // lay chi so cot


                int i = 0;
                for (String column : cursor.getColumnNames()) {
                    Log.d("userinfo", String.format("Att %s:%s", column, cursor.getColumnName(i++)));
                }

                //tao duong dan
                int last_index = uri.getPath().lastIndexOf('/');
                path = uri.getPath().substring(0, last_index) + '/' + filename;

                Log.i("userinfo", String.format("Display Name: %s \tPath: %s ", filename, path));
            }
        } finally {
            cursor.close();
        }
        return new File(path);
    }


    private void changePassword() {
        if (MainActivity.getCurrentUser().getLink() == User.LOGIN_ACCOUNT) {
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
                if (!Validate.validate(oldpass, Validate.REGEX_PASSWORD)) {
                    hoantat = false;
                    inpoldpass.setError("Mật khẩu không hợp lệ!");
                }
                if (!Validate.validate(newpass, Validate.REGEX_PASSWORD)) {
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
                            .changePassowrd(MainActivity.getCurrentUser().getAccesstoken(), oldpass, newpass);
                    dialog.show();
                    dialog.setMessage("Đang xử lý...");

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                if (response.isSuccessful()) {
                                    JsonObject json = Handler.convertToJSon(response.body().string());
                                    Database.print(response.body().string());
                                    if (json.has("message")) {
                                        playSound();
                                        showToast(getApplicationContext(), json.get("message").getAsString());
                                    } else
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
            showToast(getBaseContext(), MainActivity.getCurrentUser().statusLogin());

    }

    private TransformationMethod show(TransformationMethod transfor) {
        if (transfor == HideReturnsTransformationMethod.getInstance())
            return PasswordTransformationMethod.getInstance();
        else
            return HideReturnsTransformationMethod.getInstance();
    }


    private byte[] readFileFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        //mo bo nho doc file
        byte[] data = new byte[(int) parcelFileDescriptor.getStatSize()];
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        FileInputStream fileInputStream = new FileInputStream(fileDescriptor);

        //doc du lieu vao data
        fileInputStream.read(data);
        fileInputStream.close();
        parcelFileDescriptor.close();

        return data;
    }


    public void openReplaceImage(final int mode) {
        //dang ky quyen su dung
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            register();
        }

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        //Loc file
        intent.setType("image/*");
        startActivityForResult(intent, mode);
    }

    private void showInfo() {

        User user = MainActivity.getCurrentUser();

        if (user != null) {
            Picasso.get().load(user.getAvatar())
                    .placeholder(R.drawable.ic_profit)
                    .error(R.drawable.ic_profit)
                    .fit().into(imgavatar);
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

    public void playSound() {
        MediaPlayer player = MediaPlayer.create(this, R.raw.notify_sound);
        player.start();
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

            User user = MainActivity.getCurrentUser();
            user.setFullname(fullname);
            user.setAddress(address);
            user.setEmail(email);
            user.setGender(gender);
            user.setPnumber(std);
            if (validate()) {
                dialog.show();

                UserService service = NetworkCommon.getRetrofit().create(UserService.class);

                Call<User> call = service.updatePersonalInfo(user.getAccesstoken(), user);

                Database.print("Rquest: " + call.request());

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        if (response.isSuccessful()) {
                            playSound();
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

        if (!Validate.validate(email, Validate.REGEX_EMAIL)) {
            txtemail.setError("Email không hợp lệ");
            return false;
        }
        if (!Validate.validate(std, Validate.REGEX_PHONE_NUMBER)) {
            txtphonenumber.setError("Số điện thoại không hợp lệ");
            return false;
        }
        if (!Validate.validate(address, Validate.REGEX_ADDRESS)) {
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
