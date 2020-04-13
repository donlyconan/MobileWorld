package com.team.mobileworld.core.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {
	
	
	public static final String URL_LOGIN = "api/login/signin";

	/**
	 * Giử thông tin tài khoản đăng nhập lên server 
	 * Đúng: giử về một mã token 
	 * Sai:  Giử về lỗi kèm mã lỗi
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@FormUrlEncoded
	@POST(URL_LOGIN)
	Call<ResponseBody> loginAccount(@Field("username") String username, @Field("password") String password);

	
}
