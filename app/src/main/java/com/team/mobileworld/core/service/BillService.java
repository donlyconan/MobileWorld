package com.team.mobileworld.core.service;

import com.team.mobileworld.core.object.Order;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface BillService {
    public static final String URL_BILL = "api/bill/";

    public static final String URL_REPLACE_STATUS_ORDED = "api/bill";

    /**
     * Đưa lên server 1 id người dùng
     * Trả về cho client tất cả thông tin về các sản phẩm đã đặt hàng trước đó
     *
     * @return
     * @Status Đơn hàng đã nhận
     * Đơn hàng đang vận chuyển
     */
    @GET(URL_BILL)
    Call<List<Order>> loadBill(@Header("x-access-token") String token, @Query("type") int status);

    /**
     * CV: Xác nhận, hủy đơn hàng:
     * -Đơn hàng chỉ có thể hủy nếu status = 0, và chỉ có thể nhận nếu Status=1 tức đơn hàng đang chờ xác nhận
     * +message: Hủy đơn hàng thành công
     * +error: Lỗi...
     */
    @FormUrlEncoded
    @PUT(URL_REPLACE_STATUS_ORDED)
    Call<ResponseBody> updateStatusOrder(
            @Header("x-access-token") String token
            , @Field("idbill") int idbill
            , @Field("status") int status);


}
