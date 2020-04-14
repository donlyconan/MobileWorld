package com.team.mobileworld.fragment;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.team.mobileworld.R;
import com.team.mobileworld.adapter.CustomListAdapter;
import com.team.mobileworld.core.handle.ItemTest;
import com.team.mobileworld.core.object.ItemList;

import java.util.ArrayList;
import java.util.List;

public class CustomViewFragement extends Fragment {
    public static final int VERTICAL = 10;
    public static final int HORIZONTAL = -10;
    public static final int GRID = 0;

    protected HomeHolder holder;
    protected List<ItemList> goods;
    protected CustomListAdapter cadapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CustomViewFragement(List<ItemList> goods) {
        this.goods = goods;
        goods = ItemTest.getItemList();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onAttach(@NonNull Activity context) {
        goods.removeIf(e->e.getProducts().size() == 0);
        cadapter = new CustomListAdapter(context, goods);
        super.onAttach(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_control_home, null);
        holder = new HomeHolder(view);
        holder.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        holder.recycler.setHasFixedSize(true);
        holder.recycler.setAdapter(cadapter);
        actionViewFlipper();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void actionViewFlipper() {
        ArrayList<String> mangQuangCao = new ArrayList<>();
        mangQuangCao.add("https://cdn.tgdd.vn/Products/Images/42/217936/samsung-galaxy-s20-plus-400x460-fix-400x460.png");
        mangQuangCao.add("https://cdn.tgdd.vn/Products/Images/44/207680/asus-vivobook-x509f-i7-8565u-8gb-mx230-win10-ej13-5-2-1-2-1-600x600.jpg");
        mangQuangCao.add("https://cdn.tgdd.vn/Products/Images/42/210653/iphone-11-pro-max-256gb-black-400x460.png");
        mangQuangCao.add("https://cdn.tgdd.vn/Products/Images/44/198795/lenovo-ideapad-530s-14ikb-i7-8550u-8gb-256gb-win10-16-600x600.jpg");

        for (int i = 0; i < mangQuangCao.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            Picasso.get().load(mangQuangCao.get(i)).error(R.drawable.error).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            holder.flipper.addView(imageView);
        }

        //thời gian chuyển
        holder.flipper.setFlipInterval(5000);
        holder.flipper.setAutoStart(true);

        android.view.animation.Animation animation_slide_in = AnimationUtils.loadAnimation(getActivity() , android.R.anim.slide_in_left);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);

        //set animation
        holder.flipper.setInAnimation(animation_slide_in);
        holder.flipper.setOutAnimation(animation_slide_out);
    }


    public class HomeHolder extends RecyclerView.ViewHolder {
        public ViewFlipper flipper;
        public RecyclerView recycler;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            flipper = itemView.findViewById(R.id.viewflipper);
            recycler = itemView.findViewById(R.id.recycle_view);
        }
    }

    public HomeHolder getHolder() {
        return holder;
    }

    public void setHolder(HomeHolder holder) {
        this.holder = holder;
    }

    public List<ItemList> getGoods() {
        return goods;
    }

    public void setGoods(List<ItemList> goods) {
        this.goods = goods;
    }

    public CustomListAdapter getCadapter() {
        return cadapter;
    }

    public void setCadapter(CustomListAdapter cadapter) {
        this.cadapter = cadapter;
    }
}
