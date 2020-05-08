package com.team.mobileworld.core.object;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int STATUS_CANCLE = -1;
    public static final int STATUS_CONFIRM = 0;
    public static final int STATUS_DELIVRY = 1;
    public static final int STATUS_RECEIVE = 2;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("price")
    @Expose
    private int price;

    @SerializedName("pictureuri")
    @Expose
    private String image;

    @SerializedName("unit")
    @Expose
    private int amount; // so luong

    @SerializedName("quantity")
    @Expose
    private int slmax = 0;  // so luong

    @SerializedName("status")
    @Expose
    private int status = 0;

    @SerializedName("catalogid")
    @Expose
    private int catalogid;

    @SerializedName("updatedAt")
    private Date date;


    private boolean select = true;


    public Order(int id, String name, int price, String image, int amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.amount = amount;
    }

    public Order(int id, String name, int price, String image, int amount, int slmax) {
        this(id, name, price, image, amount);
        this.slmax = slmax;
    }


    public int getCatalogid() {
        return catalogid;
    }

    public void setCatalogid(int catalogid) {
        this.catalogid = catalogid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String currentStatus() {
        SimpleDateFormat simple = new SimpleDateFormat("dd-MM-yyyy");
        String format = simple.format(date);

        if (getStatus() == STATUS_CANCLE)
            return "[" + format + "] Đã hủy.";
        if (getStatus() == STATUS_CONFIRM)
            return "[" + format + "] Chờ xác nhận.";
        if (getStatus() == STATUS_DELIVRY)
            return "[" + format + "] Đang giao hàng.";
        else
            return "[" + format + "] Đã giao hàng.";
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", amount=" + amount +
                ", slmax=" + slmax +
                ", status=" + status +
                ", catalogid=" + catalogid +
                ", date=" + date +
                ", select=" + select +
                '}';
    }

    public static Order convertToOrder(Product product) {
        return new Order(product.getId(), product.getName(), product.getPrice(), product.getImage(), 0,
                product.getSlmax());
    }

    public void increase(int amount) {
        this.amount += amount;
    }

    // Lay tong so tien cua san pham
    public long getTotalMoney() {
        return price * amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSlmax() {
        return slmax;
    }

    public void setSlmax(int slmax) {
        this.slmax = slmax;
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

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
