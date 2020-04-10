package com.letuan.mobileworld.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toolbar;

import com.letuan.mobileworld.R;

public class ResultSearchActivity extends AppCompatActivity {

    Toolbar toolbar;
    SearchView searchView;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);

        //Anh xa phan tu
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.search_bar);
        recycler = findViewById(R.id.recycle_view);

    }
}
