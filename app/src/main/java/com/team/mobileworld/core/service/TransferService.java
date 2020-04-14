package com.team.mobileworld.core.service;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.Login;

public interface TransferService {

    public void startActivity(Class<Login> temp, String style);

    public void startActivityForResult(AppCompatActivity context, Class<?> temp, int request , String style);

}
