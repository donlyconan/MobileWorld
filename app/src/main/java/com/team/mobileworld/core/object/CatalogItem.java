package com.team.mobileworld.core.object;


import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatalogItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("product")
    @Expose
    private List<Product> products;

    @SerializedName("mode")
    @Expose
    private int mode;

    public CatalogItem(String title, List<Product> products, int mode) {
        this.title = title;
        this.products = products;
        this.mode = mode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }


}
