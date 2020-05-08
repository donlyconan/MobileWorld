package com.team.mobileworld.fragment;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.team.mobileworld.R;
import com.team.mobileworld.activity.ResultSearchActivity;
import com.team.mobileworld.adapter.ProductAdapter;
import com.team.mobileworld.adapter.SuggestAdapter;
import com.team.mobileworld.core.object.Product;
import com.team.mobileworld.core.task.OnItemClickListener;

import java.util.Arrays;
import java.util.List;

public class SearchViewFragment extends Fragment{

    LoadFragement fragload;
    ResultSearchActivity activity;
    ProductAdapter adapter;
    SuggestAdapter suggestAdapter;
    RecyclerView recycler;
    TextView txtresult;

    public SearchViewFragment(LoadFragement fragload, ProductAdapter adapter, SuggestAdapter suggestAdapter) {
        this.fragload = fragload;
        this.adapter = adapter;
        this.suggestAdapter = suggestAdapter;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onAttach(@NonNull Activity activity) {
        this.activity = (ResultSearchActivity) activity;
        super.onAttach(activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_result_search, null);
        recycler = view.findViewById(R.id.recycle_view);
        txtresult = view.findViewById(R.id.txt_result);
        txtresult.setText("Lọc kết quả 0.");

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        suggestAdapter.getList().clear();
        txtresult.getLayoutParams().height = 0;
        Arrays.asList("IPhone", "Samsung", "OPPO", "Vivo", "Xiaomi")
                .forEach(suggestAdapter.getList()::add);
        recycler.setAdapter(suggestAdapter);

        return view;
    }


    public void showSuggestQuery(List<String> suggest) {
        suggestAdapter.setList(suggest);
        recycler.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recycler.setAdapter(suggestAdapter);
    }

    public void showSearchQuery(List<Product> products) {
        adapter.setProductList(products);
        recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        txtresult.setText(String.format("Kết quả tìm kiếm: %d sản phẩm", products.size()));
        recycler.setAdapter(adapter);
    }



    public ProductAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ProductAdapter adapter) {
        this.adapter = adapter;
    }

    public SuggestAdapter getSuggestAdapter() {
        return suggestAdapter;
    }

    public TextView getTxtresult() {
        return txtresult;
    }

    public void setSuggestAdapter(SuggestAdapter suggestAdapter) {
        this.suggestAdapter = suggestAdapter;
    }

    public RecyclerView getRecycler() {
        return recycler;
    }


}
