package com.team.mobileworld.core.service;

import com.team.mobileworld.core.object.Product;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface SearchService {
	
	
	public static final String URL_GET_RESULT_SEARCH = "api/product/find?";
	
	
	public static final String URL_GET_SUGGEST_QUERY = "api/product/query?";
	
	/**
	 * Giử đi:
	 * 		Giử thông tin sản phầm đã qua xử lý ở dạng văn bản
	 * Xử lý-Nhận:
	 * 		Server trả về List là dãy các sản phẩm
			đối tượng Product đối tượng mà khách hàng đang tìm kiếm
	 *
	 * 	từ khóa: keyword: "text"
	 * @param map
	 * @return
	 */
	@POST(URL_GET_RESULT_SEARCH)
	Call<List<Product>> search(@QueryMap Map<String, String> params);
	
	
	
	/**
	 * RQ: Giử lên từ khóa hoặc id của sản phẩm kết
	 * RP: nhận về một dãy các sản phẩm có từ khóa tương tự
	 * 
	 * @param keyword
	 * @return
	 */
	@FormUrlEncoded
	@POST(URL_GET_SUGGEST_QUERY)
	Call<ResponseBody> suggestQuery(@Field("text") String keyword);
	
}
