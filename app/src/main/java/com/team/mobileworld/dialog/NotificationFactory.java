package com.team.mobileworld.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.team.mobileworld.R;

public class NotificationFactory extends AlertDialog {

    protected Button btnok;
    protected TextView txttitle, txtcontent;
    protected ImageView img_image;
    protected LinearLayout linearLayout;


    public NotificationFactory(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.notification_dialog);

        btnok = findViewById(R.id.btn_ok);
        txttitle = findViewById(R.id.txt_title);
        txtcontent = findViewById(R.id.txt_content);
        img_image = findViewById(R.id.img_image);
        linearLayout = findViewById(R.id.linearLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public static NotificationFactory create(String title, String content, Context context, Button... buttons) {
        return new NotificationFactory(context)
                .setTitle(title)
                .setContent(content)
                .factory(R.drawable.shape_10dp, buttons);
    }

    public NotificationFactory setTitle(String title) {
        setTitle(title);
        return this;
    }

    public NotificationFactory setContent(String text) {
        setContent(text);
        return this;
    }

    public NotificationFactory setImage(int resource){
        img_image.setImageResource(resource);
        return this;
    }

    public NotificationFactory factory(int resource, Button... buttons) {
        if (buttons == null)
            return this;
        for (Button button : buttons) {
            button.setMaxWidth(100);
            button.setMaxHeight(35);
            button.setBackgroundResource(resource);
            linearLayout.addView(button);
        }
        return this;
    }

    public Button getBtnok() {
        return btnok;
    }

    public void setBtnok(Button btnok) {
        this.btnok = btnok;
    }

    public TextView getTxttitle() {
        return txttitle;
    }

    public void setTxttitle(TextView txttitle) {
        this.txttitle = txttitle;
    }

    public TextView getTxtcontent() {
        return txtcontent;
    }

    public void setTxtcontent(TextView txtcontent) {
        this.txtcontent = txtcontent;
    }

    public ImageView getImg_image() {
        return img_image;
    }

    public void setImg_image(ImageView img_image) {
        this.img_image = img_image;
    }
}
