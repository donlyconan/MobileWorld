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
    private int price;

    @SerializedName("image") @Expose
    private String image;

    @SerializedName("amount") @Expose
    private int amount;    //so luong

    @SerializedName("status") @Expose
    private int status = 0;

    private int slmax = 0;

    private boolean select = true;

    public Order(int id, String name, int price, String image, int amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.amount = amount;
    }

    public Order(int id, String name, int price, String image, int amount, int slmax) {
        this(id,name,price,image,amount);
        this.slmax = slmax;
    }


    @Override
	public String toString() {
		return "Order [id=" + id + ", name=" + name + ", price=" + price + ", image=" + image + ", amount=" + amount
				+ ", select=" + select + "]";
	}

	public static Order convertToOrder(Product product){
        return  new Order(product.getId(), product.getName()
                ,product.getPrice(),product.getImage()
                ,0,product.getSlmax());
    }



	//Lay tong so tien cua san pham
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
