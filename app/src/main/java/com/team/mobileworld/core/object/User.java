package com.team.mobileworld.core.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.handle.Validate;

import java.io.Serializable;

public class User implements Serializable {
    public static final int ANONYMOUS = -1;
    public static final int LOGIN_ACCOUNT = 0;
    public static final int LOGIN_FACEBOOK = 1;
    public static final int LOGIN_GOOGLE = 2;

    @SerializedName("id")
    @Expose(serialize = false)
    private long id = ANONYMOUS;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    @Expose(serialize = false)
    private String password;

    @SerializedName("fullname")
    private String fullname;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("email")
    private String email;

    @SerializedName("bground")
    private String bground;

    @SerializedName("bdate")
    private String bdate;

    @SerializedName("address")
    private String address;

    @SerializedName("phonenumber")
    private String pnumber;

    @SerializedName("gender")
    private int gender = 0;

    @SerializedName("token")
    @Expose(serialize = false)
    private String accesstoken = null;

    private int link = LOGIN_ACCOUNT;

    public User() {
        this.id = -1;
    }

    public User(long id) {
        this.id = id;
    }

    public User(Long id, String fullname, String email, String avatar, String bground,
                String bdate, int gender, String address, String pnumber) {
        this.id = id;
        this.fullname = fullname;
        this.avatar = avatar;
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

    public boolean isLogin() {
        return accesstoken != null;
    }

    public boolean hasPhoneNumber() {
        return Validate.validate(pnumber, Validate.REGEX_PHONE_NUMBER);
    }

    public boolean hasAddress() {
        return Validate.validate(address, Validate.REGEX_ADDRESS);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", fullname=" + fullname + ", avatar=" + avatar + ", email=" + email
                + ", bground=" + bground + ", bdate=" + bdate + ", address=" + address + ", pnumber=" + pnumber + "]";
    }

    public String statusLogin() {
        switch (link) {
            case LOGIN_FACEBOOK:
                return "Bạn đang đăng nhập bằng tài khoản Facebook";
            case LOGIN_GOOGLE:
                return "Bạn đang đăng nhập bằng tài khoản Google";
            default:
                return "Bạn đang đăng nhập bằng tài khoản Mobile World";
        }
    }


    public String getFullname() {
        return get(fullname);
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar() {
        if (avatar == null)
            return avatar;
        else if (avatar.contains("http"))
            return avatar;
        else
            return NetworkCommon.BASE_URL + avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
        return get(address);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPnumber() {
        return get(pnumber);
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
    }

    public String getAccesstoken() {
        return accesstoken;
    }
}
