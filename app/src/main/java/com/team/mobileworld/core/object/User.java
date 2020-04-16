package com.team.mobileworld.core.object;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id")
    private long id = 0;

    @SerializedName("fullname")
    @Expose
    private String fullname;

    @SerializedName("profit")
    @Expose
    private String profit;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("bground")
    @Expose
    private String bground;

    @SerializedName("bdate")
    @Expose
    private String bdate;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("pnumber")
    @Expose
    private String pnumber;

    @SerializedName("gender")
    @Expose
    private int gender = 0;


    public User(long id) {
        this.id = id;
    }

    public User(Long id, String fullname, String email, String profit, String bground,
                String bdate, int gender, String address, String pnumber) {
        this.id = id;
        this.fullname = fullname;
        this.profit = profit;
        this.bground = bground;
        this.bdate = bdate;
        this.address = address;
        this.pnumber = pnumber;
        this.email = email;
        this.gender = gender;
    }

    public static String get(String text) {
        return text == null ? "" : text;
    }


    @Override
    public String toString() {
        return "User [id=" + id + ", fullname=" + fullname + ", profit=" + profit + ", email=" + email
                + ", bground=" + bground + ", bdate=" + bdate + ", address=" + address + ", pnumber=" + pnumber + "]";
    }


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getBground() {
        return bground;
    }

    public void setBground(String bground) {
        this.bground = bground;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPnumber() {
        return pnumber;
    }

    public void setPnumber(String pnumber) {
        this.pnumber = pnumber;
    }

    public User(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

}
