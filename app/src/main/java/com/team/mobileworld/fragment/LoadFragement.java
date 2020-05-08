package com.team.mobileworld.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.team.mobileworld.R;
import com.team.mobileworld.core.task.Worker;

public class LoadFragement extends Fragment {
    private TextView txtinfo;
    private ProgressBar progressBar;
    private Button btntry;
    ImageView imgview;
    private Worker worker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.process_bar, null);
        txtinfo = view.findViewById(R.id.txtinfo);
        progressBar = view.findViewById(R.id.processbar);
        btntry = view.findViewById(R.id.btnconnect);
        imgview = view.findViewById(R.id.img_error);

        btntry.setVisibility(View.INVISIBLE);
        btntry.setOnClickListener(e -> {
            if (worker != null)
                worker.hanlde();
            action();
        });
        return view;
    }

    public void error() {
        txtinfo.setText("Lỗi kết nối...");
        progressBar.setIndeterminate(false);
        btntry.setVisibility(View.VISIBLE);
        imgview.setVisibility(View.VISIBLE);
    }

    public void action() {
        txtinfo.setText("Loading...");
        progressBar.setIndeterminate(true);
        btntry.setVisibility(View.INVISIBLE);
        imgview.setVisibility(View.INVISIBLE);
    }

    public TextView getTxtinfo() {
        return txtinfo;
    }

    public void setTxtinfo(TextView txtinfo) {
        this.txtinfo = txtinfo;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public Button getBtntry() {
        return btntry;
    }



    public void setBtntry(Button btntry) {
        this.btntry = btntry;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
