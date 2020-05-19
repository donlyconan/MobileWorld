package com.team.mobileworld.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Trace;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.gson.Gson;
import com.team.mobileworld.R;
import com.team.mobileworld.adapter.ProductAdapter;
import com.team.mobileworld.adapter.SuggestAdapter;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.object.Product;
import com.team.mobileworld.core.service.SearchService;
import com.team.mobileworld.core.task.OnItemClickListener;
import com.team.mobileworld.fragment.LoadFragement;
import com.team.mobileworld.fragment.SearchViewFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import maes.tech.intentanim.CustomIntent;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchActivity extends AppCompatActivity implements OnItemClickListener {

    SearchView searchView;
    ImageButton btnback, btnfilter;
    LoadFragement fragload;
    SearchViewFragment fragsearch;

    ProductAdapter adapter;
    SuggestAdapter suggestAdapter;
    ProgressBar progress;
    Call<List<Product>> result;
    Call<ResponseBody> suggest;
    List<String> suggesttext;
    List<Product> products;
    int status = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);

        //Anh xa phan tu
        elementMapping();

        init();

        btnback.setOnClickListener(e -> finish());
        onActionSearchView();
    }

    private void elementMapping() {
        searchView = findViewById(R.id.search_bar);
        btnback = findViewById(R.id.btn_back);
        btnfilter = findViewById(R.id.btn_filter);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        products = new ArrayList<>(50);
        suggesttext = new ArrayList<>(10);
        adapter = new ProductAdapter(this, products);
        suggestAdapter = new SuggestAdapter(this, suggesttext);

        fragload = new LoadFragement();
        fragsearch = new SearchViewFragment(fragload, adapter, suggestAdapter);
        fragsearch.onAttach(this);

        adapter.setOnItemClickListener(this::onItemClick);
        suggestAdapter.setOnItemClickListener(this::onItemClick);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_searchview, fragsearch).commit();
    }

    private void onActionSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadResult(query, 1, 20);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() > 1)
                    loadSuggest(newText);
                return true;
            }
        });
    }


    public void loadSuggest(String text) {
        if (status == 1) {
            changeFragSearch();
            fragsearch.showSuggestQuery(suggesttext);
        }

        fragsearch.getTxtresult().getLayoutParams()
                .height = 0;

        if(suggest != null)
            synchronized (suggest){suggest.cancel();}

        SearchService service = NetworkCommon.getRetrofit().create(SearchService.class);

        suggest = service.suggestQuery(text);

        suggest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Gson gson = new Gson();
                        suggesttext.clear();
                        suggesttext.addAll(gson.fromJson(response.body().string(), List.class));
                        fragsearch.showSuggestQuery(suggesttext);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void loadResult(String text, int offset, int limit) {
        if (result != null)
            result.cancel();

        if(suggest != null)
            synchronized (suggest){suggest.cancel();}

        fragload.setWorker(() -> {
            changeFragSearch();
            fragload.action();
        });

        SearchService service = NetworkCommon.getRetrofit().create(SearchService.class);

        Map<String, String> map = new HashMap<>();
        map.put("keyword", text);
        map.put("offset", offset + "");
        map.put("limit", limit + "");

        result = service.search(map);

        result.enqueue(new Callback<List<Product>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                products = response.body();

                if (response.isSuccessful() && products != null) {
                    adapter.setProductList(products);
                    fragsearch.getTxtresult()
                            .getLayoutParams()
                            .height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    fragsearch.showSearchQuery(products);
                    status = 3;
                    changeFragSearch();
                } else {
                    changeFragLoad();
                    fragload.error();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                changeFragLoad();
                fragload.error();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position, int id) {
        if(fragsearch.getRecycler().getAdapter() instanceof SuggestAdapter){
            String text = fragsearch.getSuggestAdapter()
                    .getList().get(position);
            searchView.setQuery(text, false);
            loadResult(text,1,20);
        }


    }

    public void changeFragLoad() {
        status = 1;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_searchview, fragload)
                .commitNowAllowingStateLoss();
    }

    public void changeFragSearch() {
        status = 0;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_searchview, fragsearch)
                .commitNowAllowingStateLoss();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        CustomIntent.customType(this, Animation.LEFT_TO_RIGHT);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        CustomIntent.customType(this, Animation.RIGHT_TO_LEFT);
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, Animation.LEFT_TO_RIGHT);
    }


}
