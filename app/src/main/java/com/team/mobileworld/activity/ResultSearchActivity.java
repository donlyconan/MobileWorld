package com.team.mobileworld.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.team.mobileworld.R;
import com.team.mobileworld.adapter.ProductAdapter;
import com.team.mobileworld.core.object.Product;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class ResultSearchActivity extends AppCompatActivity {

    Toolbar toolbar;
    SearchView searchView;
    RecyclerView recycler;
    List<Product> products;
    ProductAdapter productadapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);

        //Anh xa phan tu
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.search_bar);
        recycler = findViewById(R.id.recycle_view);


        //khởi tạo danh sách sản phẩm
        products = new ArrayList<>();

        //khởi tạo adapter
        productadapter = new ProductAdapter(this, products);
        recycler.setLayoutManager(new GridLayoutManager(this,2));
        recycler.setAdapter(productadapter);

        onActionSearchView();
    }

    private void onActionSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                return true;
            }
        });
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
