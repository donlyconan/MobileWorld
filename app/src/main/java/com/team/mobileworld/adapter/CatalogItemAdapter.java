package com.team.mobileworld.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.team.mobileworld.R;
import com.team.mobileworld.activity.MainActivity;
import com.team.mobileworld.activity.ProductDetail;
import com.team.mobileworld.core.object.CatalogItem;
import com.team.mobileworld.core.object.Product;
import com.team.mobileworld.core.task.OnItemClickListener;
import com.team.mobileworld.core.task.ViewHolder;
import com.team.mobileworld.fragment.CustomViewFragement;

import java.util.List;

public class CatalogItemAdapter extends RecyclerView.Adapter<CatalogItemAdapter.ItemHolder> {
    private Activity activity;
    private List<CatalogItem> goods;
    private OnItemClickListener onItemClickListener;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CatalogItemAdapter(Activity activity, List<CatalogItem> goods) {
        this.activity = activity;
        this.goods = goods;
        goods.removeIf(e->e.getProducts()==null || e.getProducts().size() == 0);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_list, null);
        ItemHolder holder = new ItemHolder(v);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        CatalogItem item = goods.get(position);
        holder.fir_content.setText(item.getTitle().toUpperCase());

//        Database.print("title=" + item.getTitle() + "\t\tsize=" + item.getProducts().size());
        holder.recycler.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = getLayoutManager(activity, item.getMode());
        holder.recycler.setLayoutManager(layoutManager);

        ProductAdapter adapter = new ProductAdapter(activity, item.getProducts());
        adapter.setOnItemClickListener(onItemClickListener);
        holder.recycler.setAdapter(adapter);
    }



    public static RecyclerView.LayoutManager getLayoutManager(Activity activity, int style) {
        switch (style) {
            case CustomViewFragement.VERTICAL:
                return new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            case CustomViewFragement.HORIZONTAL:
                return new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            case CustomViewFragement.GRID:
                return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }
        return null;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }


    public static class ItemHolder extends RecyclerView.ViewHolder {
        protected TextView fir_content;
        protected RecyclerView recycler;

        public ItemHolder(@NonNull View view) {
            super(view);
            fir_content = view.findViewById(R.id.txt_fir_content);
            recycler = view.findViewById(R.id.recycle_view);
        }
    }
}
