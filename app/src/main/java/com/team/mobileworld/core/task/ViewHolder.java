package com.team.mobileworld.core.task;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private OnItemClickListener listener;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this::onClick);
    }

    /**
     * Đang ký sự kiên onclick item cho phần tử
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Lấy thông tin về sự kiện onclick
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onItemClick(v, getAdapterPosition(), v.getId());
    }
}
