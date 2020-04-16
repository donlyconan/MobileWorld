package com.team.mobileworld.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        //Tao view
        View view = inflater.inflate(R.layout.phone_product_info, null);
        return view;
    }
}
