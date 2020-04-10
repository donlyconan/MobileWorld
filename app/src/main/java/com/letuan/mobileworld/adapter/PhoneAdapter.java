package com.letuan.mobileworld.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.letuan.mobileworld.R;
import com.letuan.mobileworld.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class PhoneAdapter extends BaseAdapter {
    Context context;
    List<Product> productList;

    public PhoneAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        TextView txtPhoneName, txtPhonePrice, txtPhoneDescription;
        ImageView imgPhone;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_phone, null);
            viewHolder.txtPhoneName = view.findViewById(R.id.txtphonename);
            viewHolder.txtPhonePrice = view.findViewById(R.id.txtphoneprice);
            viewHolder.txtPhoneDescription = view.findViewById(R.id.txtphonedescription);
            viewHolder.imgPhone = view.findViewById(R.id.imgphone);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Product product = (Product) getItem(position);
        viewHolder.txtPhoneName.setText(product.getName());
        viewHolder.txtPhoneDescription.setMaxLines(2);
        viewHolder.txtPhoneDescription.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtPhoneDescription.setText(product.getDescription());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtPhonePrice.setText("Giá: " + decimalFormat.format(product.getPrice()) + " Đ");
        Picasso.get().load(product.getImage()).placeholder(R.drawable.no_image_icon)
                .error(R.drawable.error).into(viewHolder.imgPhone);
        return view;
    }
}
