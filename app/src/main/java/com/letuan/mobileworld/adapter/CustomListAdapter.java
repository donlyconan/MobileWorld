package com.letuan.mobileworld.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.letuan.mobileworld.R;
import com.letuan.mobileworld.fragment.CustomViewFragement;
import com.letuan.mobileworld.model.GoodsReview;

import java.util.List;

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ItemHolder> {
    private Activity activity;
    private List<GoodsReview> goods;

    public CustomListAdapter(Activity activity,List<GoodsReview> goods) {
        this.activity = activity;
        this.goods = goods;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_list, null);
        ItemHolder holder = new ItemHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        GoodsReview item = goods.get(position);
        holder.fir_content.setText(item.getHeader());
        holder.recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = getLayoutManager(activity, item.getStyle());
        holder.recycler.setLayoutManager(layoutManager);

        ProductAdapter adapter = new ProductAdapter(activity, item.getList());
        holder.recycler.setAdapter(adapter);
    }

    public static RecyclerView.LayoutManager getLayoutManager(Activity activity,int style){
        switch (style)
        {
            case CustomViewFragement.VERTICAL:
                return new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false);
            case CustomViewFragement.HORIZONTAL:
                return new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false);
            case CustomViewFragement.GRID:
                return new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        }
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }

//    @Override
//    public View getView(int position, View view, ViewGroup parent) {
//        ItemHolder holder = null;
//        if(view == null)
//        {
//            view = LayoutInflater.from(context).inflate(R.layout.item_custom_list, null);
//            holder = new ItemHolder(view);
//            view.setTag(holder);
//        } else {
//            holder = (ItemHolder) view.getTag();
//        }
//        GoodsReview review = list.get(position);
//        holder.fir_content.setText(review.getHeader());
//
//        return view;
//    }

    public static class ItemHolder extends RecyclerView.ViewHolder{
        protected TextView fir_content;
        protected RecyclerView recycler;

        public ItemHolder(@NonNull View view) {
            super(view);
            fir_content = view.findViewById(R.id.txt_fir_content);
            recycler = view.findViewById(R.id.recycle_view);
        }
    }
}
