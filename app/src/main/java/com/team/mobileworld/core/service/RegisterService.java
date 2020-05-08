package com.team.mobileworld.core.service;

import com.team.mobileworld.core.object.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterService {
	
	

	public static final String URL_REGISTER = "api/login/register";

	/**
	 * Giử thông tin về tài khoản mật khẩu lên server 
	 * Nhận về: Mã xác thực tài khoản đã được đăng ký (1 message hoac mot id)
	 * 			Mã lỗi tài khoản đã tồn tại
	 * 
	 * @param params
	 * @return
	 */
	@FormUrlEncoded
	@POST(URL_REGISTER)
	Call<User> registerAccount(
			  @Field("username") String username
            , @Field("password") String password
            , @Field("email") String email
    	);

}
