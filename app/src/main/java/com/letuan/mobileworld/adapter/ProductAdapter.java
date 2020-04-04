package com.letuan.mobileworld.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.letuan.mobileworld.R;
import com.letuan.mobileworld.activity.Animation;
import com.letuan.mobileworld.activity.MainActivity;
import com.letuan.mobileworld.activity.ProductDetail;
import com.letuan.mobileworld.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ItemHolder> {

    Activity activity;
    List<Product> productList;

    public ProductAdapter(Activity activity, List<Product> productList) {
        this.activity = activity;
        this.productList = productList;
    }

    //khoi tao lai view ma da thiet ke layout o ben ngoai
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productnewest, null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    //set va get thuoc tinh gan len cho layout
    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtNameProduct.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtPriceProduct.setText("Giá : " + decimalFormat.format(product.getPrice()) + " Đ");
        Picasso.with(activity.getBaseContext()).load(product.getImage()).fit().centerCrop().placeholder(R.drawable.no_image_icon)
                .error(R.drawable.error).into(holder.imgProduct);

        holder.view.setOnClickListener(e->{
            Intent intent = new Intent(activity, ProductDetail.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(MainActivity.MAIN_OPEN_ORDER);
            intent.putExtra("thongtin", productList.get(position));
            activity.startActivityForResult(intent, ProductDetail.REPLACE_SIZE);
            CustomIntent.customType(activity, Animation.LEFT_TO_RIGHT);
         });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public View view;
        public ImageView imgProduct;
        public TextView txtNameProduct, txtPriceProduct;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            imgProduct = itemView.findViewById(R.id.imgproduct);
            txtNameProduct = itemView.findViewById(R.id.txtproductname);
            txtPriceProduct = itemView.findViewById(R.id.txtproductprice);
        }
    }
}
