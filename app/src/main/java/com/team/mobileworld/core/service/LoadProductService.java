package com.team.mobileworld.core.service;

import android.graphics.pdf.PdfDocument;

import androidx.viewpager.widget.ViewPager;

import com.team.mobileworld.core.object.ItemList;
import com.team.mobileworld.core.object.Product;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface LoadProductService {
	
	public static final String URL_HOME_PAGE = "api/catalog/home/";
	
	public static final String URL_SMARTPHONE_PAGE = "api/catalog/mobile/";
	
	public static final String URL_LAPTOP_PAGE = "api/catalog/laptop/";

	public static final String URL_LOAD_PAGE = "api/catalog/{page}";

	public static final String URL_OPEN_LOAD = "api/catalog/query?";

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
	 *title = ALL /SAN PHAM MOI NHAT
	 *offset = hs * sosp
	 *Limit
	 * */
	@GET(URL_HOME_PAGE)
	Call<List<ItemList>> loadHomePage();

	/**
	 *  Lấy dữ liệu thông tin sản phầm cho trang View Smartphone
	 * @return
	 */
	@GET(URL_SMARTPHONE_PAGE)
	Call<List<ItemList>> loadSmartphonePage();

	/**
	 *  Lấy dữ liệu thông tin sản phầm cho trang View Laptop
	 * @return
	 */
	@GET(URL_LAPTOP_PAGE)
	Call<ItemList> loadLaptopPage();

	/**
	 * Page: cung cấp đường dẫn Vị trí trang cần tải
	 * Giử 1 yêu cầu load dữ liệu lên server
	 * Nhận về 1 List object itemlist chứa thông tin sản phẩm
	 * Các sản phẩm được gom nhóm và sắp xếp
	 * @param page
	 * @return
	 */
	@GET(URL_LOAD_PAGE)
	Call<List<ItemList>>  openLoadDataonPage(@Path("page") String page);

	/**
	 *  Tải thêm sản phẩm khi được yêu cầu
	 *
	 *  Param:
	 *  	title: nhãn sản phẩm cần tải
	 *  	page: trang sản phẩm hiện tại
	*  		offset: cung cấp vị trí để xác định lần tải trước đó
	 *  	limit: giói hạn sản phẩm mỗi lần tải
	 * @return
	 */
	@GET(URL_OPEN_LOAD)
	Call<List<Product>> openLoadDataOnList(@QueryMap Map<String, String> params);


}
