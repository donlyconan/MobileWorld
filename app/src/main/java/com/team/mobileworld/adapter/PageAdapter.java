package com.team.mobileworld.adapter;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.ListFragment;

import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.fragment.BillFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PageAdapter extends FragmentPagerAdapter {

    List<BillFragment> fragments;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public PageAdapter(@NonNull FragmentManager fm, List<Order> list, int behavior) {
        super(fm, behavior);
        fragments = new ArrayList<>(4);
        fragments.addAll(Arrays.asList(
                BillFragment.create("Chờ xác nhận", list.stream()
                .filter(e->e.getStatus()==Order.STATUS_CONFIRM).collect(Collectors.toList()))
                , BillFragment.create("Đang giao hàng", list.stream()
                        .filter(e->e.getStatus()==Order.STATUS_DELIVRY).collect(Collectors.toList()))
                , BillFragment.create("Đã nhận hàng", list.stream()
                        .filter(e->e.getStatus()==Order.STATUS_RECEIVE).collect(Collectors.toList()))
                , BillFragment.create("Đã hủy", list.stream()
                        .filter(e->e.getStatus()==Order.STATUS_CANCLE).collect(Collectors.toList()))
        ));
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
