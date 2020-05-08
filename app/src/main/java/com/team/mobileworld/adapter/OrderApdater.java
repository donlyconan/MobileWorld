package com.team.mobileworld.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.team.mobileworld.R;
import com.team.mobileworld.activity.LoginActivity;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.handle.Handler;
import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.core.service.BillService;
import com.team.mobileworld.core.task.ViewHolder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.team.mobileworld.R.drawable.shape_black_10dp;
import static com.team.mobileworld.R.drawable.shape_color10dp;

public class OrderApdater extends RecyclerView.Adapter<OrderApdater.ItemHolder> {
    public static final int ITEM_ORDER_VIEW = 200;
    public static final int ITEM_BILL_VIEW = 300;

    private Activity activity;
    private List<Order> list;
    private int typeview = ITEM_ORDER_VIEW;
    private Call<ResponseBody> call = null;


    public OrderApdater(Activity activity, List<Order> list) {
        this.activity = activity;
        this.list = list;
    }

    public OrderApdater(Activity activity, List<Order> list, int typeview) {
        this.activity = activity;
        this.list = list;
        this.typeview = typeview;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_delivery, null);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Order item = list.get(position);

        Handler.loadImage(activity, item.getImage(), holder.imgSanpham);
//        Picasso.get().load(item.getImage()).placeholder(R.drawable.no_image_icon)
//                .fit().centerCrop()
//                .error(R.drawable.error)
//                .into(holder.imgSanpham);

        holder.txtSanpham.setText(item.getName());
        holder.txtSluong.setText("x" + item.getAmount() + "");
        DecimalFormat format = new DecimalFormat("###,###,###,###");
        holder.txtDgia.setText(format.format(item.getPrice()) + " đ");
        holder.txttonggia.setText("Tổng tiền: " + format.format(item.getTotalMoney()) + " đ");

        if (typeview == ITEM_BILL_VIEW) {
            holder.txttrangthai.setText(item.currentStatus());
            holder.btncancle.setOnClickListener(e -> update(item.getId(), item.getStatus(), position));
            Log.d("tage", "status=" + item.getStatus());

            if (item.getStatus() == Order.STATUS_RECEIVE || item.getStatus() == Order.STATUS_DELIVRY)
                holder.btncancle.setText("Đã nhận");

            if (item.getStatus() == Order.STATUS_CONFIRM || item.getStatus() == Order.STATUS_CANCLE)
                holder.btncancle.setText("Huỷ");

            if (item.getStatus() == Order.STATUS_RECEIVE || item.getStatus() == Order.STATUS_CANCLE) {
                holder.btncancle.setEnabled(false);
                holder.btncancle.setBackgroundResource(shape_black_10dp);
            }
            if (item.getStatus() == Order.STATUS_CONFIRM || item.getStatus() == Order.STATUS_DELIVRY) {
                holder.btncancle.setEnabled(true);
                holder.btncancle.setBackgroundResource(shape_color10dp);
            }
        } else {
            holder.constraintLayout.getLayoutParams().height = 0;
        }
    }

    public void update(int idbill, int status, int local) {
        int statusChange = -1;
        if (status == Order.STATUS_CONFIRM || status == Order.STATUS_DELIVRY) {
            statusChange = (status == Order.STATUS_CONFIRM ? Order.STATUS_CANCLE : Order.STATUS_RECEIVE);
            call = NetworkCommon.getRetrofit().create(BillService.class)
                    .updateStatusOrder(idbill, statusChange);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JsonObject json = Handler.convertToJSon(response.body().string());
                        if (json.has("message")) {
                            list.remove(local);
                            notifyItemRemoved(local);
                            notifyItemRangeChanged(local, getItemCount());
                            LoginActivity.showToast(activity, json.get("message").getAsString());
                        } else
                            LoginActivity.showToast(activity, json.get("error").getAsString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    LoginActivity.showToast(activity, t.getMessage());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ItemHolder extends ViewHolder {
        TextView txtSanpham, txtSluong, txtPloai, txtDgia, txttrangthai, txttonggia;
        ImageView imgSanpham;
        Button btncancle;
        ConstraintLayout constraintLayout;

        public ItemHolder(@NonNull View view) {
            super(view);
            txtSanpham = view.findViewById(R.id.txtproductname);
            txtPloai = view.findViewById(R.id.txttype);
            txtSluong = view.findViewById(R.id.txtamount);
            txtDgia = view.findViewById(R.id.txtprice);
            imgSanpham = view.findViewById(R.id.imgSpham);
            txttrangthai = view.findViewById(R.id.txt_status);
            txttonggia = view.findViewById(R.id.txt_totalmoney);
            btncancle = view.findViewById(R.id.btn_cancle);
            constraintLayout = view.findViewById(R.id.layout_contraint);
        }
    }


    public List<Order> getList() {
        return list;
    }

    public void setList(List<Order> list) {
        this.list = list;
    }
}
