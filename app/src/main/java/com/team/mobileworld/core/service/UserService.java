package com.team.mobileworld.core.service;

import com.team.mobileworld.core.object.User;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserService {
	
	public static final String URL_GET_INFO = "api/user/find/{id}";

	public static final String URL_PUT_INFO = "api/user";

	public static final String URL_PUT_IMAGE = "api/user";

	/**
	 * Tải thông tin tài khoản của người dùng
	 * Nhận: Đối tượng User chứa thông tin khách hàng
	 * @param username
	 * @param id
     * @return
	 */
	@GET(URL_GET_INFO)
	Call<User> getPersonalInfo(@Path("id") long id);
	
	
	/**
	 * Cập nhật thông tin tài khoản
	 * Đối tượng trả về 1 mã xác nhận
	 */
	@PUT(URL_PUT_INFO)
	Call<ResponseBody> updatePersonalInfo(@Body User user);

	/**
	 * Cập nhật ảnh của người dùng trên server
	 * Ảnh có thể là: Background hoặc Image
	 * @param response
	 * @return
	 */
	@FormUrlEncoded
	@PUT(URL_PUT_IMAGE)
	Call<ResponseBody> updateImage(
			@Field("id") long id
			, @Part ResponseBody response
			, @Path("file") MultipartBody.Part file
			, @Field("type") int type
	);
	

}
