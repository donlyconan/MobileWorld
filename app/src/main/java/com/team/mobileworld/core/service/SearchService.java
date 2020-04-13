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
	 * 		Giử thông tin sản phầm đã qua xử lý
	 * Xử lý-Nhận:
	 * 		Server trả về List dãy các sản phẩm 
			đối tượng Product
	 * 		
	 * @param map
	 * @return
	 */
	@POST(URL_GET_RESULT_SEARCH)
	Call<List<Product>> search(@QueryMap Map<String, String> map);
	
	
	
	/**
	 * RQ: Giử lên từ khóa của sản phẩm kết
	 * RP: nhận về một dãy các sản phẩm có từ khóa tương tự
	 * 
	 * @param text
	 * @return
	 */
	@FormUrlEncoded
	@POST(URL_GET_SUGGEST_QUERY)
	Call<ResponseBody> suggestQuery(@Field("text") String text);
	
}
