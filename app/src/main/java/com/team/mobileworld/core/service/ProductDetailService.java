package com.team.mobileworld.core.service;

import com.team.mobileworld.core.object.Product;
import com.team.mobileworld.core.object.ProductInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductDetailService {
	
	public static final String URL_PRODUCT_INFO = "api/catalog/{id}";
	
	public static final String URL_TECHNICAL_INFO = "api/catalog/technical/{id}";

	
	/**
	 * Giử đi: 1 đường dẫn chứa id của sản phẩm
	 * Nhận về: Thông tin về sản phẩm dưới dạng object
	 */
	@GET(URL_PRODUCT_INFO)
	Call<Product> getProductDetail(@Path(value = "id") int id);
	
	
	/**
	 * Giử đi: Mã ID của sản phẩm yêu cầu trả về thông tn kỹ thuật của sản phẩm
	 * Nhận về: Một đối tượng ProductInfo chứa thông tin kỹ thuật của sản phẩm
	 * object: ProductInfo
	 */
	@GET(URL_TECHNICAL_INFO)
	Call<ResponseBody> getTechnicalData(@Path("id") int id);
	
}
