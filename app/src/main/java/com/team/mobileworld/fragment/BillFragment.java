package com.team.mobileworld.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.team.mobileworld.R;
import com.team.mobileworld.adapter.OrderApdater;
import com.team.mobileworld.core.object.Order;

import java.util.List;

public class BillFragment extends Fragment {

    private String title;
    private List<Order> list;
    private OrderApdater apdater;

    public BillFragment(String title, List<Order> apdater) {
        this.title = title;
        this.apdater = new OrderApdater(getActivity(), list);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_recyclerview, null);
        final RecyclerView recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(apdater);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    public static BillFragment create(String title, List<Order> list) {
        return new BillFragment(title, list);
    }
}
