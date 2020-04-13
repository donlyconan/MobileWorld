package com.team.mobileworld.core.object;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("id") @Expose
    private int id;

    @SerializedName("name") @Expose
    private String name;

    @SerializedName("price") @Expose
    private Long price;

    @SerializedName("image") @Expose
    private String image;

    @SerializedName("amount") @Expose
    private int amount;    //so luong

    @SerializedName("status") @Expose
    private int status = 0;
    
    private boolean select = true;

    public Order(int id, String name, Long price, String image, int amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.amount = amount;
    }
    
    

    @Override
	public String toString() {
		return "Order [id=" + id + ", name=" + name + ", price=" + price + ", image=" + image + ", amount=" + amount
				+ ", select=" + select + "]";
	}



	//Lay tong so tien cua san pham
    public long getTotalMoney() {
        return price * amount;
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

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
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
