package com.letuan.mobileworld.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.letuan.mobileworld.R;

import maes.tech.intentanim.CustomIntent;

public class PersonalInfoActivity extends AppCompatActivity {

    EditText txtfullname, txtusername, txtpassword;
    EditText txtdofbirth, txtaddress, txtphonenumber, txtemail;
    Spinner spsexs;


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
        spsexs = findViewById(R.id.sp_sexs);


        final Intent intent = new Intent();

        //Kiem tra hanh dong
        if (OrderedActivity.AC_OPEN_EDIT_ADD.equals(getIntent().getAction())) {
            setResult(10, intent);
        }

        String[] sexs = new String[]{"Nam", "Nữ", "Không xác định"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sexs);
        spsexs.setAdapter(arrayAdapter);

        //Xu ly su kien
        toolbar.setNavigationOnClickListener(e -> {
            finish();
            CustomIntent.customType(PersonalInfoActivity.this, Animation.RIGHT_TO_LEFT);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Log.d("TEST", "menu");

        return super.onOptionsItemSelected(item);
    }
}
