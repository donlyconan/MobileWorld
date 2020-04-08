package com.letuan.mobileworld.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Product implements Serializable {
    public  static final int PL_SMARTPHONE = 100;
    public  static final int PL_LAPTOP = 200;

    private int id;
    private String name;
    private Integer price;
    private String image;
    private String description;
    private int categoryid;
    private int lsanpham = PL_SMARTPHONE;


    public Product(int id, String name, Integer price, String image, String description, int categoryid) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.categoryid = categoryid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public int getLsanpham() {
        return lsanpham;
    }

    public void setLsanpham(int lsanpham) {
        this.lsanpham = lsanpham;
    }
}
