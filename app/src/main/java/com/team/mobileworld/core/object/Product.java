package com.team.mobileworld.core.object;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product implements Serializable {
    /**
     *
     */
    public static final int LSP_PHONE = 1;
    public static final int LSP_LAPTOP = 2;

    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private Integer price;
    @SerializedName("pictureuri")
    private String image;
    @SerializedName("description")
    private String description;
    @SerializedName("catalogtypeid")
    private int categoryid;
    @SerializedName("quantity")
    private int slmax;


    public Product(int id, String name, Integer price, String image, String description, int categoryid) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.categoryid = categoryid;
    }

    public Product(int id, String name, Integer price, String image, String description, int categoryid, int slmax) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.categoryid = categoryid;
        this.slmax = slmax;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", categoryid=" + categoryid +
                ", slmax=" + slmax +
                '}';
    }

    private boolean isSmartphone() {
        return categoryid == 1;
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

    public int getPrice() {
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

    public int getSlmax() {
        return slmax;
    }

    public void setSlmax(int slmax) {
        this.slmax = slmax;
    }

}
