package com.letuan.mobileworld.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.letuan.mobileworld.R;
import com.letuan.mobileworld.activity.MainActivity;
import com.letuan.mobileworld.activity.OrderActivity;
import com.letuan.mobileworld.model.Order;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class OrderAdapter extends BaseAdapter {
    Activity activity;
    List<Order> orderList;
    private int size = 0;
    private Long sum = 0l;

    public OrderAdapter(Activity activity, List<Order> orderList) {
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
            view = inflater.inflate(R.layout.item_order, null);
            holder = new ViewHolder(view);

            Picasso.with(activity).load(order.getImageProduct()).placeholder(R.drawable.no_image_icon)
                    .fit().centerCrop()
                    .error(R.drawable.error).into(holder.imgOrder);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final TextView txtMoney = activity.findViewById(R.id.txtThanhTien);
        CheckBox ckSelALL = activity.findViewById(R.id.ckSelAll);

        holder.txtNameOrder.setText(order.getNameProduct());
        holder.txtPriceOrder.setText(formatMoney(order.getTongSoTien()));
        holder.txtValue.setText(order.getSize() + "");
        holder.ckSel.setChecked(order.isSel());

        holder.ckSel.setOnCheckedChangeListener((b, e) ->{
            orderList.get(position).setSel(e);
            boolean ckall = true;
            for(Order item : orderList)
            {
                ckall &= item.isSel();
                if(ckall == false) break;
            }
            ckSelALL.setChecked(ckall);
            txtMoney.setText(OrderActivity.updateGoods());
        } );

        holder.btnPlus.setOnClickListener(e -> {
            holder.btnMinus.setEnabled(true);
            order.setSize(order.getSize() + 1);
            holder.txtValue.setText(order.getSize() + "");
            holder.txtPriceOrder.setText(formatMoney(order.getTongSoTien()));

            txtMoney.setText(OrderActivity.updateGoods());

            if (order.getSize() > 9)
                holder.btnPlus.setEnabled(false);
        });

        holder.btnMinus.setOnClickListener(e -> {
            holder.btnPlus.setEnabled(true);
            order.setSize(order.getSize() - 1);
            holder.txtValue.setText(order.getSize() + "");
            holder.txtPriceOrder.setText(formatMoney(order.getTongSoTien()));
            txtMoney.setText(OrderActivity.updateGoods());

            if (order.getSize() < 2)
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

// size = order.getSize();
//         order.setSize(size + 1);
//         holder.btnMinus.setEnabled(true);
//         sum = (order.getPriceProduct() / size) * order.getSize();
//         order.setPriceProduct(sum);
//         DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
//         holder.txtPriceOrder.setText(decimalFormat.format(sum) + " Đ");
//         holder.btnValue.setText(order.getSize() + "");
//         OrderActivity.eventUltil();
//         if (size > 8) {
//         holder.btnPlus.setEnabled(false);
//         }
//         Order order = (Order) getItem(position);
//         size = order.getSize();
//         order.setSize(size - 1);
//         holder.btnPlus.setEnabled(true);
//         sum = (order.getPriceProduct() / size) * order.getSize();
//         order.setPriceProduct(sum);
//         DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
//         holder.txtPriceOrder.setText(decimalFormat.format(sum) + " Đ");
//         holder.btnValue.setText(order.getSize() + "");
//         OrderActivity.eventUltil();
//         if (size < 3) {
//        holder.btnMinus.setEnabled(false);
//        }
//        }
