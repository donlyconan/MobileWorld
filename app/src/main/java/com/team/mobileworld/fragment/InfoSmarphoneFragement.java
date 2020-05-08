package com.team.mobileworld.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.team.mobileworld.R;
import com.team.mobileworld.core.object.SmartphoneInfo;

public class InfoSmarphoneFragement extends Fragment {

    private SmartphoneInfo info;

    public InfoSmarphoneFragement(SmartphoneInfo info) {
        this.info = info;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Tao view
        View view = inflater.inflate(R.layout.phone_product_info, null);
        TextView txtCPU = view.findViewById(R.id.txtCPU);
        TextView txtMH = view.findViewById(R.id.txtMH);
        TextView txtHDH = view.findViewById(R.id.txtHDH);
        TextView txtCT = view.findViewById(R.id.txtCT);
        TextView txtCS = view.findViewById(R.id.txtCS);
        TextView txtBNT = view.findViewById(R.id.txtBNT);
        TextView txtTNho = view.findViewById(R.id.txtTNho);
        TextView txtSim= view.findViewById(R.id.txtSim);
        TextView txtDLP = view.findViewById(R.id.txtDLP);
        TextView txtRam = view.findViewById(R.id.txtRam);

        txtCPU.setText(get(info.getCpu()));
        txtMH.setText(get(info.getScreen()));
        txtHDH.setText(get(info.getOs()));
        txtRam.setText(get(info.getRam()));
        txtBNT.setText(get(info.getInternalmemmory()));
        txtDLP.setText(get(info.getBatery()));
        txtSim.setText(get(info.getSim()));
        txtTNho.setText(get(info.getMemorystick()));
        txtCT.setText(get(info.getFrontcamera()));
        txtCS.setText(get(info.getBackcamera()));
        return view;
    }

    public static String get(String text){
        return text == null ? "" : text;
    }
}
