package com.team.mobileworld.core.task;


import android.view.View;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface OnItemClickListener {

    /**
     * Xử lý sự kiện cho recycle view
     * khi click item sẽ trả về:
     *      view: item
     *      position: vị trí phần tử
     */

    @MainThread
    public void onItemClick(View view, int position, int id);
}
