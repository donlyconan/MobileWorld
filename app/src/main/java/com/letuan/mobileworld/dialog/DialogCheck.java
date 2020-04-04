package com.letuan.mobileworld.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

public class DialogCheck extends NoticeDialog{

    private Context context;

    public DialogCheck(@NonNull Context _context) {
        super(_context);
        context = _context;
        tvTitleDialog.setText("Lỗi kết nối");
        btnOk.setText("Tải lại");
        btnCancle.setText("Thoát");
        tvNotice.setText("Không có kết nối !");
    }

    public void setNotification(final Class<?> cls, final Activity activity){
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, cls);
                activity.startActivity(intent);
                activity.finish();
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

}
