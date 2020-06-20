package com.team.mobileworld.adapter;

import android.app.Activity;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team.mobileworld.R;
import com.team.mobileworld.core.object.Message;
import com.team.mobileworld.core.task.OnItemClickListener;
import com.team.mobileworld.core.task.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ItemHolder> {
    private Activity activity;
    private List<Message> list;
    private TextView watherview;
    private OnItemClickListener onItemClickListener;

    public NotifyAdapter(Activity activity, List<Message> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notify, null);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Message item = list.get(position);
        holder.txttitle.setText(item.getTitle());
        holder.txtcotent.setText(item.getContent());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        holder.txtdate.setText(format.format(item.getDate()));

        if (item.getId() == -1)
            Linkify.addLinks(holder.txtcotent, Linkify.WEB_URLS);
    }

    public List<Message> getList() {
        return list;
    }

    public void setList(List<Message> list) {
        this.list = list;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public TextView getWatherview() {
        return watherview;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ItemHolder extends ViewHolder {
        TextView txttitle, txtcotent, txtdate;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txttitle = itemView.findViewById(R.id.txttitle);
            txtcotent = itemView.findViewById(R.id.txtcontent);
            txtdate = itemView.findViewById(R.id.txtdatetime);
        }
    }
}
