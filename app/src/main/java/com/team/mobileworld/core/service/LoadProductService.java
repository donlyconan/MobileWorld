package com.team.mobileworld.core.service;

import com.team.mobileworld.core.object.ItemList;
import com.team.mobileworld.core.object.Product;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface LoadProductService {
	
	public static final String URL_HOME_PAGE = "api/home/";
	
	public static final String URL_SMARTPHONE_PAGE = "api/smartphone/";
	
	public static final String URL_LAPTOP_PAGE = "api/laptop/";
	/**
	 * @Body - Gửi các đối tượng Java dưới dạng thân yêu cầu.
	 * @Url - sử dụng URL động.
	 * 
	 * @Query () và tên tham số truy vấn, mô tả loại. Để URL mã hóa một truy vấn sử dụng mẫu:
	 * @Field- gửi dữ liệu dưới dạng urlencoding. Điều này đòi hỏi một
	 * @FormUrlEncodedchú thích kèm theo phương pháp. Các 
	 * @Fieldtham số chỉ hoạt
	 *                    động với một POST
	 */

	/**
	 *  Lấy về dữ liệu thông tin sản phẩm cho trang chủ
	 * @return
	 */
	@GET(URL_HOME_PAGE)
	Call<ItemList> loadHomePage();

	/**
	 *  Lấy dữ liệu thông tin sản phầm cho trang View Smartphone
	 * @return
	 */
	@GET(URL_SMARTPHONE_PAGE)
	Call<ItemList> loadSmartphonePage();

	/**
	 *  Lấy dữ liệu thông tin sản phầm cho trang View Laptop
	 * @return
	 */
	@GET(URL_LAPTOP_PAGE)
	Call<ItemList> loadLaptopPage();
	

	/**
	 *  Tải thêm sản phẩm khi được yêu cầu
	 *  Index: cung cấp vị trí đã tải trước đó
	 *  
	 *  Param: 
	 *  	title: nhãn sản phẩm cần tải
	 *  	page: trang sản phẩm
	 *  	index: thứ tự cần tải
	 * @param url
	 * @return
	 */
	@GET
	Call<List<Product>> loadIndex(@Url String url, @FieldMap Map<String, String> params);

}
