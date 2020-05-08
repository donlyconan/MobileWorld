package com.team.mobileworld.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.team.mobileworld.R;
import com.team.mobileworld.activity.MainActivity;
import com.team.mobileworld.activity.ProductDetail;
import com.team.mobileworld.core.handle.Handler;
import com.team.mobileworld.core.object.Product;
import com.team.mobileworld.core.task.OnItemClickListener;
import com.team.mobileworld.core.task.ViewHolder;
import com.team.mobileworld.core.task.Worker;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ItemHolder> {
    private OnItemClickListener listener;
    private Activity activity;
    private List<Product> productList;
    private Worker worker;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ProductAdapter(Activity activity, List<Product> productList) {
        this.activity = activity;
        this.productList = productList;
    }


    //khoi tao lai view ma da thiet ke layout o ben ngoai
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productnewest, null);
        ItemHolder holder = new ItemHolder(v);
        return holder;
    }

    //set va get thuoc tinh gan len cho layout
    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtNameProduct.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtPriceProduct.setText("Giá : " + decimalFormat.format(product.getPrice()) + " đ");

        //Load anh tu URL
        Handler.loadImage(activity, product.getImage(), holder.imgProduct);

        //Chay activity thong tin chi tiet san pham
        holder.view.setOnClickListener(e->{
            Intent intent = new Intent(activity, ProductDetail.class);
            intent.setAction(MainActivity.MAIN_OPEN_ORDER);
            intent.putExtra(ProductDetail.SAN_PHAM, product);
            activity.startActivityForResult(intent, ProductDetail.REQUEST_SIZE_AMOUNT);
        });
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public class ItemHolder extends ViewHolder {
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
