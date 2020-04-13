package com.team.mobileworld.core.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Account {
    public static String REGEX_USER = "^[a-z0-9_-].{3,30}$";
    public static String REGEX_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //toi
    public boolean validate()
    {
        return  username.matches(REGEX_USER) && password.matches(REGEX_PASSWORD);
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
    



}
