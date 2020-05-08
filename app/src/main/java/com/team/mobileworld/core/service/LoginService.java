package com.team.mobileworld.core.service;

import com.team.mobileworld.core.object.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {
	
	
	public static final String URL_LOGIN = "api/login";

	public static final String URL_LOGIN_WITH_FACEBOOK = "api/login/loginfacebook";

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
	Call<User> loginWithAccount(@Field("username") String username, @Field("password") String password);

	/**
	 * Hàm đăng nhập hoặc đăng ký liên kết với tài khoản facebook
	 * Giử 1 object user mang tất cả thông tin người dùng liên kết lên server xác nhận:
	 * 		Nếu id của người dùng đã tồn tại trên server server sẽ giử về 1 mã xác nhận id đã tồn tại
	 * 			người dùng được quyền đăng nhập, sử dụng tất cả dịch vụ
	 * 		Nếu id của người sử dụng chưa tồn tại:
	 * 			Server sẽ thêm id , info của người liên kết vào cơ sở dữ liệu sau đó sẽ giử về 1 mã
	 * 			xác nhận đăng nhập, liên kết thành công
	 * @param user
	 * @return
	 */

	@POST(URL_LOGIN_WITH_FACEBOOK)
	Call<User> loginWithFacebook(@Body User user);

}
