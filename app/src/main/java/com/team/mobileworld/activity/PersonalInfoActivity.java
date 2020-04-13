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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;
import com.team.mobileworld.R;
import com.team.mobileworld.core.object.User;

import java.io.FileDescriptor;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;

public class PersonalInfoActivity extends AppCompatActivity {

    private static final int READ_RQ_IMAGE = 150;
    private static final int READ_RQ_BIMAGE = 151;
    EditText txtfullname, txtusername, txtpassword;
    EditText txtdofbirth, txtaddress, txtphonenumber, txtemail;
    Spinner spsexs;
    CircleImageView imgprofit;
    ImageView imgbground;
    final Intent intent = new Intent();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case READ_RQ_IMAGE: {
                try {
                    Uri uri = data.getData();
                    imgprofit.setImageBitmap(getBitmapFromUri(uri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } break;

            case READ_RQ_BIMAGE:{
                try {
                    Uri uri = data.getData();
                    imgbground.setImageBitmap(getBitmapFromUri(uri));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } break;
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
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sexs);
        spsexs.setAdapter(arrayAdapter);

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

        imgprofit.setOnClickListener(e->openReplaceImage(READ_RQ_IMAGE));

        imgbground.setOnClickListener(e->openReplaceImage(READ_RQ_BIMAGE));

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

    public void openReplaceImage(final  int mode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        //Loc file
        intent.setType("image/*");
        startActivityForResult(intent, mode);
    }

    private void showInfo() {
        User user = MainActivity.user;

        Picasso.get().load(user.getProfit()).error(R.drawable.ic_profit)
                .fit().into(imgprofit);
//        txtfullname.setText(user.getFullname());
//        txtusername.setText(user.getAcc().getUsername());
//        txtaddress.setText(user.getAddress());
//        txtdofbirth.setText(user.getBdate());
//        txtemail.setText(txtemail.getText());
//        txtphonenumber.setText(txtphonenumber.getText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
