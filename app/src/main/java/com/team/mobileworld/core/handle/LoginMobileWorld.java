package com.team.mobileworld.core.handle;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.RequiresApi;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.team.mobileworld.core.NetworkCommon;
import com.team.mobileworld.core.database.Database;
import com.team.mobileworld.core.object.User;
import com.team.mobileworld.core.service.LoginService;
import com.team.mobileworld.core.service.UserService;
import com.team.mobileworld.core.task.Worker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;

public class LoginMobileWorld {
    public static final List<String> permissions = Arrays.asList("public_profile", "email", "user_birthday", "user_friends");
    private static final String TAG = "userinfo";

    private CallbackManager callbackManager;
    private Activity activity;
    private User user;

    public LoginMobileWorld(Activity activity, User user) {
        this.activity = activity;
        this.user = user;
        callbackManager = CallbackManager.Factory.create();
    }

    //Dang nhap voi account
    @MainThread
    @RequiresApi(api = Build.VERSION_CODES.N)
    public User loginWithAccount(String username, String password) throws IOException {
        User user = null;
        //Thiet lap service
        LoginService service = NetworkCommon.getRetrofit().create(LoginService.class);

        //Thiet lap callback truy cap url giu va nhan du lieu
        Call<User> call = service.loginWithAccount(username, password);
        Database.print("request: " + call.request());

        user = call.execute().body();

        user = getInfoUser(user.getId());

        if (user == null)
            throw new IOException("Đăng nhập thất bại");

        return user;
    }

    //Lấy thông tin người kết nối FB
    public void loginWithFacebook(Worker action) {

        //API hiển thị activity đăng nhập
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    user = getInfoFromJsonFacebook(object);
                    Log.d(TAG, "response: " + response.toString());

                    //Dang nhap
                    LoginService service = NetworkCommon.getRetrofit().create(LoginService.class);
                    Call<User> call = service.loginWithFacebook(user);
                    Database.print("request: " + call.request());

                    User user = call.execute().body();

                    if (user != null) {
                        action.hanlde();
                    } else
                        Toast.makeText(activity.getBaseContext(), "Lỗi đăng nhập.", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity.getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle params = new Bundle();
        params.putString("fields", "name, id, email,birthday, gender");
        request.setParameters(params);
        request.executeAsync();
    }


    public static User getInfoUser(long id) throws IOException {
        /**
         * Goi den IP UserService de lay tat ca thong tin ve user
         * tham so truyen vao la id user
         * Object lay ra la user chua tat ca thong tin ve khach hang
         */
        UserService getuser = NetworkCommon.getRetrofit().create(UserService.class);
        //thuc hien goi lai
        Call<User> userCall = getuser.getPersonalInfo(id);
        //lay thong tin user
        User user = userCall.execute().body();

        return user;
    }


    public static User getInfoFromJsonFacebook(JSONObject object) throws JSONException {
        long id = Long.valueOf(object.getString("id"));
        String name = object.getString("name");
        String email = object.getString("email");
//        String birthday = object.getString("birthday");
        String urlimage = "http://graph.facebook.com/" + id + "/picture?type=large";

        return new User(id, name, email, urlimage, null,null, 0, null, null);
    }


    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public void setCallbackManager(CallbackManager callbackManager) {
        this.callbackManager = callbackManager;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
