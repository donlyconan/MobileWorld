package com.team.mobileworld.core.service;

import com.team.mobileworld.core.object.Order;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface OrderService {
	
	public static final String URL_GET_CART = "api/cart/";
	
	public static final String URL_POST_CART = "api/checkout/";
	
	/**
	 * Tải tất cả thông tin về sản phầm có trong giỏ hàng
	 * Trả về: JSON dạng LIST
	 * Đối tượng: Order
	 * 
	 * @return
	 */
	@GET(URL_GET_CART)
	Call<List<Order>> loadCart();
	
	/**
	 * Đưa thông tin các sản phẩm mà khách hàng đã đặt lên server
	 * Server: Xử lý thông tin và giử về một mã xác nhận
	 * 
	 * @param orders
	 * @return
	 */
	@POST(URL_POST_CART)
	Call<ResponseBody> postOrderedList(@Body List<Order> orders);
	


}
