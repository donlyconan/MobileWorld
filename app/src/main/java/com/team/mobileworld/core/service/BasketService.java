package com.team.mobileworld.core.service;

import com.team.mobileworld.core.object.Order;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface BasketService {
    public static final String URL_GET_CART = "api/basket/";

    public static final String URL_POST_CART = "api/basket/accept";

    public static final String URL_UPDATE_CART = "api/basket/add";

    public static final String URL_ADD_PRODUCT_ON_CART = "api/basket/add";

    public static final String URL_DEL_PRODUCT_ON_CART = "api/basket/delete";

    /**
     * Tải tất cả thông tin về sản phầm có trong giỏ hàng
     * Trả về: JSON dạng LIST
     * Đối tượng: Order
     *
     * @return
     */
    @GET(URL_GET_CART)
    Call<List<Order>> loadCart(@Header("x-access-token") String token);

    /**
     * Giu thong tin du lieu san pham ma khach hang da dat nen server
     * server se tra 1 ma thong bao ve viec them san pham thanh cong hoac that bai
     * Tra ve:
     * message: neu them thanh cong
     * error: neu them that bai
     * * @param userid
     * * @param productid
     * * @param amount
     * * @return
     *
     * @return
     */

    @FormUrlEncoded
    @POST(URL_ADD_PRODUCT_ON_CART)
    Call<ResponseBody> addOrder(@Header("x-access-token") String token, @Field("catalogid") int catalogid, @Field("unit") int amount);



    /**
     * Cap nhat lai du lieu tren server
     * Doi tuong giu la 1 Object order
     * Nhiem vu: Cap nhat lai san pham mang id order
     * message: neu thong cong
     * error: neu that bai
     *
     * @param order
     * @return
     */
    @FormUrlEncoded
    @PUT(URL_UPDATE_CART)
    Call<ResponseBody> updateCart(@Header("x-access-token") String token, @Field("catalogid") int catalogids, @Field("unit") int amount);


    /**
     * Đưa thông tin các sản phẩm mà khách hàng đã đặt lên server
     * Server: Xử lý thông tin và giử về một mã xác nhận khi giao dịch thành công
     *
     * @param orders
     * @return
     */
    @POST(URL_POST_CART)
    Call<ResponseBody> orderedList(@Header("x-access-token") String token, @Body Map<String, Object> mapBody);


    /**
     * Đẩy lên server một danh sách id sản phẩm
     * trả về kết quả:
     * nếu thực hiện thành công là trả về message
     * Nếu thất bại là trả về 1 error
     *
     * @param id
     * @param order
     * @return
     */
    @POST(URL_DEL_PRODUCT_ON_CART)
    Call<ResponseBody> deleteOrder(@Header("x-access-token") String token, @Body List<Integer> idorders);
}
