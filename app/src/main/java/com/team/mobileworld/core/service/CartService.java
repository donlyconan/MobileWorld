package com.team.mobileworld.core.service;

import com.team.mobileworld.core.object.Order;
import com.team.mobileworld.core.object.Product;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CartService {
	
	public static final String URL_GET_CART = "api/cart/";
	
	public static final String URL_POST_CART = "api/checkout/";

	public static final String URL_UPDATE_CART = "api/{id}/cart";

	public static final String URL_ADD_PRODUCT_ON_CART = "api/{}/cart";

	public static final String URL_DEL_PRODUCT_ON_CART = "api/checkout/{id}";

	/**
	 * Tải tất cả thông tin về sản phầm có trong giỏ hàng
	 * Trả về: JSON dạng LIST
	 * Đối tượng: Order
	 * 
	 * @return
	 */
	@GET(URL_GET_CART)
	Call<List<Order>> loadCart(@Query("id") long id);

	/**
	 * Giu thong tin du lieu san pham ma khach hang da dat nen server
	 * server se tra 1 ma thong bao ve viec them san pham thanh cong hoac that bai
	 * Tra ve:
	 * message: neu them thanh cong
	 * error: neu them that bai
	 * @param order
	 * @return
	 */
	@FormUrlEncoded
	@POST(URL_ADD_PRODUCT_ON_CART)
	Call<ResponseBody> addOrder(@Field("id") long id,@Body Order order);

	/**
	 * Đẩy lên server một danh sách sản phẩm
	 * trả về kết quả nếu thực hiện thành công là trả về message
	 * Nếu thất bại là trả về 1 error
	 * @param id
	 * @param order
	 * @return
	 */
	@FormUrlEncoded
	@POST(URL_DEL_PRODUCT_ON_CART)
	Call<ResponseBody> deleteOrder(@Field("id") long id,@Body List<Order> order);

	/**
	 * Cap nhat lai du lieu tren server
	 * Doi tuong giu la 1 Object order
	 * Nhiem vu: Cap nhat lai san pham mang id order
	 * message: neu thong cong
	 * error: neu that bai
	 * @param order
	 * @return
	 */
	@FormUrlEncoded
	@PUT(URL_UPDATE_CART)
	Call<ResponseBody> updateCart(@Field("id") long id,@Body Order order);



	
	/**
	 * Đưa thông tin các sản phẩm mà khách hàng đã đặt lên server
	 * Server: Xử lý thông tin và giử về một mã xác nhận khi giao dịch thành công
	 * 
	 * @param orders
	 * @return
	 */
	@FormUrlEncoded
	@POST(URL_POST_CART)
	Call<ResponseBody> postOrderedList(@Field("id") long id, @Body List<Order> orders);


}
