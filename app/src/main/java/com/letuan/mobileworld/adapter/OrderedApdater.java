package com.letuan.mobileworld.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.letuan.mobileworld.R;
import com.letuan.mobileworld.model.Order;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class OrderedApdater extends RecyclerView.Adapter<OrderedApdater.ItemHolder> {
    private Activity activity;
    private List<Order> list;

    public OrderedApdater(Activity activity, List<Order> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_ordered, null);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Order item = list.get(position);

        Picasso.get().load(item.getImageProduct()).placeholder(R.drawable.no_image_icon)
                .fit().centerCrop()
                .error(R.drawable.error)
                .into(holder.imgSanpham);

        holder.txtSanpham.setText(item.getNameProduct());
        holder.txtSluong.setText("x" + item.getSize() + "");
        DecimalFormat format = new DecimalFormat("###,###,###,###");
        holder.txtDgia.setText(format.format(item.getPriceProduct()) + " đ");
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
            txtSanpham = view.findViewById(R.id.txtSpham);
            txtPloai = view.findViewById(R.id.txtPLoai);
            txtSluong = view.findViewById(R.id.txtSLuong);
            txtDgia = view.findViewById(R.id.txtDgia);
            imgSanpham = view.findViewById(R.id.imgSpham);
        }
    }


//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return list.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View view, ViewGroup parent) {
//        Order item = list.get(position);
//        TextView txtSanpham, txtSluong, txtPloai, txtDgia;
//        ImageView imgSanpham;
//
//        if(view == null){
//            view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.item_ordered, null);
//            imgSanpham = view.findViewById(R.id.imgSpham);
//
//            Picasso.with(activity).load(item.getImageProduct()).placeholder(R.drawable.no_image_icon)
//                    .fit().centerCrop()
//                    .error(R.drawable.error)
//                    .into(imgSanpham);
//
//            view.setTag(imgSanpham);
//        } else {
//            imgSanpham = (ImageView) view.getTag();
//        }
//
//        txtSanpham = view.findViewById(R.id.txtSpham);
//        txtPloai= view.findViewById(R.id.txtPLoai);
//        txtSluong = view.findViewById(R.id.txtSLuong);
//        txtDgia = view.findViewById(R.id.txtDgia);
//
//        txtSanpham.setText(item.getNameProduct());
//        txtSluong.setText("x" + item.getSize() + "");
//        DecimalFormat format = new DecimalFormat("###,###,###,###");
//        txtDgia.setText(format.format(item.getPriceProduct()) + " đ");
//
//        return view;
//    }

}
