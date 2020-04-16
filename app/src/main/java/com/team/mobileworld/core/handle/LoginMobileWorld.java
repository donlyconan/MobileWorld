package com.team.mobileworld.core.handle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;
import com.team.mobileworld.activity.LoginActivity;
import com.team.mobileworld.activity.MainActivity;
import com.team.mobileworld.core.object.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;

public class LoginMobileWorld {
//    private CallbackManager callbackManager;
//    private LoginButton btnlogin;
//
//    public LoginMobileWorld(LoginButton btnlogin) {
//        this.btnlogin = btnlogin;
//    }
//
//    //Dang nhap voi facebook
//    public void loginWithFacebook(){
//        //cau hinh callbackmanganer
//        callbackManager = CallbackManager.Factory.create();
//        final List<String> permissions = Arrays.asList("public_profile", "email", "user_birthday", "user_friends");
//
//        //Dang ky quyen truy cap
//        btnlogin.setReadPermissions(permissions);
//
//    }
//
//    //Dang nhap voi account
//    public void loginWithAccount(String username, String password){
//        ResponseBody response = call.execute().body();
//        JsonObject json = APIhandler.convertToJSon(response.string());
//
//        if (json.has("id")) {
//            MainActivity.setUser(new User(Long.valueOf(json.get("id").toString())));
//            // On complete call either onLoginSuccess or onLoginFailed
//            onLoginSuccess();
//
//            //Dang ky
//            onActionChange(remember.isChecked());
//            onStartMainActivity();
//        } else {
//            onLoginFailed();
//            Toast.makeText(LoginActivity.this, "Đăng nhập thất bạn: " + json.get("error").toString(), Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//    //Lấy thông tin người kết nối FB
//    private void loadPersonalInfo(final Activity activity) {
//
//        //API hiển thị activity đăng nhập
//        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//            @Override
//            public void onCompleted(JSONObject object, GraphResponse response) {
//                try {
//                    User user = getInfoUserFromFacebook(object);
//                    Log.d(TAG, response.toString());
//
//
//                    onStartMainActivity();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        Bundle params = new Bundle();
//        params.putString("fields", "name, id, email,birthday, gender,friends");
//        request.setParameters(params);
//        request.executeAsync();
//    }
//
//    public static User getInfoUserFromFacebook(JSONObject object) throws JSONException {
//        User user = null;
//        long id = Long.valueOf(object.getString("id"));
//        String name = object.getString("name");
//        String email = object.getString("email");
//        String birthday = object.getString("birthday");
//        String urlimage = "http://graph.facebook.com/" + id + "/picture?type=large";
//
//        return new User(id, name, email, email, null, birthday, 0, null, null);
//    }
}
