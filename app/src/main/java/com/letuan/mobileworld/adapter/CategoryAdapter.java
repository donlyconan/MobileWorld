package com.letuan.mobileworld.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.letuan.mobileworld.R;
import com.letuan.mobileworld.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    List<Category> categoryList;
    Context context;

    public CategoryAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        TextView txtCategoryName;
        ImageView imgCategory;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_listview_category, null);
            viewHolder.txtCategoryName = view.findViewById(R.id.textviewcategory);
            viewHolder.imgCategory = view.findViewById(R.id.imageviewcategory);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Category category = (Category) getItem(position);
        viewHolder.txtCategoryName.setText(category.getCategoryName());

        if (category.getCategoryImage().contains("http")) {
            Picasso.with(context).load(category.getCategoryImage()).into(viewHolder.imgCategory);
        } else {
            viewHolder.imgCategory.setImageResource(Integer.parseInt(category.getCategoryImage()));
        }
        return view;
    }
}
