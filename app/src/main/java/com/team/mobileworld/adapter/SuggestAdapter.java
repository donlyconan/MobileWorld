package com.team.mobileworld.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team.mobileworld.R;
import com.team.mobileworld.core.task.OnItemClickListener;
import com.team.mobileworld.core.task.ViewHolder;

import java.util.List;

public class SuggestAdapter extends RecyclerView.Adapter<SuggestAdapter.Holder> {
    private OnItemClickListener listener;
    private List<String> list;
    private Activity activity;

    public SuggestAdapter(Activity activity, List<String> list) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggest, null);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.txtsuggest.setText(list.get(position));
        holder.setOnItemClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    class Holder extends ViewHolder {

        TextView txtsuggest;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txtsuggest = itemView.findViewById(R.id.txt_nameproduct);
        }
    }
}
