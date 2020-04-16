package com.team.mobileworld.core.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SignUpService {
	
	
	public static final String URL_REGISTER = "api/login/register/";


	/**
	 * Giử thông tin về tài khoản mật khẩu lên server 
	 * Nhận về: Mã xác thực tài khoản đã được đăng ký 
	 * 			Mã lỗi tài khoản đã tồn tại
	 * 
	 * @param params
	 * @return
	 */
	@FormUrlEncoded
	@POST(URL_REGISTER)
	Call<ResponseBody> registerAccount(
            @Field("username") String username
            , @Field("password") String password
            , @Field("email") String email
    );

}
