package com.team.mobileworld.core.handle;

import android.telecom.Call;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

public class FacebookAPI {

    private CallbackManager callbackManager = CallbackManager.Factory.create();


    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    public void getUserInfo()
    {

    }

}
