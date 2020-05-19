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

import com.team.mobileworld.R;
import com.team.mobileworld.activity.MainActivity;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.database.Database;
import com.team.mobileworld.core.handle.Handler;
import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.core.service.BasketService;

import java.text.DecimalFormat;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasketAdapter extends BaseAdapter {
    Activity activity;
    List<Order> orderList;
    private int size = 0;
    private Long sum = 0l;
    private Call<ResponseBody> call;

    public BasketAdapter(Activity activity, List<Order> orderList) {
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
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Handler.loadImage(activity, order.getImage(), holder.imgOrder);

//        Picasso.get().load(order.getImage()).placeholder(R.drawable.no_image_icon)
//                .fit().centerCrop()
//                .error(R.drawable.error).into(holder.imgOrder);

        final TextView txtMoney = activity.findViewById(R.id.txtThanhTien);
        CheckBox ckSelALL = activity.findViewById(R.id.ckSelAll);

        holder.txtNameOrder.setText(order.getName());
        holder.txtPriceOrder.setText(formatMoney(order.getTotalMoney()));
        holder.txtValue.setText(order.getAmount() + "");
        holder.ckSel.setChecked(order.isSelect());

        holder.ckSel.setOnCheckedChangeListener((b, e) -> {
            orderList.get(position).setSelect(e);

            //Đếm tất cả sản phẩm đc chọn
            int count = (int) orderList.stream().filter(Order::isSelect).count();

            //Cài đặt
            ckSelALL.setChecked((count == orderList.size()));
            txtMoney.setText(Handler.getFormatTotalMoneySelected(orderList));
        });

        holder.btnPlus.setOnClickListener(e -> {
            holder.btnMinus.setEnabled(true);

            order.setAmount(order.getAmount() + 1);
            update(MainActivity.getCurrentUser().getAccesstoken(), order.getId(), +1);
            holder.txtValue.setText(order.getAmount() + "");
            holder.txtPriceOrder.setText(formatMoney(order.getTotalMoney()));

            txtMoney.setText(Handler.getFormatTotalMoneySelected(orderList));

            if (order.getAmount() > order.getSlmax())
                holder.btnPlus.setEnabled(false);
        });

        holder.btnMinus.setOnClickListener(e -> {
            if (order.getAmount() <= 1)
                return;
            holder.btnPlus.setEnabled(true);
            order.setAmount(order.getAmount() - 1);
            holder.txtValue.setText(order.getAmount() + "");
            update(MainActivity.getCurrentUser().getAccesstoken(), order.getId(), -1);
            holder.txtPriceOrder.setText(formatMoney(order.getTotalMoney()));
            txtMoney.setText(Handler.getFormatTotalMoneySelected(orderList));

            if (order.getAmount() < 2)
                holder.btnMinus.setEnabled(false);
        });
        return view;
    }

    public void update(String token, int catalogid, final int amount) {
        if (MainActivity.getCurrentUser().isLogin()) {
            call = NetworkCommon.getRetrofit().create(BasketService.class)
                    .addOrder(token, catalogid, amount);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        } else {
            Database db = MainActivity.getDatabaseInstence();
            db.updateUnit(catalogid, amount);
        }
    }


    public static String formatMoney(long money) {
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

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }

    public Call<ResponseBody> getCall() {
        return call;
    }

    public void setCall(Call<ResponseBody> call) {
        this.call = call;
    }
}

