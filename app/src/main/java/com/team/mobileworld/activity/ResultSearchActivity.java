package com.team.mobileworld.activity;

import android.os.Bundle;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.team.mobileworld.R;
import com.team.mobileworld.adapter.ProductAdapter;
import com.team.mobileworld.core.object.Product;

import java.util.ArrayList;
import java.util.List;

public class ResultSearchActivity extends AppCompatActivity {

    Toolbar toolbar;
    SearchView searchView;
    RecyclerView recycler;
    List<Product> products;
    ProductAdapter productadapter;

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
    }
}
