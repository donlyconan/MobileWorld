package com.team.mobileworld.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.team.mobileworld.R;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.handle.APIhandler;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.core.service.UserService;

import java.io.FileDescriptor;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;
import okhttp3.Address;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class PersonalInfoActivity extends AppCompatActivity {

    private static final int READ_RQ_IMAGE = 150;
    private static final int READ_RQ_BIMAGE = 151;
    EditText txtfullname, txtpassword;
    EditText txtdofbirth, txtaddress, txtphonenumber, txtemail;
    Spinner spsexs;
    TextView txtusername;
    CircleImageView imgprofit;
    ImageView imgbground;
    final Intent intent = new Intent();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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


        //Cai dat spiner
        final String[] sexs = new String[]{"Nam", "Nữ", "Không xác định"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.spinner_custom, sexs);
        spsexs.setAdapter(arrayAdapter);
        spsexs.setSelection(0);

        //Xu ly su kien
        toolbar.setNavigationOnClickListener(e -> {

            //Kiem tra hanh dong
            if (Intent.ACTION_EDIT.equals(getIntent().getAction())) {
                //Cai dat du lieu cho intent
                intent.putExtra("address", txtaddress.getText());
                setResult(10, intent);
            }

            finish();
            CustomIntent.customType(PersonalInfoActivity.this, Animation.RIGHT_TO_LEFT);
        });

        imgprofit.setOnClickListener(e -> openReplaceImage(READ_RQ_IMAGE));

        imgbground.setOnClickListener(e -> openReplaceImage(READ_RQ_BIMAGE));

        showInfo();
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

        if(user != null)
        {
            Picasso.get().load(user.getProfit()).error(R.drawable.ic_profit)
                    .fit().into(imgprofit);
            txtfullname.setText(get(user.getFullname()));
            txtaddress.setText(get(user.getAddress()));
            txtdofbirth.setText(get(user.getBdate()));
            txtemail.setText(get(user.getEmail()));
            txtphonenumber.setText(get(user.getPnumber()));
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

        UserService service = NetworkCommon.getRetrofit().create(UserService.class);

        Call<ResponseBody> call = service.updatePersonalInfo(user);

        try {
            JsonObject json = APIhandler.convertToJSon(call.execute().body().string());
            if(json.has(MainActivity.MESSAGE)){
                Toast.makeText(this.getBaseContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
            }
            if(json.has(MainActivity.ERROR))
                Toast.makeText(this.getBaseContext(), "Cập nhật không thành công: lỗi " + json.get(MainActivity.ERROR), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.onOptionsItemSelected(item);
    }
}
