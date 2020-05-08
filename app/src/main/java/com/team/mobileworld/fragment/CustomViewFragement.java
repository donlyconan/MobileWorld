package com.team.mobileworld.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.team.mobileworld.R;
import com.team.mobileworld.activity.MainActivity;
import com.team.mobileworld.activity.ProductDetail;
import com.team.mobileworld.adapter.CatalogItemAdapter;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.database.Database;
import com.team.mobileworld.core.object.CatalogItem;
import com.team.mobileworld.core.object.Product;
import com.team.mobileworld.core.service.LoadProductService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomViewFragement extends Fragment {
    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = -1;
    public static final int GRID = 0;

    protected HomeHolder holder;
    protected List<CatalogItem> goods;
    protected CatalogItemAdapter adapter;
    protected LoadFragement fragload;
    protected MainActivity activity;
    protected Call<List<CatalogItem>> call;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CustomViewFragement(List<CatalogItem> goods, LoadFragement loadFragement) {
        this.goods = goods;
        this.fragload = loadFragement;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onAttach(@NonNull Activity context) {
        adapter = new CatalogItemAdapter(context, goods);
        this.activity = (MainActivity) context;
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
        holder.recycler.setAdapter(adapter);
        actionViewFlipper();
        return view;
    }


    public void loadPage(final String page) {
        closeLoad();

        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_main, fragload).commit();

        LoadProductService service = NetworkCommon.getRetrofit().create(LoadProductService.class);

        //Goi lai service lay du lieu
        call = service.openLoadDataOnPage(page);
        Database.print(call.request().toString());

        call.enqueue(new Callback<List<CatalogItem>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<CatalogItem>> call, Response<List<CatalogItem>> response) {
                if (response.isSuccessful()) {
                    goods = response.body();
                    goods.removeIf(e -> e.getProducts() == null || e.getProducts().size() == 0);
                    adapter = new CatalogItemAdapter(getActivity(), goods);
                    adapter.notifyDataSetChanged();

                    Database.print("size goods=" + goods.size());

                    if (goods.size() == 0)
                        fragload.error();
                    else
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frag_main, CustomViewFragement.this).commitAllowingStateLoss();
                } else fragload.error();

            }

            @Override
            public void onFailure(Call<List<CatalogItem>> call, Throwable t) {
                Toast.makeText(activity, "Lỗi: " + t.getMessage(), Toast.LENGTH_LONG).show();
                fragload.error();
                fragload.setWorker(() -> loadPage(page));
                Database.print("error: " + t.getMessage());
            }
        });
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

        android.view.animation.Animation animation_slide_in = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);

        //set animation
        holder.flipper.setInAnimation(animation_slide_in);
        holder.flipper.setOutAnimation(animation_slide_out);
    }

    public void print(String text) {
        Log.d("debug", text);
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

    public void closeLoad() {
        if(call != null)
        synchronized (call) {
            call.cancel();
        }
    }

    public HomeHolder getHolder() {
        return holder;
    }

    public void setHolder(HomeHolder holder) {
        this.holder = holder;
    }

    public List<CatalogItem> getGoods() {
        return goods;
    }

    public void setGoods(List<CatalogItem> goods) {
        this.goods = goods;
    }

    public CatalogItemAdapter getCadapter() {
        return adapter;
    }

    public void setCadapter(CatalogItemAdapter cadapter) {
        this.adapter = cadapter;
    }
}
