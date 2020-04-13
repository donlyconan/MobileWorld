package com.team.mobileworld.activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.team.mobileworld.R;

import maes.tech.intentanim.CustomIntent;

public class BillActivity extends AppCompatActivity {

    ImageButton btnback;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        //Anhxa phan tu
        btnback = findViewById(R.id.btn_back);







        //su kien quay tro lai
        btnback.setOnClickListener(e->{
            finish();
            CustomIntent.customType(this, Animation.RIGHT_TO_LEFT);
        });


    }
}
