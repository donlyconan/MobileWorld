package com.team.mobileworld.core.service;

import com.team.mobileworld.core.object.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface UserService {
	
	public static final String URL_GET_INFO = "api/user/find?";
	public static final String URL_PUT_INFO = "api/user";
	
	/**
	 * Tải thông tin tài khoản của người dùng
	 * Nhận: Đối tượng User chứa thông tin khách hàng
	 * @param username
	 * @return
	 */
	@FormUrlEncoded
	@GET(URL_GET_INFO)
	Call<User> getPersionInfo(@Field("username") String username);
	
	
	/**
	 * Cập nhật thông tin tài khoản
	 * Đối tượng trả về 1 mã xác nhận
	 */
	@PUT(URL_PUT_INFO)
	Call<ResponseBody> updatePersonalInfo(@Body User user);
	
	
	

}
