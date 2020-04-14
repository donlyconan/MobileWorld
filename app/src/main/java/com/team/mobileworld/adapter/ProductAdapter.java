package com.team.mobileworld.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.team.mobileworld.R;
import com.team.mobileworld.activity.Animation;
import com.team.mobileworld.activity.MainActivity;
import com.team.mobileworld.activity.ProductDetail;
import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.core.object.Product;

import java.text.DecimalFormat;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ItemHolder> {

    MainActivity activity;
    List<Product> productList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ProductAdapter(Activity activity, List<Product> productList) {
        this.activity = (MainActivity) activity;
        this.productList = productList;
        productList.removeIf(e->e.getSlmax() <= 0);
    }

    //khoi tao lai view ma da thiet ke layout o ben ngoai
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productnewest, null);
        ItemHolder holder = new ItemHolder(v);

        ImageView img = v.findViewById(R.id.imgproduct);
        img.getLayoutParams().height = parent.getMeasuredWidth()/2 - 5;
        img.getLayoutParams().width = parent.getMeasuredWidth()/2-5;
        return holder;
    }

    //set va get thuoc tinh gan len cho layout
    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtNameProduct.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtPriceProduct.setText("Giá : " + decimalFormat.format(product.getPrice()) + " Đ");

        //Load anh tu URL
        Picasso.get().load(product.getImage())
                .placeholder(R.drawable.no_image_icon)
                .error(R.drawable.error)
                .fit()
                .into(holder.imgProduct);


        //Chay activity thong tin chi tiet san pham
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
