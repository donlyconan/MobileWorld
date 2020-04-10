package com.letuan.mobileworld.activity;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.letuan.mobileworld.R;

import maes.tech.intentanim.CustomIntent;

public class BillActivity extends AppCompatActivity {

    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        //Anhxa phan tu
        toolbar = findViewById(R.id.toolbar);









        //xu ly su kien
        toolbar.setNavigationOnClickListener(e->{
           finish();
            CustomIntent.customType(this, Animation.RIGHT_TO_LEFT);
        });
    }
}
