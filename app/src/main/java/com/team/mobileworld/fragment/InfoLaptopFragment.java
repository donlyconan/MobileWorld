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
import com.team.mobileworld.core.object.LaptopInfo;

public class InfoLaptopFragment extends Fragment {

    private LaptopInfo info;

    public InfoLaptopFragment(LaptopInfo info) {
        this.info = info;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.laptop_product_info, null);
        TextView txtCPU = view.findViewById(R.id.txtCPU);
        TextView txtMH = view.findViewById(R.id.txtMH);
        TextView txtHDH = view.findViewById(R.id.txtHDH);
        TextView txtRam = view.findViewById(R.id.txtRam);

        TextView txtCMH = view.findViewById(R.id.txtCMH);
        TextView txtTK = view.findViewById(R.id.txtTK);
        TextView txtKT = view.findViewById(R.id.txtKT);
        TextView txtTDRM = view.findViewById(R.id.txtTDRM);
        TextView txtOC = view.findViewById(R.id.txtOC);
        TextView txtCKN = view.findViewById(R.id.txtCKN);

        txtCPU.setText(get(info.getCpu()));
        txtMH.setText(get(info.getScreen()));
        txtHDH.setText(get(info.getOs()));
        txtRam.setText(get(info.getRam()));

        txtCKN.setText(get(info.getConnector()));
        txtOC.setText(get(info.getHarddrive()));
        txtKT.setText(get(info.getSize()));
        txtTDRM.setText(get(info.getRelese()));
        txtCMH.setText(get(info.getCardscreen()));
        txtTK.setText(get(info.getDesign()));
        return view;
    }

    public static String get(String text){
        return text == null ? "" : text;
    }
}
