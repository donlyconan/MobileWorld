package com.letuan.mobileworld.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.letuan.mobileworld.R;

public class NoticeDialog extends Dialog implements View.OnClickListener {

    protected Button btnCancle;
    protected Button btnOk;
    protected TextView tvNotice;
    protected TextView tvTitleDialog;

    public NoticeDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.notice_dialog);

        btnCancle = findViewById(R.id.btn_cancle);
        btnOk = findViewById(R.id.btn_ok);
        tvNotice = findViewById(R.id.tv_notice);
        tvTitleDialog = findViewById(R.id.titledialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void setNotification(String notification, String textOk, String textCancle, View.OnClickListener onClickListener) {

        tvTitleDialog.setText("Xác nhận xóa sản phẩm");
        btnOk.setText(textOk);
        tvNotice.setText(notification);
        btnCancle.setText(textCancle);
        btnOk.setOnClickListener(onClickListener);
        btnCancle.setOnClickListener(onClickListener);

        if (textCancle == null) btnCancle.setVisibility(View.GONE);

        if (onClickListener == null) btnOk.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

}
