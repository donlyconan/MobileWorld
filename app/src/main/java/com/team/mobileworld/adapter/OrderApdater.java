package com.team.mobileworld.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.squareup.picasso.Picasso;
import com.team.mobileworld.R;
import com.team.mobileworld.core.object.Order;

import java.text.DecimalFormat;
import java.util.List;

public class OrderApdater extends RecyclerView.Adapter<OrderApdater.ItemHolder> {
    private Activity activity;
    private List<Order> list;

    public OrderApdater(Activity activity, List<Order> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_order, null);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Order item = list.get(position);

        Picasso.get().load(item.getImage()).placeholder(R.drawable.no_image_icon)
                .fit().centerCrop()
                .error(R.drawable.error)
                .into(holder.imgSanpham);


        holder.txtSanpham.setText(item.getName());
        holder.txtSluong.setText("x" + item.getAmount() + "");
        DecimalFormat format = new DecimalFormat("###,###,###,###");
        holder.txtDgia.setText(format.format(item.getPrice()) + " đ");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        TextView txtSanpham, txtSluong, txtPloai, txtDgia;
        ImageView imgSanpham;

        public ItemHolder(@NonNull View view) {
            super(view);
            txtSanpham = view.findViewById(R.id.txtproductname);
            txtPloai = view.findViewById(R.id.txttype);
            txtSluong = view.findViewById(R.id.txtamount);
            txtDgia = view.findViewById(R.id.txtprice);
            imgSanpham = view.findViewById(R.id.imgSpham);
        }
    }

}
