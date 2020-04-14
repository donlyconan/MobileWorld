package com.team.mobileworld.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.team.mobileworld.R;
import com.team.mobileworld.core.handle.APIhandler;
import com.team.mobileworld.core.object.Order;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends BaseAdapter {
    Activity activity;
    List<Order> orderList;
    private int size = 0;
    private Long sum = 0l;

    public CartAdapter(Activity activity, List<Order> orderList) {
        this.activity = activity;
        this.orderList = orderList;

    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNameOrder, txtPriceOrder;
        public ImageView imgOrder;
        public CheckBox ckSel;
        public Button btnMinus, btnPlus;
        public TextView txtValue;

        public ViewHolder(@NonNull View view) {
            super(view);
            txtNameOrder = view.findViewById(R.id.txtnameorder);
            txtPriceOrder = view.findViewById(R.id.txtpriceorder);
            imgOrder = view.findViewById(R.id.imgorder);
            btnMinus = view.findViewById(R.id.btnminus);
            btnPlus = view.findViewById(R.id.btnplus);
            txtValue = view.findViewById(R.id.txtnp);
            ckSel = view.findViewById(R.id.ckselect);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        final Order order = (Order) getItem(position);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_cart, null);
            holder = new ViewHolder(view);

            Picasso.get().load(order.getImage()).placeholder(R.drawable.no_image_icon)
                    .fit().centerCrop()
                    .error(R.drawable.error).into(holder.imgOrder);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final TextView txtMoney = activity.findViewById(R.id.txtThanhTien);
        CheckBox ckSelALL = activity.findViewById(R.id.ckSelAll);

        holder.txtNameOrder.setText(order.getName());
        holder.txtPriceOrder.setText(formatMoney(order.getTotalMoney()));
        holder.txtValue.setText(order.getAmount() + "");
        holder.ckSel.setChecked(order.isSelect());

        holder.ckSel.setOnCheckedChangeListener((b, e) ->{
            orderList.get(position).setSelect(e);

            //Đếm tất cả sản phẩm đc chọn
            int count = (int) orderList.stream().filter(Order::isSelect).count();

            //Cài đặt
            ckSelALL.setChecked((count == orderList.size()));
            txtMoney.setText(APIhandler.getFormatTotalMoneySelected(orderList));
        } );

        holder.btnPlus.setOnClickListener(e -> {
            holder.btnMinus.setEnabled(true);
            order.setAmount(order.getAmount() + 1);
            holder.txtValue.setText(order.getAmount() + "");
            holder.txtPriceOrder.setText(formatMoney(order.getTotalMoney()));

            txtMoney.setText(APIhandler.getFormatTotalMoneySelected(orderList));

            if (order.getAmount() > 9)
                holder.btnPlus.setEnabled(false);
        });

        holder.btnMinus.setOnClickListener(e -> {
            holder.btnPlus.setEnabled(true);
            order.setAmount(order.getAmount() - 1);
            holder.txtValue.setText(order.getAmount() + "");
            holder.txtPriceOrder.setText(formatMoney(order.getTotalMoney()));
            txtMoney.setText(APIhandler.getFormatTotalMoneySelected(orderList));

            if (order.getAmount() < 2)
                holder.btnMinus.setEnabled(false);

        });
        return view;
    }

    public static String formatMoney(long money){
        final DecimalFormat format = new DecimalFormat("###,###,###");
        return format.format(money) + " đ";
    }

    public Context getContext() {
        return activity;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public int getSize() {
        return size;
    }

    public Long getSum() {
        return sum;
    }

}

